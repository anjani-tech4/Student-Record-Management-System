import com.school.dl.exceptions.*;
import com.school.dl.dto.interfaces.*;
import com.school.dl.dao.interfaces.*;
import com.school.dl.dto.*;
import com.school.dl.dao.*;
public class StudentDAOUpdateTestCase
{
public static void main(String []args)
{
if(args.length!=3)
{
System.out.println("Pass Roll No., name and gender.");
return;
}
int rollNumber=Integer.parseInt(args[0]);
String name=args[1];
char gender=args[2].charAt(0);
StudentDTOInterface studentDTOInterface;
studentDTOInterface = new StudentDTO(rollNumber, name, gender);
try
{
StudentDAOInterface studentDAOInterface;
studentDAOInterface = new StudentDAO();
studentDAOInterface.update(studentDTOInterface);
System.out.println("Student updated!");
}
catch(DLException dlException)
{
System.out.println(dlException);
}
} //main ends
}// class ends