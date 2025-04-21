package InterFace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class FacultyDashboard extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JTabbedPane tabbedPane;
    private Map<String, JTextField> profileFields = new HashMap<>();
    
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/university_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Pratiksql@2004";
    
    // Current faculty info
    private String facultyID;
    private String facultyName;
    private String courseName;
    
    public FacultyDashboard() {
        initComponents();
        loadFacultyData();
        loadCourseData();
        loadStudentData();
    }
    
    private void initComponents() {
        // Set up frame properties
        setTitle("Faculty Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255)); // Light blue background
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 144, 255)); // Dodger blue
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Welcome message
        welcomeLabel = new JLabel("Welcome, Professor");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        // Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(240, 248, 255));
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> logout());
        headerPanel.add(logoutBtn, BorderLayout.EAST);
        
        // Add header to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create tabbed pane for different sections
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Create tabs
        tabbedPane.addTab("Profile", createProfilePanel());
        tabbedPane.addTab("Courses", createCoursesPanel());
        tabbedPane.addTab("Students", createStudentsPanel());
        tabbedPane.addTab("Schedule", createSchedulePanel());
        
        // Add tabbed pane to main panel
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Create footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(240, 248, 255));
        footerPanel.add(new JLabel("© 2024 College Administration System. All rights reserved."));
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Set content pane
        setContentPane(mainPanel);
    }
    
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        formPanel.setBackground(Color.WHITE);
        
        formPanel.add(new JLabel("Faculty ID:"));
        JTextField idField = new JTextField();
        idField.setEditable(false);
        formPanel.add(idField);
        profileFields.put("id", idField);
        
        formPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        nameField.setEditable(false);
        formPanel.add(nameField);
        profileFields.put("name", nameField);
        
        formPanel.add(new JLabel("Address:"));
        JTextField addressField = new JTextField();
        formPanel.add(addressField);
        profileFields.put("address", addressField);
        
        formPanel.add(new JLabel("Phone Number:"));
        JTextField phoneField = new JTextField();
        formPanel.add(phoneField);
        profileFields.put("phone", phoneField);
        
        formPanel.add(new JLabel("Course Expertise:"));
        JTextField courseField = new JTextField();
        courseField.setEditable(false);
        formPanel.add(courseField);
        profileFields.put("course", courseField);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setBackground(new Color(59, 130, 246));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> updateFacultyProfile());
        buttonPanel.add(saveBtn);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add a label showing current course
        JLabel courseLabel = new JLabel("Your Course: ");
        courseLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(courseLabel, BorderLayout.NORTH);
        
        // Table for courses with placeholder data
        // In a real application, this would be populated from a database
        String[] columnNames = {"Course Code", "Course Name", "Schedule", "Room", "Students Enrolled"};
        
        // Default empty table model
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable coursesTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(Color.WHITE);
        
        JButton addMaterialBtn = new JButton("Add Course Material");
        addMaterialBtn.setBackground(new Color(59, 130, 246));
        addMaterialBtn.setForeground(Color.WHITE);
        addMaterialBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Course material upload functionality would be implemented here.", 
                "Course Materials", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        controlPanel.add(addMaterialBtn);
        
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Table for students
        String[] columnNames = {"Student ID", "Name", "Course", "Grade"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable studentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(Color.WHITE);
        
        JButton gradesBtn = new JButton("Enter Grades");
        gradesBtn.setBackground(new Color(59, 130, 246));
        gradesBtn.setForeground(Color.WHITE);
        gradesBtn.addActionListener(e -> {
            int selectedRow = studentsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String studentID = (String) studentsTable.getValueAt(selectedRow, 0);
                String studentName = (String) studentsTable.getValueAt(selectedRow, 1);
                
                String grade = JOptionPane.showInputDialog(this, 
                        "Enter grade for " + studentName + " (A, B, C, D, F):", 
                        studentsTable.getValueAt(selectedRow, 3));
                
                if (grade != null && !grade.isEmpty()) {
                    studentsTable.setValueAt(grade, selectedRow, 3);
                    // In a real application, this would update the database
                    JOptionPane.showMessageDialog(this, 
                            "Grade updated for " + studentName, 
                            "Grade Update", 
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Please select a student first.", 
                        "No Selection", 
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        controlPanel.add(gradesBtn);
        
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Week days as column headers
        String[] days = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        
        // Time slots and schedule data
        String[] timeSlots = {"08:00-09:30", "09:45-11:15", "11:30-13:00", "14:00-15:30", "15:45-17:15"};
        Object[][] scheduleData = new Object[timeSlots.length][days.length];
        
        // Fill in the time slots
        for (int i = 0; i < timeSlots.length; i++) {
            scheduleData[i][0] = timeSlots[i];
            for (int j = 1; j < days.length; j++) {
                scheduleData[i][j] = "";  // Empty cells initially
            }
        }
        
        // This would be populated based on the faculty's actual schedule
        // For now using example data
        scheduleData[0][1] = "Lecture (Room 201)";  // Monday 8:00-9:30
        scheduleData[0][3] = "Lecture (Room 201)";  // Wednesday 8:00-9:30
        scheduleData[1][2] = "Office Hours";        // Tuesday 9:45-11:15
        scheduleData[1][4] = "Office Hours";        // Thursday 9:45-11:15
        scheduleData[3][2] = "Lab Session (301)";   // Tuesday 14:00-15:30
        scheduleData[3][4] = "Lab Session (301)";   // Thursday 14:00-15:30
        
        JTable scheduleTable = new JTable(scheduleData, days);
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.setBackground(Color.WHITE);
        
        JButton exportBtn = new JButton("Export Schedule");
        exportBtn.setBackground(new Color(59, 130, 246));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                    "Schedule export functionality would be implemented here.", 
                    "Export Schedule", 
                    JOptionPane.INFORMATION_MESSAGE);
        });
        controlPanel.add(exportBtn);
        
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadFacultyData() {
        try {
            facultyID = Session.getUserID();
            String query = "SELECT * FROM faculty WHERE FacultyID = ?";
            
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setString(1, facultyID);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Store faculty information
                        facultyName = rs.getString("Name");
                        courseName = rs.getString("Course");
                        
                        // Update welcome message - extract the last name
                        String[] nameParts = facultyName.split(" ");
                        String lastName = nameParts[nameParts.length - 1];
                        welcomeLabel.setText("Welcome, Prof. " + lastName);
                        
                        // Update profile fields
                        profileFields.get("id").setText(rs.getString("FacultyID"));
                        profileFields.get("name").setText(facultyName);
                        profileFields.get("address").setText(rs.getString("Address"));
                        profileFields.get("phone").setText(rs.getString("Phone_no"));
                        profileFields.get("course").setText(courseName);
                        
                        // Update course label in courses tab
                        JLabel courseLabel = (JLabel) ((JPanel) tabbedPane.getComponentAt(1)).getComponent(0);
                        courseLabel.setText("Your Course: " + courseName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "Error loading faculty data: " + e.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "Error: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadCourseData() {
        // This would typically fetch course data from the database
        // For now, using the faculty's course to populate example data
        try {
            if (courseName != null && !courseName.isEmpty()) {
                // Get the table model from the courses tab
                JTable courseTable = (JTable) ((JScrollPane) ((JPanel) tabbedPane.getComponentAt(1)).getComponent(1)).getViewport().getView();
                DefaultTableModel model = (DefaultTableModel) courseTable.getModel();
                
                // Clear existing rows
                model.setRowCount(0);
                
                // Add a row based on the faculty's course
                // In a real app, you would fetch this from a courses table
                String courseCode = generateCourseCode(courseName);
                String schedule = generateRandomSchedule();
                String room = "Room " + (100 + new Random().nextInt(300));
                String studentsEnrolled = String.valueOf(15 + new Random().nextInt(30));
                
                model.addRow(new Object[] {
                    courseCode,
                    courseName,
                    schedule,
                    room,
                    studentsEnrolled
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "Error loading course data: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadStudentData() {
        // In a real application, this would query the database for students enrolled in the faculty's course
        // For this example, we'll generate some placeholder data based on the faculty's course
        try {
            if (courseName != null && !courseName.isEmpty()) {
                // Get the table model from the students tab
                JTable studentTable = (JTable) ((JScrollPane) ((JPanel) tabbedPane.getComponentAt(2)).getComponent(0)).getViewport().getView();
                DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
                
                // Clear existing rows
                model.setRowCount(0);
                
                // Add sample students for this course
                // In a real app, fetch this from the database
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM student LIMIT 5")) {
                    
                    String[] grades = {"A", "B+", "B", "C+", "C", "D", "F"};
                    Random random = new Random();
                    
                    while (rs.next()) {
                        String studentId = rs.getString("StudentID");
                        String studentName = rs.getString("Name");
                        String grade = grades[random.nextInt(grades.length)];
                        
                        model.addRow(new Object[] {
                            studentId,
                            studentName,
                            courseName,
                            grade
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "Error loading student data: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateFacultyProfile() {
        try {
            String address = profileFields.get("address").getText();
            String phone = profileFields.get("phone").getText();
            
            if (address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "Please fill in all fields", 
                        "Validation Error", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String query = "UPDATE faculty SET Address = ?, Phone_no = ? WHERE FacultyID = ?";
            
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setString(1, address);
                pstmt.setString(2, phone);
                pstmt.setString(3, facultyID);
                
                int rowsAffected = pstmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, 
                            "Profile updated successfully", 
                            "Update Successful", 
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                            "No changes were made", 
                            "Update Failed", 
                            JOptionPane.WARNING_MESSAGE);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                    "Database error: " + e.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String generateCourseCode(String courseName) {
        // Generate a simple course code based on the course name
        // E.g., "Programming" -> "PRG101"
        if (courseName == null || courseName.isEmpty()) {
            return "COURSE101";
        }
        
        String prefix = "";
        String[] words = courseName.split(" ");
        
        for (String word : words) {
            if (!word.isEmpty()) {
                prefix += word.substring(0, Math.min(word.length(), 1)).toUpperCase();
            }
        }
        
        if (prefix.isEmpty()) {
            prefix = courseName.substring(0, Math.min(courseName.length(), 3)).toUpperCase();
        }
        
        // Pad to at least 3 characters
        while (prefix.length() < 3) {
            prefix += "X";
        }
        
        // Add a random number
        return prefix + (100 + new Random().nextInt(900));
    }
    
    private String generateRandomSchedule() {
        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};
        int day1 = new Random().nextInt(days.length);
        int day2;
        do {
            day2 = new Random().nextInt(days.length);
        } while (day2 == day1);
        
        int hour = 8 + new Random().nextInt(8); // 8 AM to 4 PM
        int minute = new Random().nextInt(4) * 15; // 0, 15, 30, or 45 minutes
        
        return days[day1] + "/" + days[day2] + " " + 
               String.format("%02d:%02d", hour, minute) + "-" + 
               String.format("%02d:%02d", hour + 1, minute);
    }
    
    private void logout() {
        // Clear session
        Session.clear();
        
        // Close this frame
        this.dispose();
        
        // Open login screen
        SwingUtilities.invokeLater(CollegeAdminLogin::createAndShowGUI);
    }
}