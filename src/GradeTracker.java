import java.util.ArrayList;
import java.util.Collections;

public class GradeTracker {
    private ArrayList<Student> students;

    public GradeTracker() {
        students = new ArrayList<>();
    }

    public void addStudent(String name, int grade) {
        students.add(new Student(name, grade));
    }

    public int averageGrade() {
        int average = 0;
        for (Student student : students) {
            average += student.getGrade();
        }
        return !students.isEmpty() ? average / students.size() : 0;
    }

    public int highestGrade() {
        if (students.isEmpty()) return 0;
        return Collections.max(students, (x, y) -> x.getGrade() - y.getGrade()).getGrade();
    }

    public int lowestGrade() {
        if (students.isEmpty()) return 0;
        return Collections.min(students, (x, y) -> x.getGrade() - y.getGrade()).getGrade();
    }


    public void display() {
        for (Student student : students) {
            System.out.println("Name: " + student.getName() + ", Grade: " + student.getGrade());
        }
        System.out.println();
        System.out.println("Average: " + averageGrade());
        System.out.println("Highest Grade: " + highestGrade());
        System.out.println("Lowest Grade: " + lowestGrade());
    }
}
