package enrollment;

import java.util.List;
import course.Course;

public interface EnrollmentDAO {
    boolean enrollStudent(int studentId, int courseId);
    boolean removeEnrollment(int studentId, int courseId);
    List<Course> getCoursesByStudent(int studentId);
}