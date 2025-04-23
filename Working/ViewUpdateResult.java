package Working;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewUpdateResult extends JFrame {
    private JTextField studentIdField;
    private JTextField resultIdField;
    private JTextField resultDetailsField;
    private JTextArea displayArea;
    private JButton viewButton;
    private JButton updateButton;
    private JButton clearButton;
    private JButton backButton;
    
    // Database credentials - replace with your actual values
    private static final String DB_URL = "jdbc:mysql://localhost:3306/university_db";
    private static final String USER = "root";
    private static final String PASS = "Pratiksql@2004";
    
    public ViewUpdateResult() {
        setTitle("View and Update Result");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Heading
        JLabel heading = new JLabel("View and Update Result", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(heading, BorderLayout.NORTH);
        
        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Student Result Information",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Arial", Font.BOLD, 14)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Student ID components
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel studentIdLabel = new JLabel("Student ID (SXXX):");
        studentIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(studentIdLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        studentIdField = new JTextField(10);
        studentIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(studentIdField, gbc);
        
        // Result ID components
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel resultIdLabel = new JLabel("Result ID (RXXX):");
        resultIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(resultIdLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        resultIdField = new JTextField(10);
        resultIdField.setFont(new Font("Arial", Font.PLAIN, 14));
        resultIdField.setEditable(false);  // Will be automatically filled from query
        inputPanel.add(resultIdField, gbc);
        
        // Result details components
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel resultDetailsLabel = new JLabel("Result Details:");
        resultDetailsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(resultDetailsLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.5;
        resultDetailsField = new JTextField(10);
        resultDetailsField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(resultDetailsField, gbc);
        
        // Button panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        viewButton = new JButton("View Result");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewButton.setPreferredSize(new Dimension(150, 35));
        viewButton.setBackground(new Color(60, 120, 180));
        viewButton.setForeground(Color.BLACK);
        buttonPanel.add(viewButton);
        
        updateButton = new JButton("Update Result");
        updateButton.setFont(new Font("Arial", Font.PLAIN, 14));
        updateButton.setPreferredSize(new Dimension(150, 35));
        updateButton.setBackground(new Color(60, 120, 180));
        updateButton.setForeground(Color.BLACK);
        updateButton.setEnabled(false);  // Initially disabled until view is done
        buttonPanel.add(updateButton);
        
        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearButton.setPreferredSize(new Dimension(150, 35));
        clearButton.setBackground(new Color(180, 60, 60));
        clearButton.setForeground(Color.BLACK);
        buttonPanel.add(clearButton);
        
        inputPanel.add(buttonPanel, gbc);
        
        // Display area
        displayArea = new JTextArea();
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        displayArea.setEditable(false);
        displayArea.setMargin(new Insets(10, 10, 10, 10));
        displayArea.setBackground(new Color(240, 240, 240));
        
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        "Result Information",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Arial", Font.BOLD, 14)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Back button at the bottom
        backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setPreferredSize(new Dimension(200, 35));
        backButton.setBackground(new Color(100, 100, 100));
        backButton.setForeground(Color.WHITE);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(backButton);
        
        // Add all components to main panel
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Add event listeners
        viewButton.addActionListener(e -> viewResult());
        updateButton.addActionListener(e -> updateResult());
        clearButton.addActionListener(e -> clearFields());
        backButton.addActionListener(e -> dispose());
        
        // Add main panel to frame
        add(mainPanel);
        setVisible(true);
    }
    
    private void viewResult() {
        String studentId = studentIdField.getText().trim();
        
        // Validate student ID format (SXXX)
        if (!isValidStudentId(studentId)) {
            JOptionPane.showMessageDialog(this, 
                "Invalid Student ID format. Must be in the format 'SXXX' where X is a digit.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // First query to get ResultID from view_result
            String viewQuery = "SELECT * FROM view_result WHERE StudentID = ?";
            PreparedStatement viewStmt = conn.prepareStatement(viewQuery);
            viewStmt.setString(1, studentId);
            ResultSet viewRs = viewStmt.executeQuery();
            
            if (viewRs.next()) {
                String resultId = viewRs.getString("ResultID");
                resultIdField.setText(resultId);
                
                // Second query to get result details
                String resultQuery = "SELECT * FROM result WHERE ResultID = ?";
                PreparedStatement resultStmt = conn.prepareStatement(resultQuery);
                resultStmt.setString(1, resultId);
                ResultSet resultRs = resultStmt.executeQuery();
                
                if (resultRs.next()) {
                    String resultDetails = resultRs.getString("Result_details");
                    resultDetailsField.setText(resultDetails);
                    
                    // Display the result information
                    displayArea.setText("");
                    displayArea.append("Student ID: " + studentId + "\n");
                    displayArea.append("Result ID: " + resultId + "\n");
                    displayArea.append("Result Details: " + resultDetails + "\n");
                    
                    // Enable update button
                    updateButton.setEnabled(true);
                } else {
                    displayArea.setText("No result details found for ResultID: " + resultId);
                }
            } else {
                displayArea.setText("No result found for StudentID: " + studentId);
                clearFields();
                updateButton.setEnabled(false);
            }
        } catch (SQLException e) {
            displayArea.setText("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateResult() {
        String resultId = resultIdField.getText().trim();
        String newResultDetails = resultDetailsField.getText().trim();
        
        // Validate Result ID format (RXXX)
        if (!isValidResultId(resultId)) {
            JOptionPane.showMessageDialog(this, 
                "Invalid Result ID format. Must be in the format 'RXXX' where X is a digit.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (resultId.isEmpty() || newResultDetails.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Result ID and Result Details are required.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Validate result details is a number
            double resultValue = Double.parseDouble(newResultDetails);
            
            // Validate the score range (assuming scores are between 0 and 10)
            if (resultValue < 0 || resultValue > 10) {
                JOptionPane.showMessageDialog(this,
                    "Result details must be between 0 and 10.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Confirm update
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to update the result to " + resultValue + "?",
                "Confirm Update",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    String updateQuery = "UPDATE result SET Result_details = ? WHERE ResultID = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setDouble(1, resultValue);
                    updateStmt.setString(2, resultId);
                    
                    int rowsAffected = updateStmt.executeUpdate();
                    
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this,
                            "Result updated successfully!",
                            "Update Success",
                            JOptionPane.INFORMATION_MESSAGE);
                            
                        // Refresh the view to show updated data
                        viewResult();
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Failed to update result. No matching record found.",
                            "Update Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this,
                        "Database Error: " + e.getMessage(),
                        "Update Error",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Result details must be a valid number.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFields() {
        studentIdField.setText("");
        resultIdField.setText("");
        resultDetailsField.setText("");
        updateButton.setEnabled(false);
    }
    
    // Validate Student ID format (SXXX)
    private boolean isValidStudentId(String studentId) {
        return studentId != null && studentId.matches("S[0-9]{3}");
    }
    
    // Validate Result ID format (RXXX)
    private boolean isValidResultId(String resultId) {
        return resultId != null && resultId.matches("R[0-9]{3}");
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(ViewUpdateResult::new);
    }
}