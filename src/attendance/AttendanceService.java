package attendance;

import java.time.LocalDate;
import java.util.List;

public class AttendanceService {

    private AttendanceDAO attendanceDAO;

    public AttendanceService() {
        attendanceDAO = new AttendanceDAO();
    }

    public void markAttendance(int studentId, int courseId, LocalDate date, String status) {
        Attendance attendance = new Attendance(0, studentId, courseId, date, status);
        attendanceDAO.addAttendance(attendance);
    }

    public List<Attendance> getAttendanceRecords() {
        return attendanceDAO.getAllAttendance();
    }
}
