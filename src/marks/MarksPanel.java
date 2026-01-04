package marks;

import course.Course;
import course.CourseDAO;
import student.Student;
import student.StudentDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MarksPanel extends JPanel {
    private MarksDAO marksDAO;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private GradeService gradeService;

    private JComboBox<Course> courseComboBox;
    private JComboBox<Student> studentComboBox;
    private JTextField marksField;
    private JTextArea remarksArea;
    private JLabel calculatedGradeLabel;
    private JTable recentActivityTable;
    private DefaultTableModel tableModel;

    // Colors
    private final Color PRIMARY_COLOR = new Color(54, 126, 226); // #367ee2
    private final Color BACKGROUND_COLOR = new Color(248, 250, 251); // #f8fafb
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(15, 23, 42); // Slate 900
    private final Color SUBTEXT_COLOR = new Color(100, 116, 139); // Slate 500
    private final Color BORDER_COLOR = new Color(226, 232, 240); // #e2e8f0

    public MarksPanel() {
        marksDAO = new MarksDAO();
        studentDAO = new StudentDAO();
        courseDAO = new CourseDAO();
        gradeService = new GradeService();

        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Main Content (Grid: Left Input, Right Sidebar)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 0, 0, 20); // Gap between columns
        gbc.weighty = 1.0;

        // Left Column (Input)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.65;
        contentPanel.add(createLeftColumn(), gbc);

        // Right Column (Recent & Scale)
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.weightx = 0.35;
        contentPanel.add(createRightColumn(), gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND_COLOR);

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel title = new JLabel("Enter Student Marks");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(TEXT_COLOR);

        JLabel subtitle = new JLabel("Record exam scores and verify automated grade calculations.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(SUBTEXT_COLOR);

        titlePanel.add(title);
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        // Header Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(createOutlineButton("View History", "history"));
        buttonPanel.add(createOutlineButton("Import CSV", "download"));
        header.add(buttonPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createLeftColumn() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);

        // Selection Context Card
        panel.add(createSelectionCard());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Marks Entry Card
        panel.add(createEntryCard());

        return panel;
    }

    private JPanel createSelectionCard() {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());

        JLabel title = new JLabel("Selection Context");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(TEXT_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        card.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(1, 2, 20, 0));
        form.setBackground(CARD_COLOR);

        // Course Select
        JPanel coursePanel = new JPanel(new BorderLayout());
        coursePanel.setBackground(CARD_COLOR);
        JLabel l1 = new JLabel("COURSE CODE");
        l1.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l1.setForeground(SUBTEXT_COLOR);
        courseComboBox = new JComboBox<>();
        coursePanel.add(l1, BorderLayout.NORTH);
        coursePanel.add(courseComboBox, BorderLayout.CENTER);

        // Student Select
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.setBackground(CARD_COLOR);
        JLabel l2 = new JLabel("STUDENT");
        l2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        l2.setForeground(SUBTEXT_COLOR);
        studentComboBox = new JComboBox<>();
        studentPanel.add(l2, BorderLayout.NORTH);
        studentPanel.add(studentComboBox, BorderLayout.CENTER);

        form.add(coursePanel);
        form.add(studentPanel);
        card.add(form, BorderLayout.CENTER);

        return card;
    }

    private JPanel createEntryCard() {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_COLOR);
        JLabel title = new JLabel("Input Scores");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_COLOR);

        JLabel tag = new JLabel(" MID-TERM EXAM ");
        tag.setOpaque(true);
        tag.setBackground(new Color(254, 243, 199)); // Yellow-100
        tag.setForeground(new Color(180, 83, 9)); // Yellow-700
        tag.setFont(new Font("Segoe UI", Font.BOLD, 10));

        header.add(title, BorderLayout.WEST);
        header.add(tag, BorderLayout.EAST);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        card.add(header, BorderLayout.NORTH);

        // Center Content
        JPanel content = new JPanel(new GridLayout(1, 2, 30, 0));
        content.setBackground(CARD_COLOR);

        // Input Fields (Left Side of Card)
        JPanel inputs = new JPanel();
        inputs.setLayout(new BoxLayout(inputs, BoxLayout.Y_AXIS));
        inputs.setBackground(CARD_COLOR);

        addLabel(inputs, "Marks Obtained");
        JPanel marksWrapper = new JPanel(new BorderLayout());
        marksWrapper.setBackground(CARD_COLOR);
        marksField = new JTextField("0");
        marksField.setFont(new Font("Segoe UI", Font.BOLD, 24));
        marksField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Add listener to update grade on change
        marksField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateGrade();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateGrade();
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateGrade();
            }
        });

        JLabel suffix = new JLabel(" / 100 ");
        suffix.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        suffix.setOpaque(true);
        suffix.setBackground(new Color(241, 245, 249)); // Slate 100

        marksWrapper.add(marksField, BorderLayout.CENTER);
        marksWrapper.add(suffix, BorderLayout.EAST);
        inputs.add(marksWrapper);

        inputs.add(Box.createRigidArea(new Dimension(0, 5)));
        JLabel hint = new JLabel("Enter value between 0 and 100");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        hint.setForeground(SUBTEXT_COLOR);
        inputs.add(hint);

        inputs.add(Box.createRigidArea(new Dimension(0, 20)));
        addLabel(inputs, "Remarks (Optional)");
        remarksArea = new JTextArea(3, 20);
        remarksArea.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        remarksArea.setLineWrap(true);
        inputs.add(new JScrollPane(remarksArea));

        content.add(inputs);

        // Grade Visualization (Right Side of Card)
        JPanel gradeVis = new JPanel();
        gradeVis.setLayout(new BoxLayout(gradeVis, BoxLayout.Y_AXIS));
        gradeVis.setBackground(new Color(248, 250, 251)); // Slate 50
        gradeVis.setBorder(BorderFactory.createLineBorder(new Color(241, 245, 249)));

        JLabel lblCalc = new JLabel("CALCULATED GRADE");
        lblCalc.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblCalc.setForeground(SUBTEXT_COLOR);
        lblCalc.setAlignmentX(Component.CENTER_ALIGNMENT);

        calculatedGradeLabel = new JLabel("F");
        calculatedGradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        calculatedGradeLabel.setForeground(Color.RED);
        calculatedGradeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        gradeVis.add(Box.createVerticalGlue());
        gradeVis.add(lblCalc);
        gradeVis.add(Box.createRigidArea(new Dimension(0, 10)));
        gradeVis.add(calculatedGradeLabel);
        gradeVis.add(Box.createVerticalGlue());

        content.add(gradeVis);
        card.add(content, BorderLayout.CENTER);

        // Footer Actions
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(CARD_COLOR);
        footer.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnClear = new JButton("Clear Form");
        btnClear.setBackground(CARD_COLOR);
        btnClear.setForeground(SUBTEXT_COLOR);
        btnClear.setBorderPainted(false);

        JButton btnSave = new JButton("Save Record");
        btnSave.setBackground(PRIMARY_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.setOpaque(true);
        btnSave.setBorderPainted(false);
        btnSave.addActionListener(e -> saveMarks());

        footer.add(btnClear);
        footer.add(btnSave);
        card.add(footer, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createRightColumn() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);

        // Recent Activity
        JPanel recentCard = createCard();
        recentCard.setLayout(new BorderLayout());

        JLabel title = new JLabel("Recent Activity");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setBorder(new EmptyBorder(0, 0, 10, 0));
        recentCard.add(title, BorderLayout.NORTH);

        String[] cols = { "Student", "Score", "Grd" };
        tableModel = new DefaultTableModel(cols, 0);
        recentActivityTable = new JTable(tableModel);
        recentActivityTable.setRowHeight(30);
        recentActivityTable.setShowGrid(false);

        recentCard.add(new JScrollPane(recentActivityTable), BorderLayout.CENTER);

        panel.add(recentCard);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Grading Scale
        JPanel scaleCard = createCard();
        scaleCard.setLayout(new BoxLayout(scaleCard, BoxLayout.Y_AXIS));
        JLabel scaleTitle = new JLabel("Grading Scale Ref.");
        scaleTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        scaleCard.add(scaleTitle);
        scaleCard.add(Box.createRigidArea(new Dimension(0, 10)));
        scaleCard.add(new JLabel("80 - 100 : A (4.0)"));
        scaleCard.add(new JLabel("70 - 79   : B (3.0)"));
        scaleCard.add(new JLabel("60 - 69   : C (2.0)"));
        scaleCard.add(new JLabel("50 - 59   : D (1.0)"));
        scaleCard.add(new JLabel("0 - 49     : F (0.0)"));

        panel.add(scaleCard);

        return panel;
    }

    private JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                new EmptyBorder(20, 20, 20, 20)));
        return card;
    }

    private void addLabel(JPanel p, String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_COLOR);
        p.add(l);
        p.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private JButton createOutlineButton(String text, String icon) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return btn;
    }

    private void updateGrade() {
        try {
            double marks = Double.parseDouble(marksField.getText());
            String grade = gradeService.calculateGrade(marks);
            calculatedGradeLabel.setText(grade);

            if (grade.startsWith("A"))
                calculatedGradeLabel.setForeground(new Color(22, 163, 74)); // Green
            else if (grade.startsWith("B"))
                calculatedGradeLabel.setForeground(new Color(37, 99, 235)); // Blue
            else if (grade.startsWith("C"))
                calculatedGradeLabel.setForeground(new Color(202, 138, 4)); // Yellow
            else if (grade.startsWith("F"))
                calculatedGradeLabel.setForeground(Color.RED);
        } catch (NumberFormatException ex) {
            calculatedGradeLabel.setText("-");
            calculatedGradeLabel.setForeground(SUBTEXT_COLOR);
        }
    }

    private void loadData() {
        List<Student> students = studentDAO.getAllStudents();
        for (Student s : students)
            studentComboBox.addItem(s);

        List<Course> courses = courseDAO.getAllCourses();
        for (Course c : courses)
            courseComboBox.addItem(c);

        refreshRecent();
    }

    private void saveMarks() {
        Student s = (Student) studentComboBox.getSelectedItem();
        Course c = (Course) courseComboBox.getSelectedItem();

        try {
            double val = Double.parseDouble(marksField.getText());
            if (val < 0 || val > 100) {
                JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100");
                return;
            }

            Marks m = new Marks();
            m.setStudentId(s.getStudentId());
            m.setCourseId(c.getCourseId());
            m.setMarks(val);
            gradeService.assignGrade(m);

            if (marksDAO.addMarks(m)) {
                JOptionPane.showMessageDialog(this, "Marks saved successfully!");
                refreshRecent();
            } else {
                JOptionPane.showMessageDialog(this, "Error saving marks. Already exists?");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format");
        }
    }

    private void refreshRecent() {
        // For simplicity, just clearing and not reloading recent since DAO doesn't have
        // getLastN yet
        // In real app, fetch top 5 by date desc

        // Mock add to table if valid
        tableModel.setRowCount(0);
        // Logic to fetch recent would go here.
        Student s = (Student) studentComboBox.getSelectedItem();
        if (s != null) {
            List<Marks> marks = marksDAO.getMarksByStudent(s.getStudentId());
            for (Marks m : marks) {
                tableModel.addRow(new Object[] { s.getName(), m.getMarks(), m.getGrade() });
            }
        }
    }
}
