import attendance.AttendanceService;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        AttendanceService service = new AttendanceService();

        service.markAttendance(
                1,          // studentId
                101,        // courseId
                LocalDate.now(),
                "Present"
        );

        System.out.println("Program ran successfully!");
    }
}
