package student;

import java.util.List;

public interface StudentDAO {
    boolean addStudent(Student student);
    boolean updateStudent(Student student);
    boolean deleteStudent(int studentId);
    Student getStudentById(int studentId);
    List<Student> getAllStudents();
}
