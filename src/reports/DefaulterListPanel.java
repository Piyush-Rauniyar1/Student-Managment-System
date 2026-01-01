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

public class DefaulterListPanel extends JPanel {
    private CourseDAO courseDAO;
    private ReportDAO reportDAO;
    private JComboBox<Course> courseComboBox;
    private JSpinner thresholdSpinner;
    private JLabel countLabel;
    private DefaultTableModel tableModel;

    public DefaulterListPanel() {
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

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setBackground(new Color(246, 247, 248));

        JLabel title = new JLabel("Defaulter List");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel sub = new JLabel("Identify students below the minimum attendance threshold.");
        sub.setForeground(new Color(100, 116, 139));

        titleBox.add(title);
        titleBox.add(sub);

        header.add(titleBox, BorderLayout.WEST);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(246, 247, 248));
        JButton btnPrint = new JButton("Print");
        btnPrint.setBackground(Color.WHITE);
        JButton btnExport = new JButton("Export");
        btnExport.setBackground(Color.WHITE);
        btnPanel.add(btnPrint);
        btnPanel.add(btnExport);
        header.add(btnPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(246, 247, 248));

        // Filter Bar
        content.add(createFilterBar());
        content.add(Box.createRigidArea(new Dimension(0, 20)));

        // Results
        content.add(createResultsPanel());

        add(new JScrollPane(content), BorderLayout.CENTER);
    }

    private JPanel createFilterBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        // Course Select
        JPanel cPanel = new JPanel(new BorderLayout());
        cPanel.setBackground(Color.WHITE);
        cPanel.add(new JLabel("Course"), BorderLayout.NORTH);
        courseComboBox = new JComboBox<>();
        courseComboBox.setPreferredSize(new Dimension(220, 35));
        cPanel.add(courseComboBox, BorderLayout.CENTER);

        // Threshold Spinner
        JPanel tPanel = new JPanel(new BorderLayout());
        tPanel.setBackground(Color.WHITE);
        tPanel.add(new JLabel("Min Attendance %"), BorderLayout.NORTH);
        thresholdSpinner = new JSpinner(new SpinnerNumberModel(75, 0, 100, 1));
        thresholdSpinner.setPreferredSize(new Dimension(100, 35));
        tPanel.add(thresholdSpinner, BorderLayout.CENTER);

        // Generate Button
        JButton btnGenerate = new JButton("Generate List");
        btnGenerate.setBackground(new Color(54, 126, 226));
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGenerate.addActionListener(e -> generateList());

        panel.add(cPanel);
        panel.add(tPanel);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(btnGenerate);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        // Results Header
        JPanel rHeader = new JPanel(new BorderLayout());
        rHeader.setBackground(new Color(248, 250, 251));
        rHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        countLabel = new JLabel("0 Defaulters Found");
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rHeader.add(countLabel, BorderLayout.WEST);

        panel.add(rHeader, BorderLayout.NORTH);

        // Table
        String[] cols = { "Student ID", "Name", "Total Lectures", "Attended", "Attendance %", "Action" };
        tableModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.getTableHeader().setBackground(new Color(241, 245, 249));

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaulterStatusRenderer());
        table.getColumnModel().getColumn(5).setCellRenderer(new ActionRenderer());

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadCourses() {
        List<Course> courses = courseDAO.getAllCourses();
        for (Course c : courses) {
            courseComboBox.addItem(c);
        }
    }

    private void generateList() {
        Course selected = (Course) courseComboBox.getSelectedItem();
        if (selected == null)
            return;

        double threshold = (int) thresholdSpinner.getValue();

        List<StudentAttendanceStat> defaulters = reportDAO.getDefaultersByCourse(selected.getCourseId(), threshold);

        tableModel.setRowCount(0);
        for (StudentAttendanceStat s : defaulters) {
            tableModel.addRow(new Object[] {
                    s.rollNo != null ? s.rollNo : s.studentId,
                    s.studentName,
                    s.totalLectures,
                    s.presentCount,
                    s.percentage, // Double for renderer
                    "Send Email"
            });
        }

        countLabel.setText(defaulters.size() + " Defaulters Found");

        if (defaulters.size() > 0) {
            countLabel.setForeground(new Color(225, 29, 72)); // Danger color
        } else {
            countLabel.setForeground(new Color(22, 163, 74)); // Green
        }
    }

    // Renderers
    class DefaulterStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if (value instanceof Double) {
                double val = (Double) value;
                JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
                p.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

                // Progress bar
                JProgressBar pb = new JProgressBar(0, 100);
                pb.setValue((int) val);
                pb.setPreferredSize(new Dimension(80, 8));
                pb.setForeground(val < 50 ? new Color(225, 29, 72) : new Color(249, 115, 22)); // Red or Orange

                JLabel l = new JLabel(String.format("%.1f%%", val));
                l.setFont(new Font("Segoe UI", Font.BOLD, 12));
                l.setForeground(val < 50 ? new Color(225, 29, 72) : new Color(249, 115, 22));

                p.add(pb);
                p.add(l);
                return p;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    class ActionRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            JLabel l = new JLabel("Send Warning");
            l.setForeground(Color.BLUE);
            l.setHorizontalAlignment(CENTER);
            return l;
        }
    }
}
