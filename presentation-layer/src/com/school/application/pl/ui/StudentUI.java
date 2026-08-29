package com.school.application.pl.ui;
import com.school.application.pl.pojo.*;
import java.util.*;
import com.school.dl.exceptions.*;
import com.school.dl.dao.interfaces.*;
import com.school.dl.dto.interfaces.*;
import com.school.dl.dao.*;
import com.school.dl.dto.*;
import com.tm.io.*;
public class StudentUI
{
private int sortedBy;
private int studentsListSortedBy;
private Map<Integer,Student> studentsMap;
private List<Student> studentsList;
private List<Student> rollNumberWiseStudentsList;
public StudentUI()
{
loadDataStructure();
}
private void loadDataStructure()
{
this.studentsList=new ArrayList<> ();
this.rollNumberWiseStudentsList = new ArrayList<> ();
this.studentsMap= new HashMap<Integer, Student>();
try
{
StudentDAOInterface studentDAOInterface;
studentDAOInterface = new StudentDAO();
List<StudentDTOInterface> students=studentDAOInterface.getAll();
Student student;
String gender;
for(StudentDTOInterface studentDTOInterface: students)
{
gender=(studentDTOInterface.getGender()=='M')?"Male":"Female";
student= new Student(studentDTOInterface.getRollNumber(), studentDTOInterface.getName(), gender);
this.studentsList.add(student);
this.rollNumberWiseStudentsList.add(student);
this.studentsMap.put(new Integer(student.getRollNumber()),student);
}
Collections.sort(studentsList);
Collections.sort(rollNumberWiseStudentsList);
Collections.sort(this.studentsList,(left,right)->{
int result=left.getName().compareToIgnoreCase(right.getName());
return (result!=0)?result:left.getRollNumber()-right.getRollNumber();
});
this.sortedBy=1;
this.studentsListSortedBy=2;
}
catch(DLException dlException)
{
System.out.println("Unable to fetch data !! "+dlException.getMessage());
System.exit(0);
}

}
public void add()
{
String name;
String gender;
int genderChoice;
while(true)
{
stdout.clearTerminal();
System.out.println("Student (Add Module)");
name=stdin.getString("Enter Name: ");
if(name==null || name.length()==0 || name.length()>50)
{
System.out.println("Name required between (1-50) characters.");
return;

}
System.out.println("1. Male");
System.out.println("2. Female");
genderChoice=stdin.getInt("Select Gender(1/2): ");
if(genderChoice<1 || genderChoice>2)
{
System.out.println("Input (1/2) required!");
return;
}
char yn;
while(true)
{
yn=stdin.getChar("Save (Y/N): ");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid choice, (Y/N) required");
}
if(yn=='N')
{
System.out.println("Student not added!");
while(true)
{
yn=stdin.getChar("Add another(Y/N)");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid choice, (Y/N) required");
}
if(yn=='N') break;
else continue;



}//if ends

gender=(genderChoice==1)?"Male":"Female";
StudentDTOInterface studentDTOInterface;
studentDTOInterface = new StudentDTO();
studentDTOInterface.setRollNumber(0);
studentDTOInterface.setName(name);
studentDTOInterface.setGender(gender.charAt(0));
try
{
StudentDAOInterface studentDAOInterface;
studentDAOInterface = new StudentDAO();
studentDAOInterface.add(studentDTOInterface);
Student student = new Student(studentDTOInterface.getRollNumber(),name,gender);
studentsMap.put(new Integer(student.getRollNumber()),student);
int insertAt;
insertAt=Collections.binarySearch(rollNumberWiseStudentsList, student);
insertAt=-(insertAt+1);
rollNumberWiseStudentsList.add(insertAt, student);
if(this.studentsListSortedBy==2)
{
insertAt=Collections.binarySearch(studentsList, student, (left, right)->{
int result= left.getName().compareToIgnoreCase(right.getName());
return (result!=0)? result: left.getRollNumber()-right.getRollNumber();
} );

studentsList.add(-(insertAt+1),student);
}
else if(this.studentsListSortedBy==3)
{
insertAt=Collections.binarySearch(studentsList, student, (left, right)->{
int result= left.getGender().compareTo(right.getGender());
return(result!=0)?result:left.getRollNumber()-right.getRollNumber();
});
studentsList.add(-(insertAt+1), student);
}
System.out.println("Student added and roll number alloted is: "+student.getRollNumber());
while(true)
{
yn=stdin.getChar("Add another (Y/N)");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid choice, (Y/N) required");

}
if(yn=='N') break;

}
catch(DLException dlException)
{

}
} // infinite loop ends
}// Add ends
//----------------------------------displayList-------------------
public void displayList()
{
if(studentsList.size()==0)
{
System.out.println("List of Students");
stdout.drawLine(25,'-');
System.out.print("S.No.");
stdout.drawChar(' ',2);
System.out.print("Roll No.");
stdout.drawChar(' ',2);
System.out.print("Name");
stdout.drawChar(' ',38);
System.out.print("Gender");
stdout.drawChar(' ',25);
System.out.println("          No Records");
stdout.drawLine(25, '-');
stdin.waitForEnterKey("Press ENTER to continue...");
return;

} //if ends
String choice;
boolean newPage=true;
int pageSize=5;
int pageNumber=1;
int sno=1;
int indexOfEqualTo;
int number;
int numberOfStudents=studentsList.size();
int numberOfPages=numberOfStudents/pageSize+((numberOfStudents % pageSize !=0)?1:0);
Student student;
String oneCharOptions="NPXFL";
List<Student> list=(this.sortedBy==1)?rollNumberWiseStudentsList:studentsList;
while(true)
{
if(newPage==true)
{
sno=(pageNumber-1)* pageSize +1;
stdout.clearTerminal();
System.out.println("List Of Students");
System.out.println("Page: "+pageNumber + "/"+numberOfPages);
stdout.drawLine(25, '-');
System.out.print("S.No.");
stdout.drawChar(' ',2);
System.out.print("Roll No.");
stdout.drawChar(' ',2);
System.out.print("Name");
stdout.drawChar(' ',38);
System.out.print("Gender");
stdout.drawChar(' ',25);
newPage=false;
}
student=list.get(sno-1);
System.out.printf("%5d", sno);
stdout.drawChar(' ',2);
System.out.printf("%10d", student.getRollNumber());
stdout.drawChar(' ',2);
System.out.printf("%-20s", student.getName());
stdout.drawChar(' ',2);
System.out.println(student.getGender());
if(sno%pageSize==0 || sno==studentsList.size())
{
stdout.drawLine(25, '-');
newPage=true;
System.out.println("(N)ext, (P)revious, (F)irst, (L)ast, E(x)it");

System.out.println("(page=n) (roll number=n)");
choice=stdin.getString("Enter your choice: ");
choice=choice.toUpperCase();
boolean invalidChoice=false;
if(choice.length()==1 && oneCharOptions.indexOf(choice)==-1)
{
invalidChoice=true;
}
else
{
if(choice.length()==1)
{
if(choice.equals("N"))
{
if(pageNumber<numberOfPages) pageNumber++;
}
else if(choice.equals("P"))
{
if(pageNumber>1) pageNumber--;
}
else if(choice.equals("F"))
{
pageNumber=1;
}
else if(choice.equals("L"))
{
pageNumber=numberOfPages;
}

} //if ends
else
{
choice=choice.toUpperCase().replaceAll(" ",  "");
if(choice.startsWith("ROLLNUMBER="))
{
// search by roll number
indexOfEqualTo=choice.indexOf("=");
if(indexOfEqualTo < choice.length()-1)
{
try
{
number=Integer.parseInt(choice.substring(indexOfEqualTo+1));

if(number>0)
{

}

} //try ends
catch(NumberFormatException numberFormatException)
{}
}
}// if of rollnumber
else if(choice.startsWith("PAGE="))
{
//goto page
indexOfEqualTo=choice.indexOf("=");
if(indexOfEqualTo < choice.length()-1)
{
try
{
number=Integer.parseInt(choice.substring(indexOfEqualTo+1));

if(number>=1 && number<=numberOfPages)
{
pageNumber=number;
}

} //try ends
catch(NumberFormatException numberFormatException)
{}

}
}
}// else ends

}
if(invalidChoice)
{
sno=sno-pageSize+1;
continue;
}
if(choice.equals("X")) break;

}
else
{
sno++;
}
} //display ends
}
public void sortByRollNumber()
{
//if(this.sortedBy==1) return;
//Collections.sort(this.studentsList);
this.sortedBy=1;
//this.studentsListSortedBy=1;
}
public void sortByName()
{
if(this.sortedBy==2) return;
if(this.studentsListSortedBy==2)
{
this.sortedBy=2;
return;
}
Collections.sort(this.studentsList,(left,right)->{
return left.getName().compareToIgnoreCase(right.getName());
});
this.sortedBy=2;
this.studentsListSortedBy=2;

}
public void sortByGender()
{
if(this.sortedBy==3) return;
if(this.studentsListSortedBy==3)
{
this.sortedBy=3;
return;
}
Collections.sort(this.studentsList,(left,right)->{
return left.getGender().compareTo(right.getGender());
});
this.studentsListSortedBy=3;
this.sortedBy=3;
}


public void delete()
{
int rollNumber;
while(true)
{
stdout.clearTerminal();
System.out.println("Student (Delete Module)");
rollNumber=stdin.getInt("Enter roll number you want to delete...");
if(rollNumber<=0)
{
System.out.println("Invalid Roll no.");
return;
} //if(rollNumber<=0) ends
Student student=studentsMap.get(rollNumber);
char yn;
if(student==null)
{
System.out.println("That roll n. student does not exist!");
while(true)
{
yn=stdin.getChar("Delete Another (Y/N): ");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid Choice: (Y/N) required..");
}
if(yn=='Y') continue;
else return;
} //if(student ==null) ends
System.out.println("Name: "+student.getName());
System.out.println("Gender: "+student.getGender());
while(true)
{
yn=stdin.getChar("Delete (Y/N): ");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid Choice: (Y/N) required..");
}
if(yn=='N')
{
while(true)
{
yn=stdin.getChar("No selected, Delete another (Y/N): ");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid Choice: (Y/N) required..");
}
if(yn=='N') return;
else continue;
} //if 'N' part ends
try
{
StudentDAOInterface studentDAOInterface;
studentDAOInterface = new StudentDAO();
studentDAOInterface.deleteByRollNumber(rollNumber);
studentsMap.remove(rollNumber);
int index=Collections.binarySearch(rollNumberWiseStudentsList, student);
rollNumberWiseStudentsList.remove(index);
if(this.studentsListSortedBy==2)
{
index=Collections.binarySearch(studentsList, student, (left, right)->{
int result= left.getName().compareToIgnoreCase(right.getName());
return (result!=0)? result : left.getRollNumber()-right.getRollNumber();
} );

studentsList.remove(index);
}
else if(this.studentsListSortedBy==3)
{
index=Collections.binarySearch(studentsList,student,  (left, right)->{
int result= left.getGender().compareTo(right.getGender());
return(result!=0)?result:left.getRollNumber()-right.getRollNumber();
});
studentsList.remove(index);
}
 
while(true)
{
yn=stdin.getChar("Student deleted, Delete another (Y/N): ");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid Choice: (Y/N) required..");
}
if(yn=='N') break;
}
catch(DLException dlException)
{
System.out.println("Unable to delete: "+dlException.getMessage());
break;
}
}
}
public void update()
{
int rollNumber;
while(true)
{
stdout.clearTerminal();
System.out.println("Student (Update Module)");
rollNumber=stdin.getInt("Enter roll number you want to update...");
if(rollNumber<=0)
{
System.out.println("Invalid Roll no.");
return;
} //if(rollNumber<=0) ends
Student student=studentsMap.get(rollNumber);
char yn;
if(student==null)
{
System.out.println("That roll n. student does not exist!");
while(true)
{
yn=stdin.getChar("Update Another (Y/N): ");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid Choice: (Y/N) required..");
}
if(yn=='Y') continue;
else return;
} //if(student ==null) ends
System.out.println("Name: "+student.getName());
System.out.println("Gender: "+student.getGender());
while(true)
{
yn=stdin.getChar("Update (Y/N): ");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid Choice: (Y/N) required..");
}
if(yn=='N')
{
while(true)
{
yn=stdin.getChar("Student not selected, Update another (Y/N): ");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid Choice: (Y/N) required..");
}
if(yn=='N') return;
else continue;
} //if 'N' part ends
//Ask new data
String name;
String gender;
int genderChoice;
name=stdin.getString("Enter name");
if(name==null || name.length()==0 || name.length()>50)
{
System.out.println("Name required between (1-50) characters.");
return;

}
System.out.println("1. Male");
System.out.println("2. Female");
genderChoice=stdin.getInt("Select Gender(1/2): ");
if(genderChoice<1 || genderChoice>2)
{
System.out.println("Input (1/2) required!");
return;
}
while(true)
{
yn=stdin.getChar("Save (Y/N): ");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid choice, (Y/N) required");
}
if(yn=='N')
{
System.out.println("Student not updated!");
while(true)
{
yn=stdin.getChar("Update another(Y/N)");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid choice, (Y/N) required");
}
if(yn=='N') break;
else continue;



}//if ends

gender=(genderChoice==1)?"Male":"Female";
boolean updated=false;
try
{
StudentDAOInterface studentDAOInterface;
studentDAOInterface = new StudentDAO();
StudentDTOInterface studentDTOInterface;
studentDTOInterface= new StudentDTO();
studentDTOInterface.setRollNumber(rollNumber);
studentDTOInterface.setName(name);
studentDTOInterface.setGender(gender.charAt(0));
studentDAOInterface.update(studentDTOInterface);
updated=true;
//now remove from list old one
int index;
if(studentsListSortedBy==2)
{
index=Collections.binarySearch(studentsList, student, (left,right)->{
int result=left.getName().compareToIgnoreCase(right.getName());
return (result!=0)? result : left.getRollNumber()-right.getRollNumber();
});
studentsList.remove(index);
} // if(studentsListSortedBy==2) ends
else if(studentsListSortedBy==3)
{
index=Collections.binarySearch(studentsList, student, (left,right)->{
int result=left.getGender().compareTo(right.getGender());
return (result!=0)? result : left.getRollNumber()-right.getRollNumber();
});
studentsList.remove(index);

} //else if(studentsListSortedBy==3) ends
//update object now, no need to do anything in Map
student.setName(name);
student.setGender(gender);
//find position in list and insert it
int insertAt;
if(this.studentsListSortedBy==2)
{
insertAt=Collections.binarySearch(studentsList, student, (left, right)->{
int result= left.getName().compareToIgnoreCase(right.getName());
return (result!=0)? result: left.getRollNumber()-right.getRollNumber();
} );

studentsList.add(-(insertAt+1),student);
}
else if(this.studentsListSortedBy==3)
{
insertAt=Collections.binarySearch(studentsList, student, (left, right)->{
int result= left.getGender().compareTo(right.getGender());
return(result!=0)?result:left.getRollNumber()-right.getRollNumber();
});
studentsList.add(-(insertAt+1), student);
}
}catch(DLException dlException)
{
System.out.println("Unable to delete: "+dlException.getMessage());
break;
}
while(true)
{
yn=stdin.getChar("Student"+((updated)?" ":" not ")+"updated, edit another(Y/N): ");
if(yn>=97 && yn<=122) yn=(char)(yn-32);
if(yn=='Y' || yn=='N') break;
System.out.println("Invalid choice, (Y/N) required");

} //last loop
if(yn=='N') break;

}
}
}





