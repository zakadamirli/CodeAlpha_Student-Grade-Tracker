import java.io.Serial;
import java.io.Serializable;

class Student implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int id;
    private String name;
    private int grade;

    public Student(int id, String name, int grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getLetterGrade() {
        if (grade >= 97) return "A+";
        else if (grade >= 93) return "A";
        else if (grade >= 90) return "A-";
        else if (grade >= 87) return "B+";
        else if (grade >= 83) return "B";
        else if (grade >= 80) return "B-";
        else if (grade >= 77) return "C+";
        else if (grade >= 73) return "C";
        else if (grade >= 70) return "C-";
        else if (grade >= 67) return "D+";
        else if (grade >= 60) return "D";
        else return "F";
    }

    public String getStatus() {
        return grade >= 60 ? "Pass" : "Fail";
    }

    @Override
    public String toString() {
        return String.format("Student{id=%d, name='%s', grade=%d, letterGrade='%s'}",
                id, name, grade, getLetterGrade());
    }
}
