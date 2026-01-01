package reports;

import course.Course;
import marks.Marks;
import java.util.List;
import java.util.Map;

public class StudentReport {
    private int studentId;
    private String studentName;
    private String rollNo;
    private String department; // Placeholder if not in DB
    private String semester; // Placeholder if not in DB
    private List<Course> courses;
    private Map<Integer, Marks> marksMap; // CourseID -> Marks
    private Map<Integer, Double> attendanceMap; // CourseID -> Percentage

    public StudentReport() {
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public Map<Integer, Marks> getMarksMap() {
        return marksMap;
    }

    public void setMarksMap(Map<Integer, Marks> marksMap) {
        this.marksMap = marksMap;
    }

    public Map<Integer, Double> getAttendanceMap() {
        return attendanceMap;
    }

    public void setAttendanceMap(Map<Integer, Double> attendanceMap) {
        this.attendanceMap = attendanceMap;
    }
}
