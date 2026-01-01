package student;

import database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student operations.
 * Handles CRUD operations for students in the database.
 */
public class StudentDAO {

    /**
     * Adds a new student to the database.
     *
     * @param student The Student object to add.
     * @return true if successful, false otherwise.
     */
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO student (name, roll_no, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getRollNo());
            pstmt.setString(3, student.getEmail());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStudent(Student student) {
        String sql = "UPDATE student SET name = ?, roll_no = ?, email = ? WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getRollNo());
            pstmt.setString(3, student.getEmail());
            pstmt.setInt(4, student.getStudentId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM student WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM student WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("roll_no"),
                        rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all students from the database.
     *
     * @return A list of Student objects.
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("roll_no"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
}
