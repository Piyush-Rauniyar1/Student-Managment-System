package course;

import enrollment.Style;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for managing courses.
 * Provides a UI for adding, updating, and deleting courses.
 */
public class CoursePanel extends JPanel {

    private JTextField courseNameField, creditsField;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private CourseDAO courseDAO;
    private int selectedCourseId = -1;

    public CoursePanel() {
        courseDAO = new CourseDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(Style.BACKGROUND_LIGHT);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top: Form
        add(createFormPanel(), BorderLayout.NORTH);

        // Center: Table
        add(createTablePanel(), BorderLayout.CENTER);

        loadCourses();
    }

    private JPanel createFormPanel() {
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
        JLabel title = new JLabel("COURSE DETAILS");
        title.setFont(Style.FONT_SUBHEADER);
        title.setForeground(Style.TEXT_MAIN);
        panel.add(title, gbc);

        // Inputs
        gbc.gridwidth = 1;
        gbc.gridy = 1;

        courseNameField = createLabeledInput(panel, "Course Name", gbc, 0);
        creditsField = createLabeledInput(panel, "Credits", gbc, 1);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Style.SURFACE_LIGHT);

        JButton addButton = createButton("Create Course", Style.PRIMARY, Color.WHITE);
        addButton.addActionListener(e -> addCourse());

        JButton updateButton = createButton("Update", Style.SURFACE_LIGHT, Style.TEXT_MAIN);
        updateButton.addActionListener(e -> updateCourse());

        JButton deleteButton = createButton("Delete", new Color(254, 226, 226), new Color(220, 38, 38));
        deleteButton.addActionListener(e -> deleteCourse());

        JButton clearButton = new JButton("Clear Form");
        clearButton.setForeground(Style.TEXT_SECONDARY);
        clearButton.setContentAreaFilled(false);
        clearButton.setBorderPainted(false);
        clearButton.addActionListener(e -> clearForm());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JTextField createLabeledInput(JPanel panel, String labelText, GridBagConstraints gbc, int xInfo) {
        gbc.gridx = xInfo;
        JPanel inputPanel = new JPanel(new BorderLayout(0, 5));
        inputPanel.setBackground(Style.SURFACE_LIGHT);

        JLabel label = new JLabel(labelText);
        label.setFont(Style.FONT_BODY_BOLD);
        label.setForeground(Style.TEXT_SECONDARY);

        JTextField textField = new JTextField();
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.BORDER_LIGHT),
                new EmptyBorder(8, 10, 8, 10)));

        inputPanel.add(label, BorderLayout.NORTH);
        inputPanel.add(textField, BorderLayout.CENTER);

        panel.add(inputPanel, gbc);
        return textField;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Style.SURFACE_LIGHT);
        panel.setBorder(BorderFactory.createLineBorder(Style.BORDER_LIGHT));

        String[] columns = { "ID", "Course Name", "Credits" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(40);
        courseTable.setShowVerticalLines(false);
        courseTable.getTableHeader().setBackground(Style.BACKGROUND_LIGHT);
        courseTable.getTableHeader().setFont(Style.FONT_BODY_BOLD);
        courseTable.getTableHeader().setForeground(Style.TEXT_SECONDARY);
        courseTable.setSelectionBackground(new Color(239, 246, 255));
        courseTable.setSelectionForeground(Style.TEXT_MAIN);

        courseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && courseTable.getSelectedRow() != -1) {
                loadSelectedCourse();
            }
        });

        JScrollPane scrollPane = new JScrollPane(courseTable);
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

    private void loadCourses() {
        tableModel.setRowCount(0);
        List<Course> courses = courseDAO.getAllCourses();
        for (Course c : courses) {
            tableModel.addRow(new Object[] { c.getCourseId(), c.getCourseName(), c.getCredits() });
        }
    }

    private void addCourse() {
        try {
            int credits = Integer.parseInt(creditsField.getText());
            Course c = new Course(courseNameField.getText(), credits);
            if (courseDAO.addCourse(c)) {
                loadCourses();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding course");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid credits");
        }
    }

    private void updateCourse() {
        if (selectedCourseId == -1)
            return;
        try {
            int credits = Integer.parseInt(creditsField.getText());
            Course c = new Course(selectedCourseId, courseNameField.getText(), credits);
            if (courseDAO.updateCourse(c)) {
                loadCourses();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating course");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid credits");
        }
    }

    private void deleteCourse() {
        if (selectedCourseId == -1)
            return;
        if (courseDAO.deleteCourse(selectedCourseId)) {
            loadCourses();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting course");
        }
    }

    private void loadSelectedCourse() {
        int row = courseTable.getSelectedRow();
        selectedCourseId = (int) tableModel.getValueAt(row, 0);
        courseNameField.setText((String) tableModel.getValueAt(row, 1));
        creditsField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
    }

    private void clearForm() {
        courseNameField.setText("");
        creditsField.setText("");
        selectedCourseId = -1;
        courseTable.clearSelection();
    }
}
