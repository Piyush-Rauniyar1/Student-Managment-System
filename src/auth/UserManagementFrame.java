package auth;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * User Management Dashboard for administrators
 */
public class UserManagementFrame extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserDAO userDAO;
    private JTextField searchField;

    public UserManagementFrame() {
        userDAO = new UserDAO();
        initializeUI();
        loadUsers();
    }

    private void initializeUI() {
        setTitle("SMS Admin - User Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());

        // Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // Main content
        add(createMainContent(), BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(229, 231, 235)));

        // Logo section
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel logoLabel = new JLabel("🎓 SMS Admin");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoPanel.add(logoLabel);

        JLabel subtitleLabel = new JLabel("University Portal");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(107, 114, 128));
        logoPanel.add(subtitleLabel);

        sidebar.add(logoPanel, BorderLayout.NORTH);

        // Navigation menu
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addMenuItem(menuPanel, "📊 Dashboard", false);
        addMenuItem(menuPanel, "🎓 Students", false);
        addMenuItem(menuPanel, "👤 Faculty", false);
        addMenuItem(menuPanel, "📚 Courses", false);
        addMenuItem(menuPanel, "👥 User Management", true);

        sidebar.add(menuPanel, BorderLayout.CENTER);

        // Bottom section
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        JButton settingsBtn = createMenuButton("⚙️ Settings", false);
        JButton logoutBtn = createMenuButton("🚪 Logout", false);
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        bottomPanel.add(settingsBtn);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        bottomPanel.add(logoutBtn);

        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private void addMenuItem(JPanel panel, String text, boolean active) {
        JButton button = createMenuButton(text, active);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private JButton createMenuButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 13));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (active) {
            button.setBackground(new Color(219, 234, 254));
            button.setForeground(new Color(54, 126, 226));
            button.setFont(new Font("Arial", Font.BOLD, 13));
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(14, 19, 27));
        }

        return button;
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(246, 247, 248));

        // Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);

        // Content area
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(246, 247, 248));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(246, 247, 248));

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        JLabel subtitleLabel = new JLabel("Manage system access, roles, and user accounts.");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(107, 114, 128));
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);

        contentPanel.add(titlePanel, BorderLayout.NORTH);

        // Table card
        JPanel tableCard = createTableCard();
        contentPanel.add(tableCard, BorderLayout.CENTER);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField(30);
        searchField.setPreferredSize(new Dimension(300, 35));
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        searchPanel.add(new JLabel("🔍"));
        searchPanel.add(searchField);

        header.add(searchPanel, BorderLayout.WEST);

        // User info
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(Color.WHITE);

        JLabel userLabel = new JLabel("Admin User");
        userLabel.setFont(new Font("Arial", Font.BOLD, 13));
        userPanel.add(userLabel);

        header.add(userPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolbar.setBackground(new Color(249, 250, 251));
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)));

        JButton addButton = new JButton("➕ Add New User");
        addButton.setFont(new Font("Arial", Font.BOLD, 12));
        addButton.setBackground(new Color(54, 126, 226));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener(e -> openAddUserDialog());
        toolbar.add(addButton);

        JButton refreshButton = new JButton("🔄 Refresh");
        refreshButton.setFont(new Font("Arial", Font.PLAIN, 12));
        refreshButton.setBackground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> loadUsers());
        toolbar.add(refreshButton);

        card.add(toolbar, BorderLayout.NORTH);

        // Table
        String[] columns = {"User ID", "Username", "Email", "Role", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only Actions column
            }
        };

        userTable = new JTable(tableModel);
        userTable.setRowHeight(45);
        userTable.setFont(new Font("Arial", Font.PLAIN, 12));
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        userTable.getTableHeader().setBackground(new Color(249, 250, 251));
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Render actions column
        userTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userDAO.getAllUsers();

        for (User user : users) {
            Object[] row = {
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    user.isActive() ? "✅ Active" : "❌ Inactive",
                    "Actions"
            };
            tableModel.addRow(row);
        }
    }

    private void openAddUserDialog() {
        dispose();
        new RegistrationFrame();
    }

    // Button renderer for Actions column
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton editBtn, deleteBtn;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            setOpaque(true);

            editBtn = new JButton("✏️");
            deleteBtn = new JButton("🗑️");

            editBtn.setPreferredSize(new Dimension(40, 30));
            deleteBtn.setPreferredSize(new Dimension(40, 30));

            add(editBtn);
            add(deleteBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // Button editor for Actions column
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton editBtn, deleteBtn;
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            editBtn = new JButton("✏️");
            deleteBtn = new JButton("🗑️");

            editBtn.setPreferredSize(new Dimension(40, 30));
            deleteBtn.setPreferredSize(new Dimension(40, 30));

            editBtn.addActionListener(e -> {
                fireEditingStopped();
                handleEdit(selectedRow);
            });

            deleteBtn.addActionListener(e -> {
                fireEditingStopped();
                handleDelete(selectedRow);
            });

            panel.add(editBtn);
            panel.add(deleteBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            selectedRow = row;
            return panel;
        }

        private void handleEdit(int row) {
            int userId = (int) tableModel.getValueAt(row, 0);
            JOptionPane.showMessageDialog(null, "Edit user ID: " + userId);
            // Implement edit functionality
        }

        private void handleDelete(int row) {
            int userId = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this user?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (userDAO.deleteUser(userId)) {
                    JOptionPane.showMessageDialog(null, "User deleted successfully!");
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete user.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}