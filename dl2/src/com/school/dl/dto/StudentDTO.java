package com.school.dl.dto;
import com.school.dl.dto.interfaces.*;
public class StudentDTO implements StudentDTOInterface
{
private int rollNumber;
private String name;
private char gender;
public StudentDTO()
{
this.rollNumber=0;
this.name="";
this.gender=(char)0;
}
public StudentDTO(int rollNumber, String name, char gender)
{
this.rollNumber=rollNumber;
this.name=name;
this.gender=gender;

}
public void setRollNumber(int rollNumber)
{
this.rollNumber=rollNumber;
}
public int getRollNumber()
{
return this.rollNumber;
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
public void setGender(char gender)
{
this.gender=gender;
}
public char getGender()
{
return this.gender;
}
public boolean equals(Object object)
{
//determine if object contains reference of StudentDTOInterface
if(!(object instanceof StudentDTOInterface))
{
return false;
}
StudentDTOInterface other;
other=(StudentDTOInterface)object;
return this.rollNumber==other.getRollNumber();
}
public String toString()
{
return "Roll number "+this.rollNumber+"Name "+this.name+"Gender"+this.gender;
}
}