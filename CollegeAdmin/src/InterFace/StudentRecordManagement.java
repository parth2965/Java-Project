package InterFace;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentRecordManagement extends JFrame {
    private JTextField idField, nameField, phoneField, courseField;
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

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel headerLabel = new JLabel("Student Record Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        
        // Operations panel (Insert and Delete)
        JPanel operationsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        
        // Insert panel
        JPanel insertPanel = createInsertPanel();
        insertPanel.setBorder(BorderFactory.createTitledBorder("Insert Student"));
        
        // Delete panel
        JPanel deletePanel = createDeletePanel();
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Student"));
        
        operationsPanel.add(insertPanel);
        operationsPanel.add(deletePanel);
        
        mainPanel.add(operationsPanel, BorderLayout.CENTER);
        
        // Status label for feedback
        statusLabel = new JLabel("Ready", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
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
        
        // StudentID
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Student ID:"), gbc);
        
        gbc.gridx = 1;
        idField = new JTextField(15);
        panel.add(idField, gbc);
        
        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        
        gbc.gridx = 1;
        nameField = new JTextField(15);
        panel.add(nameField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Address:"), gbc);
        
        gbc.gridx = 1;
        addressArea = new JTextArea(3, 15);
        addressArea.setLineWrap(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        panel.add(addressScroll, gbc);
        
        // Phone
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Phone Number:"), gbc);
        
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
        courseField = new JTextField(15);
        JComboBox<String> courseCombo = new JComboBox<>(courses);
        courseCombo.addActionListener(e -> courseField.setText((String) courseCombo.getSelectedItem()));
        panel.add(courseCombo, gbc);
        
        gbc.gridy = 5;
        panel.add(courseField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);
        
        // Insert button
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton insertButton = new JButton("Insert Student");
        insertButton.addActionListener(e -> insertStudent());
        panel.add(insertButton, gbc);
        
        return panel;
    }
    
    private JPanel createDeletePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // StudentID
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Student ID:"), gbc);
        
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
        deleteButton.addActionListener(e -> deleteStudent());
        panel.add(deleteButton, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Student Records"));
        
        // Create table model
        String[] columns = {"StudentID", "Name", "Address", "Phone Number", "Course", "Password"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add refresh button
        JButton refreshButton = new JButton("Refresh Data");
        refreshButton.addActionListener(e -> loadStudentData());
        panel.add(refreshButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void insertStudent() {
        String studentId = idField.getText().trim();
        String name = nameField.getText().trim();
        String address = addressArea.getText().trim();
        String phone = phoneField.getText().trim();
        String course = courseField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validate input
        if (studentId.isEmpty() || name.isEmpty() || address.isEmpty() || phone.isEmpty() || course.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Error: All fields are required!");
            return;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
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
                    loadStudentData(); // Refresh table
                } else {
                    statusLabel.setText("Error: Failed to insert student!");
                }
            }
        } catch (SQLException e) {
            statusLabel.setText("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void deleteStudent() {
        String studentId = deleteIdField.getText().trim();
        
        if (studentId.isEmpty()) {
            statusLabel.setText("Error: Student ID is required for deletion!");
            return;
        }
        
        // Ask for confirmation
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete student with ID: " + studentId + "?", 
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "DELETE FROM student WHERE StudentID = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, studentId);
                
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    statusLabel.setText("Student deleted successfully!");
                    deleteIdField.setText("");
                    loadStudentData(); // Refresh table
                } else {
                    statusLabel.setText("Error: Student with ID " + studentId + " not found!");
                }
            }
        } catch (SQLException e) {
            statusLabel.setText("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadStudentData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM student";
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    String id = rs.getString("StudentID");
                    String name = rs.getString("Name");
                    String address = rs.getString("Address");
                    String phone = rs.getString("Phone_no");
                    String course = rs.getString("Course");
                    String password = rs.getString("Password");
                    
                    tableModel.addRow(new Object[]{id, name, address, phone, course, password});
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
        courseField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(StudentRecordManagement::new);
    }
}