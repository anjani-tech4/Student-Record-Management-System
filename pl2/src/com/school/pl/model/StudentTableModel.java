package com.school.pl.model;
import java.util.*;
import com.school.dl.dao.interfaces.*;
import com.school.dl.dao.*;
import com.school.dl.dto.*;
import com.school.dl.dto.interfaces.*;
import com.school.dl.exceptions.*;
import com.school.pl.model.exceptions.*;
import javax.swing.table.*;
import com.school.pl.pojo.*;

public class StudentTableModel extends AbstractTableModel
{
private Class [] columnClasses = {Integer.class, Integer.class, String.class};

private String [] columnNames={"S.No.", "Roll number", "Name"};
private List<Student> students;
//one more data structure required to search by roll number
private Map<Integer, Student> rollNumberMap;
public StudentTableModel()
{
populateDataStructure();
}
public int getRowCount()
{
return this.students.size();
}
public int getColumnCount()
{
return 3; //s.no., roll.no., name
}
public String getColumnName(int index)
{
return columnNames[index];
}
public Class getColumnClass(int index)
{
return columnClasses[index];
}
public boolean isCellEditable(int rowIndex, int columnIndex)
{
return false;
}
public Object getValueAt(int rowIndex, int columnIndex)
{
if(columnIndex==0) return new Integer(rowIndex+1);
Student student=this.students.get(rowIndex);
if(columnIndex==1) return new Integer(student.getRollNumber());
return student.getName();
}
public void setValueAt(Object object, int rowIndex, int columnIndex)
{

}
public Student getStudentByIndex(int index)
{
return this.students.get(index);
}
//custom methods
public int addStudent(Student student) throws ModelException
{
String name=student.getName();
if(name!=null) name=name.trim();
String gender=student.getGender();
if(name==null || name.length()==0)
{
throw new ModelException("Name required");
}
if(name.length()>50)
{
throw new ModelException("Name cannot exceed 50 characters");
}
gender=gender.toUpperCase();
if(gender.equals("MALE")==false && gender.equals("FEMALE")==false)
{
throw new ModelException("Invalid gender, Male / Female expected");
}
StudentDTOInterface studentDTOInterface;
studentDTOInterface= new StudentDTO(0, name, gender.charAt(0));
StudentDAOInterface  studentDAOInterface;
studentDAOInterface = new StudentDAO();
try
{
studentDAOInterface.add(studentDTOInterface);
int rollNumber=studentDTOInterface.getRollNumber();
student.setRollNumber(rollNumber);
// same comparator jo populateDataStructure() mein use hua hai (name ke hisaab se sorted list)
Comparator<Student> byName = (left, right) -> left.getName().compareToIgnoreCase(right.getName());

// binary search se sahi insertion point dhundo
int index = Collections.binarySearch(this.students, student, byName);

// binarySearch agar exact match nahi milta to negative value return karta hai:
// (-(insertion point) - 1). Isse actual insertion point nikaalte hain.
if(index < 0)
{
index = -(index + 1);
}

// us index pe hi seedha insert kar do — poori list dobara sort karne ki zaroorat nahi
this.students.add(student);
// assignment, remove the next line and introduce  binary
// search followed by insert in students  list
Collections.sort(this.students); 
rollNumberMap.put(rollNumber,student);
int index=0;
for(Student s:this.students)
{
if(s.getRollNumber()==rollNumber) break;
index++;
}
return index;
}catch(DLException dlException)
{
throw new ModelException(dlException.getMessage());
}
}



public void updateStudent(Student student)
{

}
public void removeStudent(int rollNumber)
{

}
public int searchStudentByRollNumber(int rollNumber)
{
int idx=0;
for(Student student:this.students)
{
//iss if me fasa mtlb mil gya 
if(student.getRollNumber()==rollNumber)
{
return idx;
}
idx++;
}
return -1;

}
public int searchStudentByPartialName(String namePart)
{
int idx=0;
for(Student student:this.students)
{
//iss if me fasa mtlb mil gya 
if(student.getName().toUpperCase().startsWith(namePart.toUpperCase()))
{
return idx;
}
idx++;
}
return -1;
}

private void populateDataStructure()
{
StudentDAOInterface studentDAOInterface;
studentDAOInterface= new StudentDAO();
List<StudentDTOInterface> studentDTOList=null;
try
{
studentDTOList=studentDAOInterface.getAll();
}
catch(DLException dlException)
{
System.out.println(dlException);
System.out.println("Unable to fetch data, Contact Administrator");
System.exit(1);

}
this.students=new ArrayList<>();//vo jo students tha upr
this.rollNumberMap=new HashMap<>();
Student student;
for(StudentDTOInterface studentDTOInterface:studentDTOList)
{
String gender=(studentDTOInterface.getGender()=='M')?"Male": "Female";
student=new Student(studentDTOInterface.getRollNumber(), studentDTOInterface.getName(), gender);
this.students.add(student);
this.rollNumberMap.put(student.getRollNumber(), student);
}
Collections.sort(this.students,(left,right)->{
return left.getName().compareToIgnoreCase(right.getName());
});
}

}
/*
Workflow — Add → Save Cycle
Step 1: User "Add" button click karta hai

setAddMode() trigger hota hai
Value labels (rollNumberValueLabel, nameValueLabel, genderValueLabel) hide ho jaane chahiye
Unki jagah input components (JTextField for roll number/name, JComboBox for gender) show hone chahiye — same position pe (same bounds)
Table disabled ho jaata hai (already tumhare code mein hai)

Step 2: User data type karta hai

Roll number field — sirf numeric input allow ho (jaisa search field mein DocumentFilter use kiya tha)
Name field — normal text
Gender — dropdown/combo se select

Step 3: User "Save" click karta hai
Yahan 4 cheezein honi chahiye sequence mein:

Validation — save hone se pehle:

Roll number empty na ho, aur numeric ho
Roll number already exist na kare (duplicate check)
Name empty na ho
Gender selected ho
Agar validation fail ho, error message dikhao (JOptionPane) aur Save mode mein hi raho — data save mat karo


Data Structure Update — 14 july - StudentTableModel.addStudent(student) call ho:

students list mein naya Student object add ho
rollNumberMap mein bhi add ho (taaki future searches fast rahe)
List re-sort ho (kyunki tum naam ke hisaab se sorted rakhti ho)


Table Auto-Refresh — AbstractTableModel ka fireTableDataChanged() call hona zaroori hai:

Ye JTable ko batata hai "data badal gaya hai, khud ko repaint karo"
Iske bina table UI purana data hi dikhata rahega, chahe backend list update ho chuki ho


Naya row visible karo user ko — kyunki sorting ke baad row kahin bhi ja sakta hai:

searchStudentByRollNumber() se naya row index dhundo
table.setRowSelectionInterval(index, index) — select karo
table.scrollRectToVisible(...) — scroll karke us row tak le jao, taaki user ko turant dikhe ki add ho gaya


Fir wapas setViewMode() call ho — labels wapas aa jayen, inputs hide ho jayen
*/
