package Working;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDashboard extends JFrame {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/university_db";
    private static final String DB_USER = "root";  // Change to your MySQL username
    private static final String DB_PASSWORD = "Pratiksql@2004";  // Change to your MySQL password

    public StudentDashboard() {
        setTitle("Student Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 700);
        setLocationRelativeTo(null);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Get student information from Session
        String studentID = Session.getUserId();
        
     // Custom panel with background image
        JPanel outerPanel = new JPanel(new GridBagLayout()) {
            private Image backgroundImage = new ImageIcon(getClass().getResource("/resources/college_bg.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        
        // Welcome message with student ID
        JLabel heading = new JLabel("Student Dashboard - " + studentID, SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 32));
        mainPanel.add(heading, BorderLayout.NORTH);

        // Button panel with vertical BoxLayout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 400, 40, 400)); // tighter width
        buttonPanel.setOpaque(false);

        // Create and style buttons
        Font buttonFont = new Font("Arial", Font.PLAIN, 18);

        JButton viewResultBtn = new JButton("View Result");
        JButton viewEventsBtn = new JButton("View Events");
        JButton updateDetailsBtn = new JButton("Update Phone Number and Address");
        JButton logoutBtn = new JButton("Logout");

        JButton[] buttons = {viewResultBtn, viewEventsBtn, updateDetailsBtn, logoutBtn};

        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(400, 45)); // smaller button size
            btn.setPreferredSize(new Dimension(400, 45));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(70, 130, 180));
            btn.setForeground(Color.black);
            buttonPanel.add(btn);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add action listeners
        viewResultBtn.addActionListener(e -> {
            try {
                displayStudentResult(studentID);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                    StudentDashboard.this,
                    "Error retrieving results: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        });

        viewEventsBtn.addActionListener(e -> {
            try {
                displayStudentEvents(studentID);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                    StudentDashboard.this,
                    "Error retrieving events: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        });

        updateDetailsBtn.addActionListener(e -> {
            try {
                updateContactInformation(studentID);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                    StudentDashboard.this,
                    "Error updating contact information: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
                );
                ex.printStackTrace();
            }
        });
        
        logoutBtn.addActionListener(e -> {
            // Clear session data
            Session.clearSession();
            
            // Close current window
            dispose();
            
            // Open login screen
            SwingUtilities.invokeLater(() -> {
                try {
                    new CollegeAdminLogin();
					// Assuming CollegeAdminLogin has a static method createAndShowGUI
                    CollegeAdminLogin.main(new String[]{});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

        add(mainPanel);
        setVisible(true);
    }
    
    /**
     * Fetches and displays the student's result from the database
     * 
     * @param studentID the ID of the student
     * @throws SQLException if a database error occurs
     */
    private void displayStudentResult(String studentID) throws SQLException {
        String resultID = "";
        double resultDetails = 0.0;
        
        // First query: Get ResultID from view_result table
        String query1 = "SELECT ResultID FROM view_result WHERE StudentID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query1)) {
            
            pstmt.setString(1, studentID);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    resultID = rs.getString("ResultID");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No result found for student ID: " + studentID,
                            "Result Not Found",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }
        
        // Second query: Get result details using ResultID
        String query2 = "SELECT Result_details FROM result WHERE ResultID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query2)) {
            
            pstmt.setString(1, resultID);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    resultDetails = rs.getDouble("Result_details");
                    
                    // Create a formatted result message
                    String resultMessage = String.format(
                        "Student ID: %s\nResult ID: %s\nResult: %.1f\n\n",
                        studentID, resultID, resultDetails);
                    
                    // Determine grade based on result
                    String grade = "";
                    if (resultDetails >= 9.0) {
                        grade = "A+";
                    } else if (resultDetails >= 8.0) {
                        grade = "A";
                    } else if (resultDetails >= 7.0) {
                        grade = "B+";
                    } else if (resultDetails >= 6.0) {
                        grade = "B";
                    } else if (resultDetails >= 5.0) {
                        grade = "C";
                    } else {
                        grade = "F";
                    }
                    
                    resultMessage += "Grade: " + grade;
                    
                    JOptionPane.showMessageDialog(this,
                            resultMessage,
                            "Academic Results",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
    
    /**
     * Fetches and displays the events the student is participating in
     * 
     * @param studentID the ID of the student
     * @throws SQLException if a database error occurs
     */
    private void displayStudentEvents(String studentID) throws SQLException {
        // First, get event IDs the student is participating in
        List<String> eventIDs = new ArrayList<>();
        
        String query1 = "SELECT EventID FROM participates WHERE StudentID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query1)) {
            
            pstmt.setString(1, studentID);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    eventIDs.add(rs.getString("EventID"));
                }
            }
        }
        
        if (eventIDs.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "You are not participating in any events.",
                    "No Events",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Build the IN clause for the SQL query
        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < eventIDs.size(); i++) {
            inClause.append("?");
            if (i < eventIDs.size() - 1) {
                inClause.append(",");
            }
        }
        
        // Second, get event details
        String query2 = "SELECT * FROM event WHERE EventID IN (" + inClause.toString() + ")";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query2)) {
            
            // Set parameters for the IN clause
            for (int i = 0; i < eventIDs.size(); i++) {
                pstmt.setString(i + 1, eventIDs.get(i));
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                // Create a table model for displaying events
                DefaultTableModel model = new DefaultTableModel();
                model.addColumn("Event ID");
                model.addColumn("Event Name");
                model.addColumn("Description");
                model.addColumn("Date");
                model.addColumn("Venue");
                
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("EventID"),
                        rs.getString("Event_name"),
                        rs.getString("Description"),
                        rs.getString("Date_of_event"),
                        rs.getString("Venue")
                    });
                }
                
                // Create and display a JTable with the events
                JTable table = new JTable(model);
                table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
                table.getColumnModel().getColumn(0).setPreferredWidth(70);
                table.getColumnModel().getColumn(1).setPreferredWidth(150);
                table.getColumnModel().getColumn(2).setPreferredWidth(250);
                table.getColumnModel().getColumn(3).setPreferredWidth(100);
                table.getColumnModel().getColumn(4).setPreferredWidth(150);
                
                JScrollPane scrollPane = new JScrollPane(table);
                scrollPane.setPreferredSize(new Dimension(800, 200));
                
                JOptionPane.showMessageDialog(this,
                        scrollPane,
                        "Your Events",
                        JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
    
    /**
     * Updates the contact information (phone and address) for a student
     * 
     * @param studentID the ID of the student
     * @throws SQLException if a database error occurs
     */
    private void updateContactInformation(String studentID) throws SQLException {
        // First, fetch current contact information to display in form
        String currentPhone = "";
        String currentAddress = "";
        
        String fetchQuery = "SELECT Phone_no, Address FROM student WHERE StudentID = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(fetchQuery)) {
            
            pstmt.setString(1, studentID);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    currentPhone = rs.getString("Phone_no");
                    currentAddress = rs.getString("Address");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Student record not found: " + studentID,
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        
        // Create a form with current values pre-filled
        JPanel updatePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField phoneField = new JTextField(currentPhone);
        JTextField addressField = new JTextField(currentAddress);

        updatePanel.add(new JLabel("Phone Number:"));
        updatePanel.add(phoneField);
        updatePanel.add(new JLabel("Address:"));
        updatePanel.add(addressField);

        int result = JOptionPane.showConfirmDialog(
                this,
                updatePanel,
                "Update Contact Information",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String newPhone = phoneField.getText().trim();
            String newAddress = addressField.getText().trim();
            
            // Validate phone number (should be a valid format)
            if (!isValidPhoneNumber(newPhone)) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid phone number (10 digits).",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validate address (minimum 4 characters)
            if (newAddress.length() < 4) {
                JOptionPane.showMessageDialog(this,
                        "Address must be at least 4 characters long.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Perform the update
            String updateQuery = "UPDATE student SET Phone_no = ?, Address = ? WHERE StudentID = ?";
            
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                
                pstmt.setString(1, newPhone);
                pstmt.setString(2, newAddress);
                pstmt.setString(3, studentID);
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Contact information updated successfully!\n\n" +
                            "Phone: " + newPhone + "\n" +
                            "Address: " + newAddress,
                            "Update Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to update contact information.",
                            "Update Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Validates if the provided phone number is in the correct format
     * 
     * @param phoneNumber the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Phone number should be 10 digits
        String phoneRegex = "^[0-9]{10}$";
        return phoneNumber != null && phoneNumber.matches(phoneRegex);
    }

    /**
     * Validates if the provided password meets security requirements
     * 
     * @param password the password to validate
     * @return true if the password is valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        // Password must be at least 8 characters long
        // Must contain at least one digit, one uppercase letter, one lowercase letter
        // and one special character
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(StudentDashboard::new);
    }
}