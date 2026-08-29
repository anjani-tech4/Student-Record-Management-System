package com.school.dl.dao;
import java.io.*;
import java.sql.*;
import com.school.dl.dao.interfaces.*;
import com.school.dl.dto.interfaces.*;
import com.school.dl.exceptions.*;
import com.school.dl.dto.*;
import java.util.*;
public class StudentDAO implements StudentDAOInterface
{
public StudentDAO() {}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
public void add(StudentDTOInterface studentDTOInterface) throws DLException
{
if(studentDTOInterface==null)
{
throw new DLException("Student required");
}
int rollNumber=studentDTOInterface.getRollNumber();
if(rollNumber!=0)
{
throw new DLException("Invalid roll number : "+rollNumber+", system will allot roll number");
}
String name=studentDTOInterface.getName();
if(name==null)
{
throw new DLException("Name required  ");

}
name=name.trim();
if(name.length()==0)
{
throw new DLException("Name required  ");
}
char gender=studentDTOInterface.getGender();
if("MFmf".indexOf(gender)==-1)
{
throw new DLException("Invalid gender: "+gender);

}
if(gender=='m') gender='M';
else if(gender=='f') gender='F';
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("insert into stud(name, gender) values (?,?)",Statement.RETURN_GENERATED_KEYS);
preparedStatement.setString(1, name);
preparedStatement.setString(2, String.valueOf(gender));
preparedStatement.executeUpdate();
ResultSet resultSet;
resultSet=preparedStatement.getGeneratedKeys();
resultSet.next();
int generatedRollNumber=resultSet.getInt(1);
studentDTOInterface.setRollNumber(generatedRollNumber);
resultSet.close();
connection.close();

}
catch(SQLException sqlException)
{
    throw new DLException(sqlException.getMessage());
}
}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public void update(StudentDTOInterface studentDTOInterface) throws DLException
{
if(studentDTOInterface==null)
{
throw new DLException("Name Required!!");
}
int rollNumber=studentDTOInterface.getRollNumber();
if(rollNumber<=0)
{
throw new DLException("Invalid Roll Number "+rollNumber);
}
String name=studentDTOInterface.getName();
if(name==null)
{
throw new DLException("Name Required!!");

}
name=name.trim();
if(name.length()==0)
{
throw new DLException("Name Required!!");

}
char gender=studentDTOInterface.getGender();
if("MFmf".indexOf(gender)==-1)
{
throw new DLException("Invalid gender: "+gender);

}
if(gender=='m') gender='M';
else if(gender=='f') gender='F';

try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select count(*) as cnt from stud where roll_number=?");
preparedStatement.setInt(1,rollNumber);

ResultSet resultSet;
resultSet=preparedStatement.executeQuery();

// if(resultSet.next()==false) //Answer to this ques will be 100% true.
int rowCount;
resultSet.next(); // row fetch
rowCount=resultSet.getInt("cnt");
resultSet.close();
preparedStatement.close();
if(rowCount==0)
{
connection.close();
throw new DLException("Invalid roll no. "+rollNumber); 
}
preparedStatement=connection.prepareStatement("update stud set name=? , gender=? where roll_number=? ");
preparedStatement.setString(1,name);
preparedStatement.setString(2,gender+"");
preparedStatement.setInt(3,rollNumber);
preparedStatement.executeUpdate();
preparedStatement.close();



connection.close();


}
catch(SQLException sqlException)
{
 throw new DLException(sqlException.getMessage());
}
}
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
public void deleteByRollNumber(int rollNumber) throws DLException
{
if(rollNumber<=0)
{
throw new DLException("Invalid Roll Number "+rollNumber);
}
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement= connection.prepareStatement("select roll_number from stud where roll_number=?");
preparedStatement.setInt(1,rollNumber);

ResultSet resultSet;
resultSet = preparedStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
connection.close();
throw new DLException("Invalid roll number: "+rollNumber);

}

resultSet.close();
preparedStatement.close();
preparedStatement=connection.prepareStatement("delete from stud where roll_number= ?");
preparedStatement.setInt(1, rollNumber);
preparedStatement.executeUpdate();

preparedStatement.close();
connection.close();

}

catch(SQLException sqlException)
{
 throw new DLException(sqlException.getMessage());
}

}
public StudentDTOInterface getByRollNumber(int rollNumber) throws DLException
{
if(rollNumber<=0)
{
throw new DLException("Invalid Roll Number "+rollNumber);
}
StudentDTOInterface studentDTOInterface;
try
{
Connection connection=DAOConnection.getConnection();
PreparedStatement preparedStatement;
preparedStatement=connection.prepareStatement("select name, gender from stud where roll_number=?");
preparedStatement.setInt(1,rollNumber);
ResultSet resultSet;
resultSet=preparedStatement.executeQuery();
if(!resultSet.next())
{
resultSet.close();
connection.close();
throw new DLException("Invalid roll number: "+rollNumber);
}
String vName;
char vGender;
vName=resultSet.getString("name").trim();
vGender=resultSet.getString("gender").charAt(0);
studentDTOInterface=new StudentDTO(rollNumber, vName, vGender);
resultSet.close();
preparedStatement.close();
connection.close();
return studentDTOInterface;

} //try ends
catch(SQLException sqlException)
{
 throw new DLException(sqlException.getMessage());
} //getByRollNumber ends
}
public List<StudentDTOInterface> getAll() throws DLException
{
List<StudentDTOInterface> students;
students= new ArrayList<StudentDTOInterface> ();
try
{
StudentDTOInterface studentDTOInterface;
Connection connection=DAOConnection.getConnection();
Statement statement;
statement=connection.createStatement();
ResultSet resultSet;
resultSet=statement.executeQuery("select * from stud order by roll_number");
System.out.println("Query executed successfully");
int vRollNumber;
String vName;
char vGender;
while(resultSet.next())
{
  System.out.println("One row found");
vRollNumber=resultSet.getInt("roll_number");
vName=resultSet.getString("name").trim();
vGender=resultSet.getString("gender").charAt(0);
System.out.println(
        "Data = " + vRollNumber + " " + vName + " " + vGender
    );
studentDTOInterface=new StudentDTO(vRollNumber, vName, vGender);
students.add(studentDTOInterface);

}
resultSet.close();
statement.close();
connection.close();



}
catch(SQLException sqlException)
{
 throw new DLException(sqlException.getMessage());
}
System.out.println("Students fetched from DB = " + students.size());
return students;
}
}

