package attendance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class AttendanceDAO {

    private Connection connection;

    public AttendanceDAO(Connection connection) {
        this.connection = connection;
    }

    // Mark attendance
    public boolean markAttendance(Attendance attendance) {

        if (attendanceExists(
                attendance.getStudentId(),
                attendance.getCourseId(),
                attendance.getDate())) {
            return false;
        }

        String sql = "INSERT INTO attendance(student_id, course_id, date, status) VALUES(?,?,?,?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, attendance.getStudentId());
            ps.setInt(2, attendance.getCourseId());
            ps.setDate(3, java.sql.Date.valueOf(attendance.getDate()));
            ps.setString(4, attendance.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Prevent duplicate attendance
    public boolean attendanceExists(int studentId, int courseId, LocalDate date) {

        String sql = "SELECT 1 FROM attendance WHERE student_id=? AND course_id=? AND date=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setDate(3, java.sql.Date.valueOf(date));

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Attendance percentage
    public double calculateAttendancePercentage(int studentId, int courseId) {

        String sql = """
                SELECT COUNT(*) AS total,
                SUM(CASE WHEN status='PRESENT' THEN 1 ELSE 0 END) AS present
                FROM attendance
                WHERE student_id=? AND course_id=?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int total = rs.getInt("total");
                int present = rs.getInt("present");
                return total == 0 ? 0 : (present * 100.0) / total;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
