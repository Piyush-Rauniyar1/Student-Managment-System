
package enrollment;

import course.Course;
import course.CourseDAO;

import student.Student;
import student.StudentDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for managing enrollments.
 * Allows enrolling students in courses and removing enrollments.
 */
public class EnrollmentPanel extends JPanel {

    private JComboBox<Student> studentCombo;
    private JComboBox<Course> courseCombo;
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;

    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private EnrollmentDAO enrollmentDAO;

    public EnrollmentPanel() {
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();
        enrollmentDAO = new EnrollmentDAO();

        setLayout(new BorderLayout(20, 20));
        setBackground(Style.BACKGROUND_LIGHT);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top: Selection & Actions
        add(createSelectionPanel(), BorderLayout.NORTH);

        // Center: Enrollment Table for Selected Student
        add(createTablePanel(), BorderLayout.CENTER);

        refreshData();
    }

    private JPanel createSelectionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Style.SURFACE_LIGHT);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER_LIGHT),
                new EmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("COURSE ENROLLMENT");
        title.setFont(Style.FONT_SUBHEADER);
        title.setForeground(Style.TEXT_MAIN);
        panel.add(title, gbc);

        // Student Selection
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(createLabel("Select Student"), gbc);

        studentCombo = new JComboBox<>();
        styleComboBox(studentCombo);
        studentCombo.addActionListener(e -> loadEnrollmentsForSelectedStudent());
        gbc.gridx = 1;
        panel.add(studentCombo, gbc);

        // Course Selection
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(createLabel("Select Course"), gbc);

        courseCombo = new JComboBox<>();
        styleComboBox(courseCombo);
        gbc.gridx = 1;
        panel.add(courseCombo, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Style.SURFACE_LIGHT);

        JButton enrollButton = createButton("Enroll Student", Style.PRIMARY, Color.WHITE);
        enrollButton.addActionListener(e -> enroll());

        JButton removeButton = createButton("Unenroll", new Color(254, 226, 226), new Color(220, 38, 38));
        removeButton.addActionListener(e -> unenroll());

        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setForeground(Style.TEXT_SECONDARY);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorderPainted(false);
        refreshButton.addActionListener(e -> refreshData());

        buttonPanel.add(enrollButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Style.FONT_BODY_BOLD);
        label.setForeground(Style.TEXT_SECONDARY);
        return label;
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setFont(Style.FONT_BODY);
        combo.setBackground(Style.SURFACE_LIGHT);
        ((JComponent) combo.getRenderer()).setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Style.SURFACE_LIGHT);
        panel.setBorder(BorderFactory.createLineBorder(Style.BORDER_LIGHT));

        // Header for table section
        JLabel tableHeader = new JLabel("Current Enrollments for Selected Student");
        tableHeader.setFont(Style.FONT_BODY_BOLD);
        tableHeader.setForeground(Style.TEXT_MAIN);
        tableHeader.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.add(tableHeader, BorderLayout.NORTH);

        String[] columns = { "Course ID", "Course Name", "Credits" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        enrollmentTable = new JTable(tableModel);
        enrollmentTable.setRowHeight(40);
        enrollmentTable.setShowVerticalLines(false);
        enrollmentTable.getTableHeader().setBackground(Style.BACKGROUND_LIGHT);
        enrollmentTable.getTableHeader().setFont(Style.FONT_BODY_BOLD);
        enrollmentTable.getTableHeader().setForeground(Style.TEXT_SECONDARY);
        enrollmentTable.setSelectionBackground(new Color(239, 246, 255));
        enrollmentTable.setSelectionForeground(Style.TEXT_MAIN);

        JScrollPane scrollPane = new JScrollPane(enrollmentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Style.SURFACE_LIGHT);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setOpaque(true); // Fix for macOS visibility
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFont(Style.FONT_BODY_BOLD);
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));
        return btn;
    }

    // --- Logic ---

    public void refreshData() {
        studentCombo.removeAllItems();
        List<Student> students = studentDAO.getAllStudents();
        for (Student s : students) {
            studentCombo.addItem(s);
        }

        courseCombo.removeAllItems();
        List<Course> courses = courseDAO.getAllCourses();
        for (Course c : courses) {
            courseCombo.addItem(c);
        }
    }

    private void loadEnrollmentsForSelectedStudent() {
        tableModel.setRowCount(0);
        Student selectedStudent = (Student) studentCombo.getSelectedItem();
        if (selectedStudent == null)
            return;

        List<Course> courses = enrollmentDAO.getCoursesByStudent(selectedStudent.getStudentId());
        for (Course c : courses) {
            tableModel.addRow(new Object[] { c.getCourseId(), c.getCourseName(), c.getCredits() });
        }
    }

    private void enroll() {
        Student s = (Student) studentCombo.getSelectedItem();
        Course c = (Course) courseCombo.getSelectedItem();

        if (s == null || c == null) {
            JOptionPane.showMessageDialog(this, "Please select both a student and a course.");
            return;
        }

        if (enrollmentDAO.enrollStudent(s.getStudentId(), c.getCourseId())) {
            loadEnrollmentsForSelectedStudent();
            JOptionPane.showMessageDialog(this, "Enrolled successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Enrollment failed (maybe already enrolled?)");
        }
    }

    private void unenroll() {
        Student s = (Student) studentCombo.getSelectedItem();

        int selectedRow = enrollmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a course from the table to remove.");
            return;
        }

        int courseId = (int) tableModel.getValueAt(selectedRow, 0);

        if (enrollmentDAO.removeEnrollment(s.getStudentId(), courseId)) {
            loadEnrollmentsForSelectedStudent();
            JOptionPane.showMessageDialog(this, "Unenrolled successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Error removing enrollment");
        }
    }
}
