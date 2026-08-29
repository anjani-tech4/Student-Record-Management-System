package com.school.dl.dao.interfaces;
import com.school.dl.exceptions.*;
import com.school.dl.dto.interfaces.*;
import java.util.*;
public interface StudentDAOInterface
{
public void add(StudentDTOInterface studentDTOInterface) throws DLException;
public void update(StudentDTOInterface studentDTOInterface) throws DLException;
public void deleteByRollNumber(int rollNumber) throws DLException;
public StudentDTOInterface getByRollNumber(int rollNumber) throws DLException;
public List<StudentDTOInterface> getAll() throws DLException;
}