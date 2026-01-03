package attendance;

import java.time.LocalDate;

public class Attendance {

    private int attendanceId;
    private int studentId;
    private int courseId;
    private LocalDate date;
    private String status;

    public Attendance(int attendanceId, int studentId, int courseId, LocalDate date, String status) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.date = date;
        this.status = status;
    }

    public int getAttendanceId() {
        return attendanceId;
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

    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId=" + attendanceId +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}
