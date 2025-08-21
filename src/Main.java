import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GradeTracker gradeTracker = new GradeTracker();

        while (true) {
            System.out.println("\n Welcome to the grade tracker");
            System.out.println("1. Add Student\n2. Show Report\n3. Exit\nChoose: ");
            int choiceOne = input.nextInt();
            input.nextLine();

            switch (choiceOne) {
                case 1:
                    System.out.print("Enter student name: ");
                    String name = input.nextLine();
                    System.out.print("Enter grade: ");
                    int grade = input.nextInt();
                    gradeTracker.addStudent(0, name, grade);
                    System.out.println("Success");
                    break;
                case 2:
                    gradeTracker.display();
                    break;
                case 3:
                    System.out.println("Exiting program...");
                    input.close();
                    return;
                default:
                    System.out.println("Invalid input, try again.");
            }
        }
    }
}