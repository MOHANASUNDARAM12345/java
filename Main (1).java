import java.util.*;
interface StudentOperations{
    void addStudent();
    void viewStudents();
    void searchById();
    void searchByName();
    void updateStudent();
    void sortByName();
    void sortByTotal();
    void sortByAverage();
    void deleteStudent();
}
abstract class Person{
    protected int id;
    protected String name;
    public Person(int id,String name){
        this.id=id;
        this.name=name;
    }
    public abstract void display();
}
class Student extends Person{
    private int m1;
    private int m2;
    private int m3;
    public Student(int id,String name,int m1,int m2,int m3){
        super(id,name);
        this.m1=m1;
        this.m2=m2;
        this.m3=m3;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getM1(){
        return m1;
    }
    public int getM2(){
        return m2;
    }
    public int getM3(){
        return m3;
    }
    public int getTotal(){
        return m1 +m2+m3;
    }
    public double getAverage(){
        return getTotal()/3.0;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setMarks(int m1,int m2,int m3){
        this.m1=m1;
        this.m2=m2;
        this.m3=m3;
    }
    @Override
    public void display(){
        System.out.println("---------------------------------------");
        System.out.println("ID      : "+id);
        System.out.println("Name    : "+name);
        System.out.println("Mark 1  : "+m1);
        System.out.println("Mark 2  : "+m2);
        System.out.println("Mark 3  : "+m3);
        System.out.println("Total   : "+getTotal());
        System.out.printf("Average:%.2f\n",getAverage());
    }
}
class StudentManager implements StudentOperations{
    Scanner sc=new Scanner(System.in);
    ArrayList<Student> students=new ArrayList<>();
    String username="admin";
    String password="1234";
    public boolean login(){
        System.out.println("===== LOGIN =====");
        System.out.print("Username : ");
        String user=sc.nextLine();
        System.out.print("Password : ");
        String pass=sc.nextLine();
        if(user.equals(username)&&pass.equals(password)){
            System.out.println("Login Successful...");
            return true;
        }
        else{
            System.out.println("Invalid Username or Password.");
            return false;
        }
    }
    @Override
    public void addStudent(){
        System.out.print("Enter Student ID : ");
        int id=sc.nextInt();
        sc.nextLine();
        for(Student s:students){
            if(s.getId()==id){
                System.out.println("Student ID already exists.");
                return;
            }
        }
        System.out.print("Enter Student Name : ");
        String name=sc.nextLine();
        if (!name.matches("[a-zA-Z]+")){
            System.out.println("Invalid Name.");
            return;
        }
        System.out.print("Enter Mark 1 : ");
        int m1=sc.nextInt();
        System.out.print("Enter Mark 2 : ");
        int m2=sc.nextInt();
        System.out.print("Enter Mark 3 : ");
        int m3=sc.nextInt();
        if(m1<0||m1>100||m2<0||m2>100||m3<0||m3>100){
            System.out.println("Marks should be between 0 and 100.");
            return;
        }
        students.add(new Student(id, name,m1,m2,m3));
        System.out.println("Student Added Successfully.");
    }
    @Override
    public void viewStudents(){
        if (students.isEmpty()){
            System.out.println("No Student Records Found.");
            return;
        }
        for(Student s:students){
            s.display();
        }
    }
    @Override
    public void searchById(){
        System.out.print("Enter Student ID : ");
        int id=sc.nextInt();
        for(Student s:students){
            if (s.getId()==id){
                s.display();
                return;
            }
        }
        System.out.println("Student Not Found.");
    }
    @Override
    public void searchByName(){
        sc.nextLine();
        System.out.print("Enter Student Name : ");
        String name=sc.nextLine();
        boolean found=false;
        for(Student s:students){
            if(s.getName().equalsIgnoreCase(name)){
                s.display();
                found=true;
            }
        }
        if(!found){
            System.out.println("Student Not Found.");
        }
    }
    @Override
    public void updateStudent(){
        System.out.print("Enter Student ID: ");
        int id=sc.nextInt();
        sc.nextLine();
        for (Student s : students){
            if (s.getId()==id){
                System.out.print("Enter New Name: ");
                String name=sc.nextLine();
                if(!name.matches("[a-zA-Z ]+")){
                    System.out.println("Invalid Name.");
                    return;
                }
                System.out.print("Enter Mark 1: ");
                int m1=sc.nextInt();
                System.out.print("Enter Mark 2: ");
                int m2=sc.nextInt();
                System.out.print("Enter Mark 3: ");
                int m3=sc.nextInt();
                if(m1<0||m1>100||m2<0||m2>100||m3<0||m3>100){
                    System.out.println("Marks should be between 0 and 100.");
                    return;
                }
                s.setName(name);
                s.setMarks(m1,m2,m3);
                System.out.println("Student Updated Successfully.");
                return;
            }
        }
        System.out.println("Student Not Found.");
    }
    @Override
    public void sortByName(){
        Collections.sort(students,Comparator.comparing(Student::getName));
        System.out.println("Sorted by Name.");
        viewStudents();
    }
    @Override
    public void sortByTotal(){
        Collections.sort(students,(a, b) -> b.getTotal() - a.getTotal());
        System.out.println("Sorted by Total.");
        viewStudents();
    }
    @Override
    public void sortByAverage(){
        Collections.sort(students,(a, b) -> Double.compare(b.getAverage(),a.getAverage()));
        System.out.println("Sorted by Average.");
        viewStudents();
    }
    @Override
    public void deleteStudent(){
        System.out.print("Enter Student ID:");
        int id=sc.nextInt();
        for(int i=0;i<students.size();i++){
            if(students.get(i).getId()==id){
                students.remove(i);
                System.out.println("Student Deleted Successfully!");
                return;
            }
        }
        System.out.println("Student Not Found?");
    }
    } 
public class Main{
    public static void main(String[] args){
        StudentManager manager=new StudentManager();
        if(!manager.login()){
            return;
        }
        Scanner sc=new Scanner(System.in);
        while(true){
            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search by ID");
            System.out.println("4. Search by Name");
            System.err.println("5. Update Student");
            System.out.println("6. Sort by Name");
            System.out.println("7. Sort by Total");
            System.out.println("8. Sort by Average");
            System.out.println("9. Delete Student");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");
            int choice=sc.nextInt();
            switch(choice){
                case 1:
                    manager.addStudent();
                    break;
                case 2:
                    manager.viewStudents();
                    break;
                case 3:
                    manager.searchById();
                    break;
                case 4:
                    manager.searchByName();
                    break;
                case 5:
                    manager.updateStudent();
                    break;
                case 6:
                    manager.sortByName();
                    break;
                case 7:
                    manager.sortByTotal();
                    break;
                case 8:
                    manager.sortByAverage();
                    break;
                case 9:
                    manager.deleteStudent();
                    break;
                case 10:
                    System.out.println("Thank You...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Choice.");
            }
        }
    }
}
