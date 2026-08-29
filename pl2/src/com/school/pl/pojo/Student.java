package com.school.pl.pojo;
public class Student implements Comparable<Student>
{
private int rollNumber;
private String name;
private String gender;
public Student()
{
this.rollNumber=0;
this.name="";
this.gender="";
}
public Student(int rollNumber, String name, String gender)
{
this.rollNumber=rollNumber;
this.name=name;
this.gender=gender;
}
public void setRollNumber(int rollNumber)
{
this.rollNumber=rollNumber;

}
public void setName(String name)
{
this.name=name;

}
public void setGender(String gender)
{
this.gender=gender;
}
public int getRollNumber()
{
return this.rollNumber;
}
public String getName()
{
return this.name;
}
public String getGender()
{
return this.gender;
}
public boolean equals(Object object)
{
if(!(object instanceof Student)) return false;
Student other=(Student)object;
return this.rollNumber==other.rollNumber;
} 
public int compareTo(Student other)
{
return this.rollNumber-other.rollNumber; 
}
}// class ends 