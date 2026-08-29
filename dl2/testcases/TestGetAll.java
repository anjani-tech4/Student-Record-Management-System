import com.school.dl.dao.*;
import com.school.dl.dto.interfaces.*;
import java.util.*;

public class TestGetAll
{
    public static void main(String args[]) throws Exception
    {
        StudentDAO dao = new StudentDAO();
        List<StudentDTOInterface> students = dao.getAll();

        System.out.println("Total = " + students.size());

        for(StudentDTOInterface s : students)
        {
            System.out.println(
                s.getRollNumber() + " " +
                s.getName() + " " +
                s.getGender()
            );
        }
    }
}