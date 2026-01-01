package reports;

import course.Course;
import marks.Marks;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentReportPanel extends JPanel {
    private ReportDAO reportDAO;
    private JTextField searchField;
    private JPanel resultPanel;
    private JLabel nameLabel, infoLabel, idLabel, yearLabel, deptLabel, advisorLabel;
    private DefaultTableModel tableModel;
    private JLabel totalCreditsLabel, attendanceAvgLabel, gpaLabel;

    public StudentReportPanel() {
        reportDAO = new ReportDAO();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(246, 247, 248)); // Background Light

        // Top Search Bar
        add(createSearchPanel(), BorderLayout.NORTH);

        // Main Content Area
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(new Color(246, 247, 248));
        resultPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        resultPanel.setVisible(false); // Hidden initially

        // Profile Section
        JPanel profilePanel = createProfilePanel();

        // Table Section
        JPanel tablePanel = createTablePanel();

        // Footer Summary
        JPanel footerPanel = createFooterStats();

        // Assemble Result Panel
        JPanel centerContainer = new JPanel();
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        centerContainer.setBackground(new Color(246, 247, 248));
        centerContainer.add(profilePanel);
        centerContainer.add(Box.createRigidArea(new Dimension(0, 20)));
        centerContainer.add(tablePanel);
        centerContainer.add(footerPanel);

        resultPanel.add(new JScrollPane(centerContainer), BorderLayout.CENTER);
        add(resultPanel, BorderLayout.CENTER);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JLabel lbl = new JLabel("Student ID:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setToolTipText("Enter Student ID (e.g. 1)");

        JButton btnSearch = new JButton("Generate Report");
        btnSearch.setBackground(new Color(54, 126, 226));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSearch.addActionListener(e -> generateReport());

        panel.add(lbl);
        panel.add(searchField);
        panel.add(btnSearch);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        // Header (Image + Name)
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        header.setBackground(Color.WHITE);

        // Mock Image Placeholder
        JLabel imgLabel = new JLabel("Please Select");
        imgLabel.setPreferredSize(new Dimension(80, 80));
        imgLabel.setOpaque(true);
        imgLabel.setBackground(Color.LIGHT_GRAY);
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel textInfo = new JPanel(new GridLayout(2, 1));
        textInfo.setBackground(Color.WHITE);

        nameLabel = new JLabel("Student Name");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        infoLabel = new JLabel("Course / Semester Info");
        infoLabel.setForeground(Color.GRAY);

        textInfo.add(nameLabel);
        textInfo.add(infoLabel);

        header.add(imgLabel);
        header.add(textInfo);

        panel.add(header, BorderLayout.WEST);

        // Details Grid
        JPanel details = new JPanel(new GridLayout(1, 4, 10, 0));
        details.setBackground(Color.WHITE);
        details.setBorder(new EmptyBorder(20, 20, 20, 20));

        idLabel = createDetailLabel("Student ID", "-");
        yearLabel = createDetailLabel("Academic Year", "2023 - 2024");
        deptLabel = createDetailLabel("Department", "Computer Science");
        advisorLabel = createDetailLabel("Advisor", "Prof. Assign");

        details.add(idLabel);
        details.add(yearLabel);
        details.add(deptLabel);
        details.add(advisorLabel);

        panel.add(details, BorderLayout.SOUTH);

        return panel;
    }

    private JLabel createDetailLabel(String title, String value) {
        JLabel l = new JLabel("<html><div style='color:#64748b;font-size:10px;text-transform:uppercase'>" + title
                + "</div><div style='font-size:14px;font-weight:bold'>" + value + "</div></html>");
        return l;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        String[] cols = { "Code", "Course Name", "Credits", "Marks", "Grade", "Attendance" };
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(new Color(248, 250, 251));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Custom Renderer for Grade and Attendance
        table.getColumnModel().getColumn(4).setCellRenderer(new GradeRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new AttendanceRenderer());

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFooterStats() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 40, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240))); // Top border only

        totalCreditsLabel = createStatLabel("Total Credits", "0.0");
        attendanceAvgLabel = createStatLabel("Attendance Avg", "0%");
        gpaLabel = createStatLabel("Cumulative GPA", "0.0"); // Placeholder logic for GPA

        panel.add(totalCreditsLabel);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(attendanceAvgLabel);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(gpaLabel);

        return panel;
    }

    private JLabel createStatLabel(String title, String value) {
        JLabel l = new JLabel(
                "<html><div style='text-align:right'><div style='color:#64748b;font-size:10px;text-transform:uppercase'>"
                        + title + "</div><div style='font-size:18px;font-weight:bold'>" + value
                        + "</div></div></html>");
        return l;
    }

    private void generateReport() {
        String input = searchField.getText().trim();
        if (input.isEmpty())
            return;

        try {
            int studentId = Integer.parseInt(input);
            StudentReport report = reportDAO.generateStudentReport(studentId);

            if (report == null) {
                JOptionPane.showMessageDialog(this, "Student not found!", "Error", JOptionPane.ERROR_MESSAGE);
                resultPanel.setVisible(false);
                return;
            }

            // Populate Data
            nameLabel.setText(report.getStudentName());
            infoLabel.setText(report.getDepartment() + " • " + report.getSemester());
            idLabel.setText(
                    "<html><div style='color:#64748b;font-size:10px;text-transform:uppercase'>Student ID</div><div style='font-size:14px;font-weight:bold'>"
                            + report.getRollNo() + "</div></html>");

            tableModel.setRowCount(0);
            double totalCredits = 0;
            double totalPercentage = 0;
            int count = 0;

            if (report.getCourses() != null) {
                for (Course c : report.getCourses()) {
                    Marks m = report.getMarksMap().get(c.getCourseId());
                    Double att = report.getAttendanceMap().get(c.getCourseId());

                    String grade = (m != null) ? m.getGrade() : "-";
                    double marksVal = (m != null) ? m.getMarks() : 0;
                    double attVal = (att != null) ? att : 0.0;

                    tableModel.addRow(new Object[] {
                            "C" + c.getCourseId(), // Dummy code
                            c.getCourseName(),
                            c.getCredits() + ".0",
                            marksVal,
                            grade,
                            attVal // Pass double for renderer
                    });

                    totalCredits += c.getCredits();
                    totalPercentage += attVal;
                    count++;
                }
            }

            totalCreditsLabel.setText(
                    "<html><div style='text-align:right'><div style='color:#64748b;font-size:10px;text-transform:uppercase'>Total Credits</div><div style='font-size:18px;font-weight:bold'>"
                            + totalCredits + "</div></div></html>");

            double avgAtt = (count > 0) ? totalPercentage / count : 0;
            attendanceAvgLabel.setText(
                    "<html><div style='text-align:right'><div style='color:#64748b;font-size:10px;text-transform:uppercase'>Attendance Avg</div><div style='font-size:18px;font-weight:bold'>"
                            + String.format("%.1f", avgAtt) + "%</div></div></html>");

            resultPanel.setVisible(true);
            revalidate();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID format", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Renderers
    class GradeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String grade = (String) value;
            l.setHorizontalAlignment(SwingConstants.CENTER);

            if ("A".equals(grade)) {
                l.setForeground(new Color(22, 163, 74));
                l.setBackground(new Color(220, 252, 231));
            } else if ("B".equals(grade)) {
                l.setForeground(new Color(202, 138, 4));
                l.setBackground(new Color(254, 249, 195));
            } else if ("F".equals(grade)) {
                l.setForeground(Color.RED);
                l.setBackground(new Color(254, 226, 226));
            } else {
                l.setForeground(Color.BLACK);
                l.setBackground(Color.WHITE);
            }
            l.setOpaque(true);
            return l;
        }
    }

    class AttendanceRenderer extends DefaultTableCellRenderer {
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
}
