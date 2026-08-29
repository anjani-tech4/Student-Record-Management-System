import com.school.application.pl.ui.*;
import com.tm.io.*;
class StudentUITestCase
{
public static void main(String []gg)
{
int choice;
StudentUI studentUI= new StudentUI();
while(true)
{
stdout.clearTerminal();
System.out.println("1. Add Student");
System.out.println("2. Display List of Students");
System.out.println("3. Sort by roll number");
System.out.println("4. Sort by name");
System.out.println("5. Sort by gender");
System.out.println("6. Delete Student");
System.out.println("7. Update Student");

System.out.println("8. Exit");
choice = stdin.getInt("Enter your choice");
if(choice==1) studentUI.add();
else if(choice==2) studentUI.displayList();
else if(choice==3) studentUI.sortByRollNumber();
else if(choice==4) studentUI.sortByName();
else if(choice==5) studentUI.sortByGender();
else if(choice==6) studentUI.delete();
else if(choice==7) studentUI.update();

else if(choice==8) break;
else System.out.println("Invalid");

}
}
}