package enrollment;

import course.Course;
import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Enrollment operations.
 * Handles student-course enrollment management in the database.
 */
public class EnrollmentDAO {

    /**
     * Enrolls a student in a course.
     *
     * @param studentId The ID of the student.
     * @param courseId  The ID of the course.
     * @return true if successful, false otherwise.
     */
    public boolean enrollStudent(int studentId, int courseId) {
        String sql = "INSERT INTO student_course (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes a student's enrollment from a course.
     *
     * @param studentId The ID of the student.
     * @param courseId  The ID of the course.
     * @return true if successful, false otherwise.
     */
    public boolean removeEnrollment(int studentId, int courseId) {
        String sql = "DELETE FROM student_course WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all courses a specific student is enrolled in.
     *
     * @param studentId The ID of the student.
     * @return A list of Course objects.
     */
    public List<Course> getCoursesByStudent(int studentId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.course_id, c.course_name, c.credits FROM course c " +
                "JOIN student_course sc ON c.course_id = sc.course_id " +
                "WHERE sc.student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getInt("credits")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}
