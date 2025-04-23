package Working;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentRecordManagement extends JFrame {
    private JTextField idField, nameField, phoneField;
    private JComboBox<String> courseCombo;
    private JTextArea addressArea;
    private JPasswordField passwordField;
    private JTextField deleteIdField;
    private JLabel statusLabel;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/university_db";
    private static final String USER = "root";
    private static final String PASS = "Pratiksql@2004";

    public StudentRecordManagement() {
        setTitle("Student Record Management");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(173, 216, 230)); // Light blue background

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(173, 216, 230));
        
        // Header
        JLabel headerLabel = new JLabel("Student Record Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.BLUE);
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Operations panel (Insert and Delete)
        JPanel operationsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        operationsPanel.setBackground(new Color(173, 216, 230));
        
        // Insert panel
        JPanel insertPanel = createInsertPanel();
        insertPanel.setBorder(BorderFactory.createTitledBorder("Insert Student"));
        insertPanel.setBackground(new Color(200, 230, 240)); // Lighter blue
        
        // Delete panel
        JPanel deletePanel = createDeletePanel();
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Student"));
        deletePanel.setBackground(new Color(200, 230, 240)); // Lighter blue
        
        operationsPanel.add(insertPanel);
        operationsPanel.add(deletePanel);
        
        mainPanel.add(operationsPanel, BorderLayout.CENTER);
        
        // Status label for feedback
        statusLabel = new JLabel("Ready", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(Color.BLUE);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Create table panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
        
        // Load data when form opens
        loadStudentData();
    }
    
    private JPanel createInsertPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.setBackground(new Color(200, 230, 240));
        
        // StudentID
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Student ID (SXXX):"), gbc);
        
        gbc.gridx = 1;
        idField = new JTextField(15);
        panel.add(idField, gbc);
        
        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Name (2+ words, 5+ chars):"), gbc);
        
        gbc.gridx = 1;
        nameField = new JTextField(15);
        panel.add(nameField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Address (10+ chars):"), gbc);
        
        gbc.gridx = 1;
        addressArea = new JTextArea(3, 15);
        addressArea.setLineWrap(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        panel.add(addressScroll, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Phone (10 digits, starts with 6-9):"), gbc);
        
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        panel.add(phoneField, gbc);
        
        // Course
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Course:"), gbc);
        
        gbc.gridx = 1;
        String[] courses = {"Computer Science", "Electrical Engineering", "Mechanical Engineering", 
                           "Civil Engineering", "Physics", "Mathematics", "Chemistry", 
                           "Biology", "Business Administration", "Economics", "Data Science"};
        courseCombo = new JComboBox<>(courses);
        panel.add(courseCombo, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Password (passwordXXX):"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);
        
        // Insert button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton insertButton = new JButton("Insert Student");
        insertButton.setBackground(new Color(100, 150, 200));
        insertButton.setForeground(Color.WHITE);
        insertButton.addActionListener(e -> insertStudent());
        panel.add(insertButton, gbc);
        
        return panel;
    }
    
    private JPanel createDeletePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.setBackground(new Color(200, 230, 240));
        
        // StudentID
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Student ID (SXXX):"), gbc);
        
        gbc.gridx = 1;
        deleteIdField = new JTextField(15);
        panel.add(deleteIdField, gbc);
        
        // Delete button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton deleteButton = new JButton("Delete Student");
        deleteButton.setBackground(new Color(200, 100, 100));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteStudent());
        panel.add(deleteButton, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Records"));
        panel.setBackground(new Color(173, 216, 230));
        
        // Create table model
        String[] columns = {"StudentID", "Name", "Address", "Phone Number", "Course", "Password"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setBackground(new Color(200, 230, 240));
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add refresh button
        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.setBackground(new Color(100, 150, 200));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> loadStudentData());
        panel.add(refreshButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private boolean isValidStudentId(String id) {
        return id.matches("^S\\d{3}$") && id.charAt(0) == 'S';
    }
    
    private boolean isValidName(String name) {
        return name.matches("^[A-Za-z]+(\\s+[A-Za-z]+)+$") && name.length() >= 5;
    }
    
    private boolean isValidAddress(String address) {
        return address.length() >= 10;
    }
    
    private boolean isValidPhone(String phone) {
        return phone.matches("^[6-9]\\d{9}$");
    }
    
    private boolean isValidPassword(String password) {
        return password.matches("(?i)^password\\d{3}$");
    }
    
    private void insertStudent() {
        String studentId = idField.getText().trim();
        String name = nameField.getText().trim();
        String address = addressArea.getText().trim();
        String phone = phoneField.getText().trim();
        String course = (String) courseCombo.getSelectedItem();
        String password = new String(passwordField.getPassword()).trim();

        // Validate all fields
        try {
            if (studentId.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                throw new IllegalArgumentException("All fields are required!");
            }

            if (!isValidStudentId(studentId)) {
                throw new IllegalArgumentException("Student ID must be S followed by 3 digits (e.g., S123)");
            }

            if (!isValidName(name)) {
                throw new IllegalArgumentException("Name must be at least 5 characters with 2+ words (e.g., John Smith)");
            }

            if (!isValidAddress(address)) {
                throw new IllegalArgumentException("Address must be at least 10 characters");
            }

            if (!isValidPhone(phone)) {
                throw new IllegalArgumentException("Phone must be 10 digits starting with 6-9 (e.g., 9876543210)");
            }

            if (!isValidPassword(password)) {
                throw new IllegalArgumentException("Password must be 'password' followed by 3 digits (e.g., Password123)");
            }

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                // Check if student ID already exists
                String checkSql = "SELECT COUNT(*) FROM student WHERE StudentID = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, studentId);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new IllegalArgumentException("Student ID already exists!");
                    }
                }

                String sql = "INSERT INTO student (StudentID, Name, Address, Phone_no, Course, Password) VALUES (?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, studentId);
                    pstmt.setString(2, name);
                    pstmt.setString(3, address);
                    pstmt.setString(4, phone);
                    pstmt.setString(5, course);
                    pstmt.setString(6, password);
                    
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        statusLabel.setText("Student inserted successfully!");
                        clearInsertFields();
                        loadStudentData();
                    } else {
                        throw new SQLException("Failed to insert student");
                    }
                }
            } catch (SQLException e) {
                throw new Exception("Database Error: " + e.getMessage());
            }
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteStudent() {
        String studentId = deleteIdField.getText().trim();
        
        try {
            if (studentId.isEmpty()) {
                throw new IllegalArgumentException("Student ID is required for deletion!");
            }
            
            if (!isValidStudentId(studentId)) {
                throw new IllegalArgumentException("Student ID must be S followed by 3 digits (e.g., S123)");
            }

            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete student with ID: " + studentId + "?", 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) return;
            
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                // Check if student exists first
                String checkSql = "SELECT COUNT(*) FROM student WHERE StudentID = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, studentId);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) == 0) {
                        throw new IllegalArgumentException("Student with ID " + studentId + " not found!");
                    }
                }

                // Check if student has any records in participates table
                String checkParticipationSql = "SELECT COUNT(*) FROM participates WHERE StudentID = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkParticipationSql)) {
                    checkStmt.setString(1, studentId);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        int option = JOptionPane.showConfirmDialog(this, 
                            "This student has event participations. Deleting will remove all participation records. Continue?",
                            "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        
                        if (option != JOptionPane.YES_OPTION) return;
                        
                        // Delete participation records first
                        String deleteParticipationSql = "DELETE FROM participates WHERE StudentID = ?";
                        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteParticipationSql)) {
                            deleteStmt.setString(1, studentId);
                            deleteStmt.executeUpdate();
                        }
                    }
                }

                String sql = "DELETE FROM student WHERE StudentID = ?";
                
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, studentId);
                    
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        statusLabel.setText("Student deleted successfully!");
                        deleteIdField.setText("");
                        loadStudentData();
                    } else {
                        throw new SQLException("Failed to delete student");
                    }
                }
            } catch (SQLException e) {
                throw new Exception("Database Error: " + e.getMessage());
            }
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadStudentData() {
        tableModel.setRowCount(0);
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM student ORDER BY StudentID";
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("StudentID"),
                        rs.getString("Name"),
                        rs.getString("Address"),
                        rs.getString("Phone_no"),
                        rs.getString("Course"),
                        "********" // Mask password for display
                    });
                }
            }
        } catch (SQLException e) {
            statusLabel.setText("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearInsertFields() {
        idField.setText("");
        nameField.setText("");
        addressArea.setText("");
        phoneField.setText("");
        courseCombo.setSelectedIndex(0);
        passwordField.setText("");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            StudentRecordManagement app = new StudentRecordManagement();
            app.setVisible(true);
        });
    }
}