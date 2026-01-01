package reports;

import course.Course;
import course.CourseDAO;
import reports.ReportDAO.StudentAttendanceStat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class AttendanceReportPanel extends JPanel {
    private CourseDAO courseDAO;
    private ReportDAO reportDAO;
    private JComboBox<Course> courseComboBox;
    private JLabel totalStudentsLabel, avgAttendanceLabel, atRiskLabel;
    private DefaultTableModel tableModel;

    public AttendanceReportPanel() {
        courseDAO = new CourseDAO();
        reportDAO = new ReportDAO();
        initializeUI();
        loadCourses();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(246, 247, 248));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(246, 247, 248));
        JLabel title = new JLabel("Attendance Report");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(246, 247, 248));

        // Filter Bar
        content.add(createFilterBar());
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // Stats Cards
        content.add(createStatsPanel());
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // Table
        content.add(createTablePanel());

        add(new JScrollPane(content), BorderLayout.CENTER);
    }

    private JPanel createFilterBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        JPanel cPanel = new JPanel(new BorderLayout());
        cPanel.setBackground(Color.WHITE);
        cPanel.add(new JLabel("Course"), BorderLayout.NORTH);
        courseComboBox = new JComboBox<>();
        courseComboBox.setPreferredSize(new Dimension(250, 30));
        cPanel.add(courseComboBox, BorderLayout.CENTER);

        JButton btnGenerate = new JButton("Generate Report");
        btnGenerate.setBackground(new Color(54, 126, 226));
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.addActionListener(e -> generateReport());

        panel.add(cPanel);
        panel.add(btnGenerate);

        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(new Color(246, 247, 248));

        totalStudentsLabel = createStatCard("Total Students", "0", "Enrolled");
        avgAttendanceLabel = createStatCard("Avg. Attendance", "0%", "Class Average");
        atRiskLabel = createStatCard("At Risk Students", "0", "< 75% Attendance");

        panel.add(totalStudentsLabel);
        panel.add(avgAttendanceLabel);
        panel.add(atRiskLabel);

        return panel;
    }

    private JLabel createStatCard(String title, String value, String sub) {
        JLabel l = new JLabel("<html><div style='background:white;padding:20px;border:1px solid #e2e8f0;width:200px'>" +
                "<div style='color:#64748b;font-size:10px;text-transform:uppercase'>" + title + "</div>" +
                "<div style='font-size:32px;font-weight:bold;margin:10px 0'>" + value + "</div>" +
                "<div style='color:#64748b;font-size:12px'>" + sub + "</div>" +
                "</div></html>");
        return l;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        panel.setBorder(createTitledBorder("Search Results"));

        String[] cols = { "ID", "Student Name", "Total Lectures", "Present", "Absent", "Attendance %", "Status" };
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.getColumnModel().getColumn(5).setCellRenderer(new ProgressBarRenderer());
        table.getColumnModel().getColumn(6).setCellRenderer(new StatusRenderer());

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private javax.swing.border.Border createTitledBorder(String title) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createTitledBorder(
                        BorderFactory.createEmptyBorder(10, 10, 10, 10),
                        title));
    }

    private void loadCourses() {
        List<Course> courses = courseDAO.getAllCourses();
        for (Course c : courses) {
            courseComboBox.addItem(c);
        }
    }

    private void generateReport() {
        Course selected = (Course) courseComboBox.getSelectedItem();
        if (selected == null)
            return;

        int courseId = selected.getCourseId();

        // Stats
        Map<String, Object> stats = reportDAO.generateCourseAttendanceReport(courseId);
        int total = (int) stats.get("totalStudents");
        double avg = (double) stats.get("avgAttendance");

        // List
        List<StudentAttendanceStat> list = reportDAO.getCourseAttendanceList(courseId);
        int atRisk = 0;

        tableModel.setRowCount(0);
        for (StudentAttendanceStat s : list) {
            int absent = s.totalLectures - s.presentCount;
            String status = "Good";
            if (s.percentage < 75) {
                status = "At Risk";
                atRisk++;
            } else if (s.percentage < 85) {
                status = "Warning";
            }

            tableModel.addRow(new Object[] {
                    s.studentId, // Or rollNo
                    s.studentName,
                    s.totalLectures,
                    s.presentCount,
                    absent,
                    s.percentage,
                    status
            });
        }

        // Update Stats Labels
        updateStatCard(totalStudentsLabel, "Total Students", String.valueOf(total), "Enrolled");
        updateStatCard(avgAttendanceLabel, "Avg. Attendance", String.format("%.1f%%", avg), "Class Average");
        updateStatCard(atRiskLabel, "At Risk Students", String.valueOf(atRisk), "< 75% Attendance");
    }

    private void updateStatCard(JLabel label, String title, String value, String sub) {
        label.setText("<html><div style='background:white;padding:20px;border:1px solid #e2e8f0;width:200px'>" +
                "<div style='color:#64748b;font-size:10px;text-transform:uppercase'>" + title + "</div>" +
                "<div style='font-size:32px;font-weight:bold;margin:10px 0'>" + value + "</div>" +
                "<div style='color:#64748b;font-size:12px'>" + sub + "</div>" +
                "</div></html>");
    }

    // Renderers
    class ProgressBarRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (value instanceof Double) {
                double val = (Double) value;
                JProgressBar pb = new JProgressBar(0, 100);
                pb.setValue((int) val);
                pb.setString(String.format("%.1f%%", val));
                pb.setStringPainted(true);
                if (val < 75)
                    pb.setForeground(Color.RED);
                else
                    pb.setForeground(new Color(54, 126, 226));
                return pb;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    class StatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = (String) value;
            l.setHorizontalAlignment(SwingConstants.CENTER);
            if ("At Risk".equals(status)) {
                l.setForeground(new Color(185, 28, 28));
                l.setBackground(new Color(254, 226, 226));
            } else if ("Warning".equals(status)) {
                l.setForeground(new Color(180, 83, 9));
                l.setBackground(new Color(254, 243, 199));
            } else {
                l.setForeground(new Color(21, 128, 61));
                l.setBackground(new Color(220, 252, 231));
            }
            l.setOpaque(true);
            return l;
        }
    }
}
