package course;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Course operations.
 * Handles CRUD operations for courses in the database.
 */
public class CourseDAO {

    /**
     * Adds a new course to the database.
     *
     * @param course The Course object to add.
     * @return true if successful, false otherwise.
     */
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO course (course_name, credits) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseName());
            pstmt.setInt(2, course.getCredits());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates an existing course in the database.
     *
     * @param course The Course object with updated details.
     * @return true if successful, false otherwise.
     */
    public boolean updateCourse(Course course) {
        String sql = "UPDATE course SET course_name = ?, credits = ? WHERE course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, course.getCourseName());
            pstmt.setInt(2, course.getCredits());
            pstmt.setInt(3, course.getCourseId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a course from the database by its ID.
     *
     * @param courseId The ID of the course to delete.
     * @return true if successful, false otherwise.
     */
    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM course WHERE course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all courses from the database.
     *
     * @return A list of Course objects.
     */
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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
