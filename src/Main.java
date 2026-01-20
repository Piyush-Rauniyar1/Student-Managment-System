import database.DatabaseConnection;
import auth.LoginFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Main application entry point
 * University Student Management System
 */
/**
 * Main application entry point for the University Student Management System.
 * Initializes the Look and Feel, checks database connection, and launches the
 * application.
 */
public class Main {

    /**
     * The main method that starts the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Set Look and Feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Test database connection
        System.out.println("========================================");
        System.out.println("University SMS - Starting Application");
        System.out.println("========================================");

        if (DatabaseConnection.testConnection()) {
            System.out.println("✓ Database connection successful!");

            // Start the application
            SwingUtilities.invokeLater(() -> {
                showSplashScreen();
            });
        } else {
            System.err.println("✗ Database connection failed!");
            System.err.println("Please check your database configuration.");

            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null,
                        "Failed to connect to database.\nPlease check your configuration.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            });
        }
    }

    /*
     * =============================================================================
     * =========
     * MODULE 1: Authentication & User Management
     * Branch: module-auth
     * Package: auth/
     *
     * Responsibilities:
     * - Credential validation
     * - Password management
     * - User session initiation
     *
     * Classes:
     * - User (Attributes: userId, username, password)
     * - UserDAO (Methods: findByUsername, saveUser, updatePassword, deleteUser)
     * - AuthService (Methods: authenticate, changePassword)
     * =============================================================================
     * =========
     */
    // [Module 1 Start: Application Entry & Auth Handover]
    /**
     * Show splash screen before login
     */
    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        splash.setSize(400, 300);
        splash.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(54, 126, 226));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel logo = new JLabel("🎓");
        logo.setFont(new Font("Arial", Font.PLAIN, 80));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(logo);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel title = new JLabel("University SMS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel subtitle = new JLabel("Student Management System");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(new Color(220, 230, 250));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(subtitle);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setMaximumSize(new Dimension(300, 20));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(progressBar);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel loading = new JLabel("Loading...");
        loading.setFont(new Font("Arial", Font.PLAIN, 12));
        loading.setForeground(Color.WHITE);
        loading.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(loading);

        splash.add(panel);
        splash.setVisible(true);

        // Close splash and open login after 2 seconds
        Timer timer = new Timer(2000, e -> {
            splash.dispose();
            new LoginFrame();
        });
        timer.setRepeats(false);
        timer.start();
    }
    // [Module 1 Ends Here]

    /*
     * =============================================================================
     * =========
     * PROJECT MODULE DIRECTORY & TEAM DIVERSION
     * =============================================================================
     * =========
     *
     * MODULE 2: Student & Course Management
     * Branch: module-student-course
     * Package: student/, course/
     *
     * Responsibilities:
     * - Student CRUD
     * - Course CRUD
     * - Student-Course mapping
     *
     * Components:
     * - Student (Attributes: studentId, name, rollNo, email)
     * - StudentDAO (Methods: addStudent, updateStudent, deleteStudent,
     * getStudentById, getAllStudents)
     * - Course (Attributes: courseId, courseName, credits)
     * - CourseDAO (Methods: addCourse, updateCourse, deleteCourse, getAllCourses)
     * - EnrollmentDAO (Methods: enrollStudent, removeEnrollment,
     * getCoursesByStudent)
     *
     * -----------------------------------------------------------------------------
     * ---------
     *
     * MODULE 3: Attendance Management
     * Branch: module-attendance
     * Package: attendance/
     *
     * Responsibilities:
     * - Prevent duplicate attendance
     * - Attendance calculation
     * - Course-wise & student-wise records
     *
     * Components:
     * - Attendance (Attributes: attendanceId, studentId, courseId, date, status)
     * - AttendanceDAO (Methods: markAttendance, attendanceExists,
     * getAttendanceByStudent...)
     * - AttendanceService (Methods: markPresent, markAbsent)
     *
     * -----------------------------------------------------------------------------
     * ---------
     *
     * MODULE 4: Marks & Grade Management
     * Branch: module-marks-grades
     * Package: marks/
     *
     * Responsibilities:
     * - Store marks
     * - Auto grade calculation
     * - Update & fetch results
     *
     * Components:
     * - Marks (Attributes: marksId, studentId, courseId, marks, grade)
     * - MarksDAO (Methods: addMarks, updateMarks, getMarks, getMarksByStudent)
     * - GradeService (Methods: calculateGrade, assignGrade)
     *
     * -----------------------------------------------------------------------------
     * ---------
     *
     * MODULE 5: Reports & System Integration
     * Branch: module-reports
     * Package: reports/
     *
     * Responsibilities:
     * - Student Reports
     * - Course Attendance Reports
     * - Defaulter Lists
     *
     * Components:
     * - StudentReport (Attributes: studentId, courses, marks, attendancePercentage)
     * - ReportDAO (Methods: generateStudentReport, generateCourseAttendanceReport,
     * getDefaulterList)
     * =============================================================================
     * =========
     */
}