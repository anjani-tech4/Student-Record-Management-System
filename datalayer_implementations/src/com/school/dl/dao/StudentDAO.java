package com.school.dl.dao;
import java.io.*;
import com.school.dl.dao.interfaces.*;
import com.school.dl.dto.interfaces.*;
import com.school.dl.exceptions.*;
import com.school.dl.dto.*;
import java.util.*;
public class StudentDAO implements StudentDAOInterface
{
private static final String DATA_FILE;
private static final String AUTO_DATA_FILE;
static 
{
AUTO_DATA_FILE="data"+File.separator+"school-student.auto";
DATA_FILE="data"+File.separator+"school-student.data";


try
{
File dataDirectory=new File("data");
if(dataDirectory.exists())
{
if(dataDirectory.isDirectory()==false)
{
System.out.println("Application directory contains a file named as data, please delete it!");
System.out.println("Application aborted!");
System.exit(1);

}
}
else
{
dataDirectory.mkdir();
}
File studentAutoIncrementDataFile;
String studentAutoIncrementDataFilePath;
studentAutoIncrementDataFilePath = "data"+File.separator+"school-student.auto";
studentAutoIncrementDataFile=new File(studentAutoIncrementDataFilePath);
if(studentAutoIncrementDataFile.exists()==false)
{
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(studentAutoIncrementDataFile, "rw");
randomAccessFile.writeBytes("0\n");
randomAccessFile.close();

}
}
catch(Exception exception)
{
System.out.println("Critical Issue: "+exception.getMessage());
System.out.println("Application aborted!");
System.exit(1);
}
}
public StudentDAO() {}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
public void add(StudentDTOInterface studentDTOInterface) throws DLException
{
if(studentDTOInterface==null)
{
}
int rollNumber=studentDTOInterface.getRollNumber();
if(rollNumber!=0)
{
throw new DLException("Invalid Roll Number: "+rollNumber);

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
File file=new File(DATA_FILE);
int vRollNumber;
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file, "rw");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
vRollNumber=Integer.parseInt(randomAccessFile.readLine());
if(vRollNumber==rollNumber)
{
randomAccessFile.close();
throw new DLException("Roll no. "+rollNumber+" already exists");
}
randomAccessFile.readLine();
randomAccessFile.readLine();
}
File autoDataFile=new File(AUTO_DATA_FILE);
RandomAccessFile autoRandomAccessFile;
autoRandomAccessFile= new RandomAccessFile(autoDataFile, "rw");
rollNumber=Integer.parseInt(autoRandomAccessFile.readLine());
rollNumber++;
autoRandomAccessFile.seek(0);
autoRandomAccessFile.writeBytes(rollNumber+"\n");
autoRandomAccessFile.close();

randomAccessFile.writeBytes(rollNumber+"\n");
randomAccessFile.writeBytes(name+"\n");
randomAccessFile.writeBytes(gender+"\n");
randomAccessFile.close();
studentDTOInterface.setRollNumber(rollNumber);

}
catch(IOException ioException)
{
throw new DLException("Unable to add: "+ioException.getMessage());
}
}

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
int vRollNumber;
String vName;
char vGender;

File file= new File(DATA_FILE);
if(file.exists()==false)
{
throw new DLException("Invalid Roll Number "+rollNumber);
}
RandomAccessFile randomAccessFile;
randomAccessFile = new RandomAccessFile(file, "rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DLException("Roll number "+rollNumber+ "does not exist!");
}


boolean found = false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
vRollNumber=Integer.parseInt(randomAccessFile.readLine());
if(vRollNumber==rollNumber)
{
found = true;
break;
} // if ends
randomAccessFile.readLine();
randomAccessFile.readLine();
}
if(!found)
{
randomAccessFile.close();
throw new DLException("Roll number "+rollNumber+ "does not exist!");
}
randomAccessFile.seek(0);
File tmpFile=new File("tmp.tmp");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRandomAccessFile;
tmpRandomAccessFile = new RandomAccessFile(tmpFile, "rw");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
vRollNumber=Integer.parseInt(randomAccessFile.readLine());
vName=randomAccessFile.readLine();
vGender=randomAccessFile.readLine().charAt(0);
if(vRollNumber!=rollNumber)
{
tmpRandomAccessFile.writeBytes(vRollNumber+"\n");
tmpRandomAccessFile.writeBytes(vName+"\n");
tmpRandomAccessFile.writeBytes(vGender+"\n");
}
else
{
tmpRandomAccessFile.writeBytes(studentDTOInterface.getRollNumber()+"\n");
tmpRandomAccessFile.writeBytes(studentDTOInterface.getName()+"\n");
tmpRandomAccessFile.writeBytes(studentDTOInterface.getGender()+"\n");
}
}
randomAccessFile.seek(0);
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
randomAccessFile.close();
}
catch(IOException ioException)
{
throw new DLException(ioException.getMessage());
}


}
public void deleteByRollNumber(int rollNumber) throws DLException
{
if(rollNumber<=0)
{
throw new DLException("Invalid Roll Number "+rollNumber);
}
try
{
int vRollNumber;
String vName;
char vGender;

File file= new File(DATA_FILE);
if(file.exists()==false)
{
throw new DLException("Invalid Roll Number "+rollNumber);
}
RandomAccessFile randomAccessFile;
randomAccessFile = new RandomAccessFile(file, "rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DLException("Roll number "+rollNumber+ "does not exist!");
}


boolean found = false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
vRollNumber=Integer.parseInt(randomAccessFile.readLine());
if(vRollNumber==rollNumber)
{
found = true;
break;
} // if ends
randomAccessFile.readLine();
randomAccessFile.readLine();
}
if(!found)
{
randomAccessFile.close();
throw new DLException("Roll number "+rollNumber+ "does not exist!");
}
randomAccessFile.seek(0);
File tmpFile=new File("tmp.tmp");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRandomAccessFile;
tmpRandomAccessFile = new RandomAccessFile(tmpFile, "rw");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
vRollNumber=Integer.parseInt(randomAccessFile.readLine());
vName=randomAccessFile.readLine();
vGender=randomAccessFile.readLine().charAt(0);
if(vRollNumber!=rollNumber)
{
tmpRandomAccessFile.writeBytes(vRollNumber+"\n");
tmpRandomAccessFile.writeBytes(vName+"\n");
tmpRandomAccessFile.writeBytes(vGender+"\n");
}
}
randomAccessFile.seek(0);
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(tmpRandomAccessFile.length());

tmpRandomAccessFile.setLength(0);
randomAccessFile.close();
}
catch(IOException ioException)
{
throw new DLException(ioException.getMessage());
}

}
public StudentDTOInterface getByRollNumber(int rollNumber) throws DLException
{
if(rollNumber<=0)
{
throw new DLException("Invalid Roll Number "+rollNumber);
}
try
{
File file= new File(DATA_FILE);
if(file.exists()==false)
{
throw new DLException("Invalid Roll Number "+rollNumber);
}
RandomAccessFile randomAccessFile;
randomAccessFile = new RandomAccessFile(file, "r");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DLException("Roll number "+rollNumber+ "does not exist!");
}
int vRollNumber;
String name;
char gender;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
vRollNumber=Integer.parseInt(randomAccessFile.readLine());
name=randomAccessFile.readLine();
gender=randomAccessFile.readLine().charAt(0);

if(vRollNumber==rollNumber)
{
StudentDTOInterface studentDTOInterface;
studentDTOInterface = new StudentDTO();
studentDTOInterface.setRollNumber(vRollNumber);
studentDTOInterface.setName(name);
studentDTOInterface.setGender(gender);
randomAccessFile.close();
return studentDTOInterface;


}
}
randomAccessFile.close();
throw new DLException("Roll number "+rollNumber+ "does not exist!");

} //try ends
catch(IOException ioException)
{
throw new DLException(ioException.getMessage());
}
} //getByRollNumber ends

public List<StudentDTOInterface> getAll() throws DLException
{
List<StudentDTOInterface> students;
students= new ArrayList<StudentDTOInterface> ();
try
{
File file= new File(DATA_FILE);
if(file.exists()==false) return students;
RandomAccessFile randomAccessFile;
randomAccessFile = new RandomAccessFile(file, "r");
StudentDTOInterface studentDTOInterface;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
studentDTOInterface = new StudentDTO();
studentDTOInterface.setRollNumber(Integer.parseInt(randomAccessFile.readLine()));
studentDTOInterface.setName(randomAccessFile.readLine());
studentDTOInterface.setGender(randomAccessFile.readLine().charAt(0));
students.add(studentDTOInterface);
}
randomAccessFile.close();
}
catch(IOException ioException)
{
throw new DLException(ioException.getMessage());
}
return students;
}
}

