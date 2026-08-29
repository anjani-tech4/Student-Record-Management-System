import com.school.dl.exceptions.*;
import com.school.dl.dto.interfaces.*;
import com.school.dl.dao.interfaces.*;
import com.school.dl.dto.*;
import com.school.dl.dao.*;
public class StudentDAOAddTestCase
{
public static void main(String args[])
{
if(args.length!=2)
{
System.out.println("Pass name, gender");
return;
}
int rollNumber=0;
String name=args[0];
char gender=args[1].charAt(0);
StudentDTOInterface studentDTOInterface;
studentDTOInterface= new StudentDTO(rollNumber, name, gender);
try
{
StudentDAOInterface studentDAOInterface;
studentDAOInterface = new StudentDAO();
studentDAOInterface.add(studentDTOInterface);
System.out.println("Student added... with roll number "+studentDTOInterface.getRollNumber());
}
catch(DLException dlException)
{
System.out.println(dlException.getMessage());
}
}
}