package InterFace;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentRecordManagement extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/university_db";
    private static final String DB_USER = "root"; // Replace with your database username
    private static final String DB_PASSWORD = "Pratiksql@2004"; // Replace with your database password

    public StudentRecordManagement() {
        setTitle("Student Record Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new CardLayout()); // Use CardLayout to switch between panels
        JPanel insertPanel = createInsertStudentPanel();
        JPanel deletePanel = createDeleteStudentPanel();

        mainPanel.add(insertPanel, "insert");
        mainPanel.add(deletePanel, "delete");

        JPanel buttonPanel = new JPanel();
        JButton insertButton = new JButton("Insert Student");
        JButton deleteButton = new JButton("Delete Student");

        insertButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) (mainPanel.getLayout());
            cl.show(mainPanel, "insert");
        });

        deleteButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) (mainPanel.getLayout());
            cl.show(mainPanel, "delete");
            populateDeleteStudentComboBox((JPanel) mainPanel.getComponent(1)); // Refresh IDs
        });

        buttonPanel.add(insertButton);
        buttonPanel.add(deleteButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    private JPanel createInsertStudentPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField studentID = new JTextField(10);
        JTextField name = new JTextField(20);
        JTextArea address = new JTextArea(3, 20);
        JScrollPane addressScrollPane = new JScrollPane(address);
        JTextField phoneNo = new JTextField(15);
        JComboBox<String> courseComboBox = new JComboBox<>(getCoursesFromDatabase());
        JPasswordField password = new JPasswordField(20);
        JButton insertButton = new JButton("Insert");

        panel.add(new JLabel("Student ID:"));
        panel.add(studentID);
        panel.add(new JLabel("Name:"));
        panel.add(name);
        panel.add(new JLabel("Address:"));
        panel.add(addressScrollPane);
        panel.add(new JLabel("Phone No:"));
        panel.add(phoneNo);
        panel.add(new JLabel("Course:"));
        panel.add(courseComboBox);
        panel.add(new JLabel("Password:"));
        panel.add(password);
        panel.add(new JLabel("")); // Placeholder for alignment
        panel.add(insertButton);

        insertButton.addActionListener(e -> {
            String studentIDValue = studentID.getText().trim();
            String nameValue = name.getText().trim();
            String addressValue = address.getText().trim();
            String phoneNoValue = phoneNo.getText().trim();
            String courseValue = (String) courseComboBox.getSelectedItem();
            String passwordValue = new String(password.getPassword());

            insertStudentToDatabase(studentIDValue, nameValue, addressValue, phoneNoValue, courseValue, passwordValue);
        });

        return panel;
    }

    private JPanel createDeleteStudentPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JComboBox<String> studentIDComboBox = new JComboBox<>();
        JButton deleteButton = new JButton("Delete");

        panel.add(new JLabel("Select Student ID to Delete:"));
        panel.add(studentIDComboBox);
        panel.add(deleteButton);

        deleteButton.addActionListener(e -> {
            String studentIDToDelete = (String) studentIDComboBox.getSelectedItem();
            if (studentIDToDelete != null) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete student with ID " + studentIDToDelete + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteStudentFromDatabase(studentIDToDelete);
                    populateDeleteStudentComboBox(panel); // Refresh the combo box after deletion
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a Student ID to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private String[] getCoursesFromDatabase() {
        List<String> courses = new ArrayList<>();
        String sql = "SELECT DISTINCT Course FROM student";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                courses.add(rs.getString("Course"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return courses.toArray(new String[0]);
    }

    private void insertStudentToDatabase(String studentID, String name, String address, String phoneNo, String course, String password) {
        String sql = "INSERT INTO student (StudentID, Name, Address, Phone_no, Course, Password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentID);
            pstmt.setString(2, name);
            pstmt.setString(3, address);
            pstmt.setString(4, phoneNo);
            pstmt.setString(5, course);
            pstmt.setString(6, password);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Student record inserted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Optionally clear the input fields after successful insertion
                clearInsertFields((JPanel) ((CardLayout) getContentPane().getLayout()).getLayoutComponent(getContentPane(), "insert"));
            } else {
                JOptionPane.showMessageDialog(this, "Failed to insert student record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void deleteStudentFromDatabase(String studentID) {
        String sql = "DELETE FROM student WHERE StudentID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Student record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Student with ID " + studentID + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void populateDeleteStudentComboBox(JPanel deletePanel) {
        JComboBox<String> studentIDComboBox = null;
        for (Component comp : deletePanel.getComponents()) {
            if (comp instanceof JComboBox) {
                studentIDComboBox = (JComboBox<String>) comp;
                studentIDComboBox.removeAllItems();
                break;
            }
        }

        if (studentIDComboBox != null) {
            List<String> studentIDs = getStudentIDsFromDatabaseList();
            for (String id : studentIDs) {
                studentIDComboBox.addItem(id);
            }
        }
    }

    private List<String> getStudentIDsFromDatabaseList() {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT StudentID FROM student";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ids.add(rs.getString("StudentID"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return ids;
    }

    private void clearInsertFields(JPanel insertPanel) {
        for (Component comp : insertPanel.getComponents()) {
            if (comp instanceof JTextField) {
                ((JTextField) comp).setText("");
            } else if (comp instanceof JTextArea) {
                ((JTextArea) comp).setText("");
            } else if (comp instanceof JPasswordField) {
                ((JPasswordField) comp).setText("");
            } else if (comp instanceof JComboBox) {
                ((JComboBox) comp).setSelectedIndex(0); // Reset to the first item
            }
        }
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