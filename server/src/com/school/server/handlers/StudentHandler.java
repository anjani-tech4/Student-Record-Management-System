package com.school.server.handlers;
import com.school.server.*;
import com.school.dl.dao.interfaces.*;
import com.school.dl.dao.*;
import com.school.dl.dto.interfaces.*;
import com.school.dl.dto.*;
import com.school.dl.exceptions.*;
import com.school.net.*;
import java.util.*;
public class StudentHandler implements RequestHandlerInterface
{
public String performAction(String actionName, String splits[])
{
if(actionName.equals(DataProtocol.ADD_STUDENT))
{
return this.addStudent(splits);
}//if ends
else if(actionName.equals(DataProtocol.GET_STUDENTS))
{
return this.getStudents();
}
return "";

}
private String addStudent(String splits[])
{
String response;
int rollNumber=Integer.parseInt(splits[1]);
String name=splits[2];
char gender=splits[3].charAt(0);
StudentDTOInterface studentDTOInterface;
studentDTOInterface= new StudentDTO(rollNumber, name, gender);
try
{
StudentDAOInterface studentDAOInterface;
studentDAOInterface= new StudentDAO();
studentDAOInterface.add(studentDTOInterface);
response=DataProtocol.SUCCESS+","+studentDTOInterface.getRollNumber()+"#";
}catch(DLException dlException)
{
response=DataProtocol.FAILURE+","+dlException.getMessage()+"#";
}
return response;
}
private String getStudents()
{
String response;
StudentDAOInterface studentDAOInterface;
studentDAOInterface= new StudentDAO();
System.out.println(StudentDAO.class.getProtectionDomain().getCodeSource().getLocation());
List<StudentDTOInterface> students;
try
{
students=studentDAOInterface.getAll();
System.out.println("Students fetched = " + students.size());
response=DataProtocol.SUCCESS;
for(StudentDTOInterface studentDTOInterface: students)
{
response=response+","+studentDTOInterface.getRollNumber()+","+studentDTOInterface.getName()+","+studentDTOInterface.getGender();
}
response=response+"#";
}
catch(DLException dlException)
{
response=DataProtocol.FAILURE+","+dlException.getMessage()+"#";
}
System.out.println("Students fetched = ");
System.out.println(response);
return response;
}

}
