//package student;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class StudentDAOImpl implements StudentDAO {
//
//    @Override
//    public boolean addStudent(Student student) {
//        String query = "INSERT INTO students (name, rollNo, email) VALUES (?, ?, ?)";
//        DriverManager DatabaseConnection = null;
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//
//            ps.setString(1, student.getName());
//            ps.setString(2, student.getRollNo());
//            ps.setString(3, student.getEmail());
//
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public boolean updateStudent(Student student) {
//        String query = "UPDATE students SET name = ?, rollNo = ?, email = ? WHERE studentId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//
//            ps.setString(1, student.getName());
//            ps.setString(2, student.getRollNo());
//            ps.setString(3, student.getEmail());
//            ps.setInt(4, student.getStudentId());
//
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public boolean deleteStudent(int studentId) {
//        String query = "DELETE FROM students WHERE studentId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//
//            ps.setInt(1, studentId);
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    public Student getStudentById(int studentId) {
//        String query = "SELECT * FROM students WHERE studentId = ?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//
//            ps.setInt(1, studentId);
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                return new Student(
//                        rs.getInt("studentId"),
//                        rs.getString("name"),
//                        rs.getString("rollNo"),
//                        rs.getString("email")
//                );
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public List<Student> getAllStudents() {
//        List<Student> students = new ArrayList<>();
//        String query = "SELECT * FROM students";
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//
//            while (rs.next()) {
//                students.add(new Student(
//                        rs.getInt("studentId"),
//                        rs.getString("name"),
//                        rs.getString("rollNo"),
//                        rs.getString("email")
//                ));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return students;
//    }
//}