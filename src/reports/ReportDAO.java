package reports;

import java.util.List;
import java.util.Map;
import course.Course;
import student.Student;

public interface ReportDAO {

    StudentReport generateStudentReport(int studentId);

    Map<Course, Double> generateCourseAttendanceReport(int courseId);

    List<Student> getDefaulterList(double minAttendance);
}
