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

Comparator<Student> byName = (left, right) -> left.getName().compareToIgnoreCase(right.getName());

int index = Collections.binarySearch(this.students, student, byName);

if(index < 0)
{
index = -(index + 1);
}

this.students.add(student);

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
StudentDAOInterface studentDAOInterface;
studentDAOInterface = new StudentDAO();
try
{
StudentDTOInterface studentDTOInterface;
studentDTOInterface = new StudentDTO(
student.getRollNumber(),
student.getName(),
student.getGender().charAt(0)
 );
studentDAOInterface.update(studentDTOInterface);
Student oldStudent = this.rollNumberMap.get(student.getRollNumber());
if(oldStudent != null)
{
oldStudent.setName(student.getName());
oldStudent.setGender(student.getGender());
}
Collections.sort(this.students, (left, right) -> {
return left.getName().compareToIgnoreCase(right.getName());
});
this.fireTableDataChanged();
}
catch(DLException dlException)
{
throw new RuntimeException(dlException.getMessage());
}
}
public void removeStudent(int rollNumber)
{
StudentDAOInterface studentDAOInterface;
studentDAOInterface = new StudentDAO();
try
{
studentDAOInterface.deleteByRollNumber(rollNumber);
student = this.rollNumberMap.get(rollNumber);
if(student != null)
{
this.students.remove(student);
this.rollNumberMap.remove(rollNumber);
}
this.fireTableDataChanged();
}
catch(DLException dlException)
{
throw new RuntimeException(dlException.getMessage());
}
}

public int searchStudentByRollNumber(int rollNumber)
{
int idx=0;
for(Student student:this.students)
{
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
this.students=new ArrayList<>();
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

