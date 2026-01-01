package enrollment;

import auth.LoginFrame;
import student.StudentPanel;
import course.CoursePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Main dashboard frame for the application.
 * Manages the sidebar navigation and displays different panels (Student,
 * Course, Enrollment).
 */
public class DashboardFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public DashboardFrame() {
        setTitle("UniAdmin Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Main Content Area
        JPanel mainArea = new JPanel(new BorderLayout());
        add(mainArea, BorderLayout.CENTER);

        // Header
        JPanel header = createHeader();
        mainArea.add(header, BorderLayout.NORTH);

        // Content Panel (CardLayout)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Style.BACKGROUND_LIGHT);
        mainArea.add(contentPanel, BorderLayout.CENTER);

        // Add Panels
        StudentPanel studentPanel = new StudentPanel();
        studentPanel.setEnrollmentListener(e -> cardLayout.show(contentPanel, "ENROLLMENT"));
        contentPanel.add(studentPanel, "STUDENT");
        contentPanel.add(new CoursePanel(), "COURSE");
        contentPanel.add(new EnrollmentPanel(), "ENROLLMENT");
        contentPanel.add(new attendance.AttendancePanel(), "ATTENDANCE");
        contentPanel.add(new marks.MarksPanel(), "MARKS");
        contentPanel.add(new reports.StudentReportPanel(), "REPORT_STUDENT");
        contentPanel.add(new reports.AttendanceReportPanel(), "REPORT_ATTENDANCE");
        contentPanel.add(new reports.DefaulterListPanel(), "REPORT_DEFAULTER");

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(Style.SURFACE_LIGHT);
        sidebar.setPreferredSize(new Dimension(Style.SIDEBAR_WIDTH, getHeight()));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Style.BORDER_LIGHT));

        // Logo
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        logoPanel.setBackground(Style.SURFACE_LIGHT);
        logoPanel.setMaximumSize(new Dimension(Style.SIDEBAR_WIDTH, 80));
        JLabel logoLabel = new JLabel("UniAdmin");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        logoLabel.setForeground(Style.TEXT_MAIN);
        logoPanel.add(logoLabel);
        sidebar.add(logoPanel);

        // Navigation
        sidebar.add(createNavItem("Student Management", e -> cardLayout.show(contentPanel, "STUDENT")));
        sidebar.add(createNavItem("Course Management", e -> cardLayout.show(contentPanel, "COURSE")));
        sidebar.add(createNavItem("Enrollment", e -> cardLayout.show(contentPanel, "ENROLLMENT")));
        sidebar.add(createNavItem("Attendance", e -> cardLayout.show(contentPanel, "ATTENDANCE")));

        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel acadLabel = new JLabel("  ACADEMIC");
        acadLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        acadLabel.setForeground(Style.TEXT_SECONDARY);
        acadLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(acadLabel);

        sidebar.add(createNavItem("Marks Entry", e -> cardLayout.show(contentPanel, "MARKS")));

        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        JLabel repLabel = new JLabel("  REPORTS");
        repLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        repLabel.setForeground(Style.TEXT_SECONDARY);
        repLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(repLabel);

        sidebar.add(createNavItem("Student Report", e -> cardLayout.show(contentPanel, "REPORT_STUDENT")));
        sidebar.add(createNavItem("Attendance Stats", e -> cardLayout.show(contentPanel, "REPORT_ATTENDANCE")));
        sidebar.add(createNavItem("Defaulter List", e -> cardLayout.show(contentPanel, "REPORT_DEFAULTER")));

        sidebar.add(Box.createVerticalGlue()); // Push User Profile to bottom

        // User Profile (Stub)
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        profilePanel.setBackground(Style.SURFACE_LIGHT);
        profilePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Style.BORDER_LIGHT));
        profilePanel.setMaximumSize(new Dimension(Style.SIDEBAR_WIDTH, 80));
        JLabel userLabel = new JLabel("Admin User");
        userLabel.setFont(Style.FONT_BODY_BOLD);
        userLabel.setForeground(Style.TEXT_MAIN);
        profilePanel.add(userLabel);
        sidebar.add(profilePanel);

        return sidebar;
    }

    private JButton createNavItem(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(Style.FONT_BODY);
        btn.setForeground(Style.TEXT_SECONDARY);
        btn.setBackground(Style.SURFACE_LIGHT);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Style.SIDEBAR_WIDTH, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        return btn;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Style.SURFACE_LIGHT);
        header.setPreferredSize(new Dimension(getWidth(), Style.HEADER_HEIGHT));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Style.BORDER_LIGHT));

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(Style.FONT_HEADER);
        titleLabel.setForeground(Style.TEXT_MAIN);
        titleLabel.setBorder(new EmptyBorder(0, 20, 0, 0));
        header.add(titleLabel, BorderLayout.WEST);

        return header;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame();
        });
    }
}
