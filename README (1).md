# Student Management System

A multi-module Java application built to practice and demonstrate the **DAO/DTO design pattern** and **N-tier (layered) architecture**. The project cleanly separates data storage from business/UI logic, and includes **two interchangeable data storage implementations** and **two interchangeable presentation layers**, all built against the same interface contracts.

> **Scope note:** This README covers the project up through the Presentation Layer only (interfaces → implementations → UI). The networking layer (TCP generic server, socket-based server, and the DAO proxy client used to talk to it) is a separate, later extension and is intentionally left out of this document.

---

## Architecture

The system follows a classic layered architecture with strict interface-based decoupling:

```
Presentation Layer  →  Data Access Layer (Interfaces)  →  Data Access Layer (Implementation)
   (UI / Console)         (contracts only)                  (RandomAccessFile or JDBC/MySQL)
```

The presentation layer never depends on a concrete DAO/DTO class directly — only on the interfaces. This means either data-layer implementation can be swapped in without touching any UI code, and either UI can be swapped in without touching the data layer.

### Modules

| Module | Role | Description |
|---|---|---|
| `datalayer_interfaces` | Contracts | Defines `StudentDAOInterface`, `StudentDTOInterface`, and `DLException` — the shared contract every other module codes against. |
| `datalayer_implementations` | Data Layer (v1) | File-based storage. Implements the DAO/DTO interfaces using `RandomAccessFile` for persistence, with an auto-incrementing roll number counter. |
| `dl2` | Data Layer (v2) | Database-backed storage. Implements the same interfaces using **JDBC + MySQL**, including a `DAOConnection` helper for connection management. |
| `presentation-layer` | UI (v1) | Console/terminal-based menu UI. Reads/writes through the DAO interface directly and does its own in-memory sorting, searching, and pagination. |
| `pl2` | UI (v2) | Desktop GUI built with **Java Swing**, using a `TableModel` for the student list, form validation via `DocumentFilter`, and **PDF export via iText**. |

### Package structure

```
com.school.dl.dao.interfaces     → StudentDAOInterface
com.school.dl.dto.interfaces     → StudentDTOInterface
com.school.dl.exceptions         → DLException

com.school.dl.dao                → StudentDAO (RandomAccessFile impl, or JDBC impl in dl2)
com.school.dl.dto                → StudentDTO

com.school.application.pl.ui     → StudentUI (console version, in presentation-layer)
com.school.application.pl.pojo   → Student (console version)

com.school.pl.ui                 → StudentUI (Swing version, in pl2)
com.school.pl.model              → StudentTableModel (Swing TableModel for the JTable)
com.school.pl.pojo               → Student (Swing version)
```

---

## Design pattern: DAO / DTO

- **DTO (`StudentDTOInterface` / `StudentDTO`)** — a plain data carrier for a student's `rollNumber`, `name`, and `gender`. It implements `Serializable` so it can be passed across process/network boundaries if needed later.
- **DAO (`StudentDAOInterface`)** — exposes the persistence operations a caller can perform, independent of how they're implemented:
  ```java
  void add(StudentDTOInterface student) throws DLException;
  void update(StudentDTOInterface student) throws DLException;
  void deleteByRollNumber(int rollNumber) throws DLException;
  StudentDTOInterface getByRollNumber(int rollNumber) throws DLException;
  List<StudentDTOInterface> getAll() throws DLException;
  ```
- **`DLException`** — a single checked exception type used to surface any data-layer failure (validation errors, I/O errors, SQL errors) to the caller in a storage-agnostic way.

Both DAO implementations enforce the same validation rules before touching storage: roll number must be system-assigned on `add`, name is required and trimmed, and gender must be `M`/`F` (case-insensitive).

---

## Data Layer implementations

### 1. File-based (`datalayer_implementations`)
- Persists records as plain text in `data/school-student.data`, one student per 3 lines (roll number, name, gender).
- Roll numbers are auto-incremented using a separate counter file, `data/school-student.auto`.
- `update`/`delete` work by streaming the data file into a temp file (`tmp.tmp`) with the change applied, then copying it back — a simple but instructive approach to file-based record management with `RandomAccessFile`.

### 2. JDBC/MySQL-based (`dl2`)
- `DAOConnection` centralizes connection creation via `com.mysql.cj.jdbc.Driver`.
- CRUD operations are implemented with `PreparedStatement`s against a `stud` table.
- `add` uses `Statement.RETURN_GENERATED_KEYS` to retrieve the database-assigned roll number after insert.

Because both implementations satisfy the same `StudentDAOInterface`, either one can be dropped into a presentation layer's classpath without any code changes above the data layer.

---

## Presentation Layer implementations

### 1. Console UI (`presentation-layer`)
- Menu-driven terminal application (`StudentUITestCase`) with options to add, list, sort, delete, and update students.
- Loads all students into in-memory structures (`List`, `HashMap`) on startup for fast lookups by roll number.
- Supports **pagination** and **inline search** in the list view via typed commands (e.g. `PAGE=2`, `ROLLNUMBER=101`, `N`/`P`/`F`/`L` to navigate pages).
- Supports sorting the in-memory list by roll number, name, or gender.

### 2. Swing GUI (`pl2`)
- Full desktop GUI (`StudentUI extends JFrame`) built on a `MODES` enum (`VIEW`, `EDIT`, `ADD`, `DELETE`, `EXPORT_TO_PDF`) that drives which controls are active/visible.
- `StudentTableModel` (an `AbstractTableModel`) backs a `JTable` showing serial number, roll number, and name.
- Input validation at the UI level via a `DocumentFilter` on the name field, plus radio buttons for gender selection.
- **Exports the student list to PDF** using iText (`itextpdf-5.5.13.5.jar`), producing a formatted table document.

---

## Tech stack

- **Language:** Java
- **UI:** Java Swing (`pl2`), plain console I/O (`presentation-layer`)
- **Persistence:** `java.io.RandomAccessFile` (flat-file) and JDBC + MySQL (`mysql-connector-java-8.0.20`)
- **PDF generation:** iText (`itextpdf-5.5.13.5`)
- **Build:** plain `javac`/`java` via classpath, with `cmp.bat` / `rn.bat` scripts per module (no Maven/Gradle)

---

## Project structure

```
app1/
├── datalayer_interfaces/       # DAO/DTO contracts + DLException
│   ├── src/
│   └── dist/datalayer-interfaces.jar
├── datalayer_implementations/  # File-based DAO/DTO implementation
│   ├── src/
│   ├── testcases/              # JUnit-style test cases for each DAO method
│   └── dist/datalayer-implementations.jar
├── dl2/                        # JDBC/MySQL DAO implementation
│   ├── src/
│   └── dist/
├── presentation-layer/         # Console UI
│   ├── src/
│   └── testcases/
└── pl2/                        # Swing GUI (with PDF export)
    ├── src/
    ├── dependencies/           # datalayer-interfaces.jar, itextpdf, mysql-connector, ioutils
    └── testcases/icons/        # UI icons
```

---

## Building & running

Each module is compiled independently against its dependencies via classpath (see each module's `testcases/cmp.bat` and `rn.bat`). General pattern:

```bash
# Compile the interfaces module first
javac -d classes src/com/school/dl/**/*.java

# Compile a data layer implementation against the interfaces jar
javac -classpath datalayer-interfaces.jar -d classes src/com/school/dl/**/*.java

# Compile & run the console UI against a chosen data layer implementation
javac -classpath "dependencies/*;classes;." -d classes testcases/StudentUITestCase.java
java  -classpath "dependencies/*;classes;." StudentUITestCase

# Compile & run the Swing UI against a chosen data layer implementation
javac -classpath "dependencies/*;classes;." -d classes testcases/TestStudentUI.java
java  -classpath "dependencies/*;classes;." TestStudentUI
```

> The JDBC-based data layer (`dl2`) expects a MySQL database reachable at `jdbc:mysql://localhost:3306/java_examples_db_1` with a `stud` table and appropriate credentials — update `DAOConnection` if your setup differs.

---
