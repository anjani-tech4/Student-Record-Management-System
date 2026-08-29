import com.school.dl.exceptions.*;
import com.school.dl.dto.interfaces.*;
import com.school.dl.dao.interfaces.*;
import com.school.dl.dto.*;
import com.school.dl.dao.*;
import java.util.*;
class StudentDAOGetAllTestCase
{
public static void main(String []gg)
{
try
{
StudentDAOInterface studentDAOInterface;
studentDAOInterface = new StudentDAO();
List<StudentDTOInterface> students;
students= studentDAOInterface.getAll();
for(StudentDTOInterface s: students)
{
System.out.println(s.getRollNumber()+","+s.getName()+","+s.getGender());
}
}
catch(DLException dlException)
{

System.out.println(dlException.getMessage());
}
}
}
