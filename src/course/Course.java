package course;

/**
 * Entity class representing a course.
 * Contains course details such as name and credits.
 */
public class Course {
    private int courseId;
    private String courseName;
    private int credits;

    /**
     * Default constructor.
     */
    public Course() {
    }

    /**
     * Parameterized constructor for creating a new course with an ID.
     *
     * @param courseId   The unique identifier for the course.
     * @param courseName The name of the course.
     * @param credits    The number of credits for the course.
     */
    public Course(int courseId, String courseName, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
    }

    /**
     * Parameterized constructor for creating a new course (without ID).
     *
     * @param courseName The name of the course.
     * @param credits    The number of credits for the course.
     */
    public Course(String courseName, int credits) {
        this.courseName = courseName;
        this.credits = credits;
    }

    /**
     * Retrieves the course ID.
     *
     * @return The course ID.
     */
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return courseName + " (" + credits + " Credits)";
    }
}
