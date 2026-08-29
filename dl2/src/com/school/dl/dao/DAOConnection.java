package com.school.dl.dao;
import java.sql.*;
public class DAOConnection
{
private DAOConnection(){}
public static Connection getConnection()
{
try
{
Class.forName("com.mysql.cj.jdbc.Driver");
return DriverManager.getConnection("jdbc:mysql://localhost:3306/java_examples_db_1","java_examples","java");
}
catch(ClassNotFoundException cnfe)
{
System.out.println("Driver class not found"+ cnfe.getMessage());
System.exit(1);
}
catch(SQLException sqlException)
{
System.out.println(sqlException.getMessage());
System.exit(1);
}
return null;
}
}