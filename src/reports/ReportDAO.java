package reports;

import database.DatabaseConnection;
import course.Course;
import marks.Marks;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportDAO {

    public StudentReport generateStudentReport(int studentId) {
        StudentReport report = new StudentReport();
        report.setStudentId(studentId);

        // Fetch Basic Student Info
        String studentSql = "SELECT * FROM student WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(studentSql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                report.setStudentName(rs.getString("name"));
                report.setRollNo(rs.getString("roll_no"));
                // Default placeholders as these columns might not exist yet
                report.setDepartment("Computer Science");
                report.setSemester("Sem 5");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        // Fetch Enrolled Courses
        List<Course> courses = new ArrayList<>();
        String courseSql = "SELECT c.* FROM course c JOIN student_course sc ON c.course_id = sc.course_id WHERE sc.student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(courseSql)) {

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
        report.setCourses(courses);

        // Fetch Marks
        Map<Integer, Marks> marksMap = new HashMap<>();
        String marksSql = "SELECT * FROM marks WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(marksSql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Marks m = new Marks(
                        rs.getInt("marks_id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getDouble("marks"),
                        rs.getString("grade"));
                marksMap.put(m.getCourseId(), m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        report.setMarksMap(marksMap);

        // Calculate Attendance Percentage per Course
        Map<Integer, Double> attendanceMap = new HashMap<>();
        for (Course c : courses) {
            attendanceMap.put(c.getCourseId(), calculateAttendancePercentage(studentId, c.getCourseId()));
        }
        report.setAttendanceMap(attendanceMap);

        return report;
    }

    public Map<String, Object> generateCourseAttendanceReport(int courseId) {
        Map<String, Object> stats = new HashMap<>();

        // Total Enrolled
        String countSql = "SELECT COUNT(*) FROM student_course WHERE course_id = ?";
        int totalStudents = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(countSql)) {
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                totalStudents = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stats.put("totalStudents", totalStudents);

        // Average Attendance
        // (Sum of present days for all students) / (Total days * Total students)
        // For simplicity: Average of each student's percentage
        if (totalStudents > 0) {
            // Get all students enrolled
            List<Integer> studentIds = new ArrayList<>();
            String enrollSql = "SELECT student_id FROM student_course WHERE course_id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(enrollSql)) {
                pstmt.setInt(1, courseId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next())
                    studentIds.add(rs.getInt("student_id"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            double totalPercentage = 0;
            int studentsWithAttendance = 0;

            for (int sid : studentIds) {
                double pct = calculateAttendancePercentage(sid, courseId);
                totalPercentage += pct;
                studentsWithAttendance++;

                // Also need student details for the list
                // Fetch student name - skipping for optimization, assuming caller handles or
                // separate join query better
            }
            stats.put("avgAttendance", studentsWithAttendance > 0 ? totalPercentage / studentsWithAttendance : 0.0);
        } else {
            stats.put("avgAttendance", 0.0);
        }

        return stats;
    }

    // Helper Class for Defaulter/Attendance List
    public static class StudentAttendanceStat {
        public int studentId;
        public String studentName;
        public String rollNo;
        public int totalLectures;
        public int presentCount;
        public double percentage;

        public StudentAttendanceStat(int studentId, String studentName, String rollNo, int totalLectures,
                                     int presentCount, double percentage) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.rollNo = rollNo;
            this.totalLectures = totalLectures;
            this.presentCount = presentCount;
            this.percentage = percentage;
        }
    }

    public List<StudentAttendanceStat> getCourseAttendanceList(int courseId) {
        List<StudentAttendanceStat> list = new ArrayList<>();

        // Join for student details
        String sql = "SELECT s.student_id, s.name, s.roll_no FROM student s " +
                "JOIN student_course sc ON s.student_id = sc.student_id " +
                "WHERE sc.course_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int sid = rs.getInt("student_id");
                String name = rs.getString("name");
                String roll = rs.getString("roll_no");

                // Calc stats
                int[] counts = getAttendanceCounts(sid, courseId);
                int total = counts[0];
                int present = counts[1];
                double pct = total == 0 ? 0 : (double) present / total * 100;

                list.add(new StudentAttendanceStat(sid, name, roll, total, present, pct));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<StudentAttendanceStat> getDefaulterList(double minThreshold) {
        List<StudentAttendanceStat> defaulters = new ArrayList<>();

        // This is a heavy operation scanning all enrollments.
        // Optimized query:
        // iterate all courses or enrollments. For this demo, let's grab all students
        // and their courses

        String sql = "SELECT s.student_id, s.name, s.roll_no, c.course_id, c.course_name " +
                "FROM student s " +
                "JOIN student_course sc ON s.student_id = sc.student_id " +
                "JOIN course c ON sc.course_id = c.course_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int sid = rs.getInt("student_id");
                int cid = rs.getInt("course_id");
                String name = rs.getString("name");
                String roll = rs.getString("roll_no");
                // String cName = rs.getString("course_name"); // Unused in current VO

                int[] counts = getAttendanceCounts(sid, cid);
                int total = counts[0];
                int present = counts[1];
                double pct = total == 0 ? 100.0 : (double) present / total * 100;

                if (total == 0)
                    pct = 0.0;

                if (pct < minThreshold && total > 0) {
                    defaulters.add(new StudentAttendanceStat(sid, name, roll, total, present, pct));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return defaulters;
    }

    // Better implementation for Defaulter List passing specific course if needed,
    // or all.
    // Let's implement getDefaultersForCourse(courseId, minThreshold) first as it's
    // more common.
    // The UI shows generic "Defaulter List" with filters.

    public List<StudentAttendanceStat> getDefaultersByCourse(int courseId, double minThreshold) {
        List<StudentAttendanceStat> all = getCourseAttendanceList(courseId);
        List<StudentAttendanceStat> defaulters = new ArrayList<>();
        for (StudentAttendanceStat stat : all) {
            if (stat.percentage < minThreshold) {
                defaulters.add(stat);
            }
        }
        return defaulters;
    }

    private double calculateAttendancePercentage(int studentId, int courseId) {
        int[] counts = getAttendanceCounts(studentId, courseId);
        int total = counts[0];
        int present = counts[1];
        if (total == 0)
            return 0.0;
        return (double) present / total * 100;
    }

    private int[] getAttendanceCounts(int studentId, int courseId) {
        // [Total, Present]
        int total = 0;
        int present = 0;

        String sql = "SELECT status, COUNT(*) as cnt FROM attendance WHERE student_id = ? AND course_id = ? GROUP BY status";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String status = rs.getString("status");
                int count = rs.getInt("cnt");
                total += count;
                if ("Present".equalsIgnoreCase(status)) {
                    present += count;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new int[] { total, present };
    }
}
