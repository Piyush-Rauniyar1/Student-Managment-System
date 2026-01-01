package course;

import java.util.List;

public interface CourseDAO {
    boolean addCourse(Course course);
    boolean updateCourse(Course course);
    boolean deleteCourse(int courseId);
    List<Course> getAllCourses();
}
