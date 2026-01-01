package student;

/**
 * Entity class representing a student.
 * Contains student details like name, roll number, and email.
 */
public class Student {
    private int studentId;
    private String name;
    private String rollNo;
    private String email;

    public Student() {
    }

    public Student(int studentId, String name, String rollNo, String email) {
        this.studentId = studentId;
        this.name = name;
        this.rollNo = rollNo;
        this.email = email;
    }

    /**
     * Parameterized constructor for creating a new student (without ID).
     *
     * @param name   Student's full name.
     * @param rollNo Student's roll number.
     * @param email  Student's email address.
     */
    public Student(String name, String rollNo, String email) {
        this.name = name;
        this.rollNo = rollNo;
        this.email = email;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return name + " (" + rollNo + ")";
    }
}
