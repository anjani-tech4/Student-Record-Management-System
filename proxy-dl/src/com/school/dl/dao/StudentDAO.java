package com.school.dl.dao;
import java.io.*;
import java.net.*;
import com.school.net.*;
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
String requestData=DataProtocol.ADD_STUDENT+","+rollNumber+","+name+","+gender+"#";
Socket socket;
try
{
socket=new Socket(ServerConfiguration.IP, ServerConfiguration.PORT);
OutputStream outputStream;
outputStream = socket.getOutputStream();
OutputStreamWriter outputStreamWriter;
outputStreamWriter= new OutputStreamWriter(outputStream);
outputStreamWriter.write(requestData);
outputStreamWriter.flush();
InputStream inputStream;
inputStream= socket.getInputStream();

InputStreamReader inputStreamReader;
inputStreamReader=new InputStreamReader(inputStream);
StringBuffer stringBuffer= new StringBuffer();
int x;
while(true)
{
x=inputStreamReader.read();
if(x==-1 || x=='#') break;
stringBuffer.append((char)x);
}
String responseData=stringBuffer.toString();
String splits[];
splits=responseData.split(",");
if(splits[0].equals(DataProtocol.SUCCESS))
{
studentDTOInterface.setRollNumber(Integer.parseInt(splits[1]));
}
else if(splits[1].equals(DataProtocol.FAILURE))
{
throw new DLException(splits[1]);
}

}
catch(IOException ioException)
{
    throw new DLException(ioException.getMessage());
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
throw new DLException("not impl.");
}


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
public void deleteByRollNumber(int rollNumber) throws DLException
{
if(rollNumber<=0)
{
throw new DLException("Invalid Roll Number "+rollNumber);
}
throw new DLException("not impl.");





}
public StudentDTOInterface getByRollNumber(int rollNumber) throws DLException
{
if(rollNumber<=0)
{
throw new DLException("Invalid Roll Number "+rollNumber);
}
throw new DLException("not impl.");

}
public List<StudentDTOInterface> getAll() throws DLException
{
List<StudentDTOInterface> students=new ArrayList<>();
String requestData=DataProtocol.GET_STUDENTS+"#";
Socket socket;
try
{
socket=new Socket(ServerConfiguration.IP, ServerConfiguration.PORT);
OutputStream outputStream;
outputStream = socket.getOutputStream();
OutputStreamWriter outputStreamWriter;
outputStreamWriter= new OutputStreamWriter(outputStream);
outputStreamWriter.write(requestData);
outputStreamWriter.flush();
InputStream inputStream;
inputStream= socket.getInputStream();

InputStreamReader inputStreamReader;
inputStreamReader=new InputStreamReader(inputStream);
StringBuffer stringBuffer= new StringBuffer();
int x;
while(true)
{
x=inputStreamReader.read();
if(x==-1 || x=='#') break;
stringBuffer.append((char)x);
}
String responseData=stringBuffer.toString();
String splits[];
splits=responseData.split(",");
if(splits[0].equals(DataProtocol.SUCCESS))
{
StudentDTOInterface studentDTOInterface;
int rollNumber;
String name;
char gender;
int i;
for(i=1;i<splits.length;i+=3)
{
rollNumber=Integer.parseInt(splits[i]);
name=splits[i+1];
gender=splits[i+2].charAt(0);
studentDTOInterface=new StudentDTO(rollNumber,name,gender);
students.add(studentDTOInterface);
}

}
else if(splits[0].equals(DataProtocol.FAILURE))
{
throw new  DLException(splits[1]);
}
}
catch(IOException ioException)
{
throw new DLException(ioException.getMessage());
}
System.out.println("Students fetched = " + students.size());
return students;

}
}

