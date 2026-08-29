import com.school.dl.exceptions.*;
import com.school.dl.dto.interfaces.*;
import com.school.dl.dao.interfaces.*;
import com.school.dl.dto.*;
import com.school.dl.dao.*;
public class StudentDAODeleteByRollNumberTestCase
{
public static void main(String []args)
{
if(args.length!=1)
{
System.out.println("Pass Roll No.");
return;
}
int rollNumber=Integer.parseInt(args[0]);
try
{
StudentDAOInterface studentDAOInterface;
studentDAOInterface = new StudentDAO();
studentDAOInterface.deleteByRollNumber(rollNumber);
System.out.println("Student deleted!");
}
catch(DLException dlException)
{
System.out.println(dlException);
}
} //main ends
}// class ends