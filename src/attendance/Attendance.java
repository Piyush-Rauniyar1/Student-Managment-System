package attendance;

import java.time.LocalDate;

public class Attendance {

    private int attendanceId;
    private int studentId;
    private int courseId;
    private LocalDate date;
    private String status; // "PRESENT" or "ABSENT"

    public Attendance() {
    }

    public Attendance(int studentId, int courseId, LocalDate date, String status) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.date = date;
        this.status = status;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(int attendanceId) {
        this.attendanceId = attendanceId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
