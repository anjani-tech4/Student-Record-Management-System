import com.school.dl.dto.interfaces.*;
import com.school.dl.dao.interfaces.*;
import com.school.dl.exceptions.*;
import com.school.dl.dto.*;
import com.school.dl.dao.*;
public class StudentDAOGetByRollNumberTestCase
{
public static void main(String []args)
{
if(args.length!=1)
{
System.out.println("Pass Roll No.");
return;
}
int rollNumber=Integer.parseInt(args[0]);
StudentDTOInterface studentDTOInterface;
try
{
StudentDAOInterface studentDAOInterface;
studentDAOInterface = new StudentDAO();
studentDTOInterface = studentDAOInterface.getByRollNumber(rollNumber);
System.out.println(studentDTOInterface.getRollNumber()+","+studentDTOInterface.getName()+","+studentDTOInterface.getGender());
}
catch(DLException dlException)
{
System.out.println(dlException);
}
} //main ends
}// class ends