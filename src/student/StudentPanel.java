
package student;

import enrollment.Style;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel for managing students.
 * Provides a UI for adding, updating, and deleting students.
 */
public class StudentPanel extends JPanel {

    private JTextField nameField, rollNoField, emailField;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO;
    private int selectedStudentId = -1;
    private java.awt.event.ActionListener enrollmentListener;

    public void setEnrollmentListener(java.awt.event.ActionListener listener) {
        this.enrollmentListener = listener;
    }

    public StudentPanel() {
        studentDAO = new StudentDAO();
        setLayout(new BorderLayout(20, 20));
        setBackground(Style.BACKGROUND_LIGHT);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top: Form
        add(createFormPanel(), BorderLayout.NORTH);

        // Center: Table
        add(createTablePanel(), BorderLayout.CENTER);

        loadStudents();
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
        gbc.gridwidth = 3;
        JLabel title = new JLabel("STUDENT DETAILS");
        title.setFont(Style.FONT_SUBHEADER);
        title.setForeground(Style.TEXT_MAIN);
        panel.add(title, gbc);

        // Inputs
        gbc.gridwidth = 1;
        gbc.gridy = 1;

        nameField = createLabeledInput(panel, "Full Name", gbc, 0);
        rollNoField = createLabeledInput(panel, "Roll Number", gbc, 1);
        emailField = createLabeledInput(panel, "Email Address", gbc, 2);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Style.SURFACE_LIGHT);

        JButton addButton = createButton("Create Student", Style.PRIMARY, Color.WHITE);
        addButton.addActionListener(e -> addStudent());

        JButton updateButton = createButton("Update", Style.SURFACE_LIGHT, Style.TEXT_MAIN);
        updateButton.addActionListener(e -> updateStudent());

        JButton deleteButton = createButton("Delete", new Color(254, 226, 226), new Color(220, 38, 38)); // Red-ish
        deleteButton.addActionListener(e -> deleteStudent());

        JButton clearButton = new JButton("Clear Form");
        clearButton.setForeground(Style.TEXT_SECONDARY);
        clearButton.setContentAreaFilled(false);
        clearButton.setBorderPainted(false);
        clearButton.addActionListener(e -> clearForm());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        JButton enrollNavButton = createButton("Enroll Student", Style.PRIMARY, Color.WHITE);
        enrollNavButton.addActionListener(e -> {
            if (enrollmentListener != null)
                enrollmentListener.actionPerformed(e);
        });
        buttonPanel.add(enrollNavButton);

        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
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

        String[] columns = { "ID", "Name", "Roll No", "Email" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(40);
        studentTable.setShowVerticalLines(false);
        studentTable.getTableHeader().setBackground(Style.BACKGROUND_LIGHT);
        studentTable.getTableHeader().setFont(Style.FONT_BODY_BOLD);
        studentTable.getTableHeader().setForeground(Style.TEXT_SECONDARY);
        studentTable.setSelectionBackground(new Color(239, 246, 255));
        studentTable.setSelectionForeground(Style.TEXT_MAIN);

        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && studentTable.getSelectedRow() != -1) {
                loadSelectedStudent();
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
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

    private void loadStudents() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[] { s.getStudentId(), s.getName(), s.getRollNo(), s.getEmail() });
        }
    }

    private void addStudent() {
        Student s = new Student(nameField.getText(), rollNoField.getText(), emailField.getText());
        if (studentDAO.addStudent(s)) {
            loadStudents();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Error adding student");
        }
    }

    private void updateStudent() {
        if (selectedStudentId == -1)
            return;
        Student s = new Student(selectedStudentId, nameField.getText(), rollNoField.getText(), emailField.getText());
        if (studentDAO.updateStudent(s)) {
            loadStudents();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Error updating student");
        }
    }

    private void deleteStudent() {
        if (selectedStudentId == -1)
            return;
        if (studentDAO.deleteStudent(selectedStudentId)) {
            loadStudents();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting student");
        }
    }

    private void loadSelectedStudent() {
        int row = studentTable.getSelectedRow();
        selectedStudentId = (int) tableModel.getValueAt(row, 0);
        nameField.setText((String) tableModel.getValueAt(row, 1));
        rollNoField.setText((String) tableModel.getValueAt(row, 2));
        emailField.setText((String) tableModel.getValueAt(row, 3));
    }

    private void clearForm() {
        nameField.setText("");
        rollNoField.setText("");
        emailField.setText("");
        selectedStudentId = -1;
        studentTable.clearSelection();
    }
}
