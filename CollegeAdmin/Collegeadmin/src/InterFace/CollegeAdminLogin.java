package InterFace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CollegeAdminLogin {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/university_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Pratiksql@2004";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CollegeAdminLogin::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("College Administration System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 700);
        frame.setLocationRelativeTo(null);

        // Custom panel with background image
        JPanel outerPanel = new JPanel(new GridBagLayout()) {
            private Image backgroundImage = new ImageIcon(getClass().getResource("/resources/college_bg.jpg")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formPanel.setBackground(Color.WHITE);

        // Logo
        ImageIcon logoIcon = new ImageIcon(CollegeAdminLogin.class.getResource("/resources/symbiosis_logo.png"));
        Image logoImg = logoIcon.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImg));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(logoLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel title = new JLabel("College Administration System");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to access your portal");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(title);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(subtitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Institute Dropdown
        JLabel instituteLabel = new JLabel("Select Department");
        instituteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JComboBox<String> instituteCombo = new JComboBox<>(new String[]{
                "CSE", "AIML", "ENTC", "RNA", "CIVIL", "MECH"
        });
        instituteCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        instituteCombo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // User Type
        JLabel userTypeLabel = new JLabel("User Type");
        userTypeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JRadioButton facultyBtn = new JRadioButton("Faculty");
        JRadioButton studentBtn = new JRadioButton("Student");
        facultyBtn.setBackground(Color.WHITE);
        studentBtn.setBackground(Color.WHITE);
        studentBtn.setSelected(true);

        ButtonGroup userTypeGroup = new ButtonGroup();
        userTypeGroup.add(facultyBtn);
        userTypeGroup.add(studentBtn);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        radioPanel.setBackground(Color.WHITE);
        radioPanel.add(facultyBtn);
        radioPanel.add(studentBtn);
        radioPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username and Password
        JTextField username = new JTextField();
        username.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        username.setBorder(BorderFactory.createTitledBorder("Username (StudentID/FacultyID)"));

        JPasswordField password = new JPasswordField();
        password.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        password.setBorder(BorderFactory.createTitledBorder("Password"));

        // Remember and forgot
        JPanel rememberPanel = new JPanel(new BorderLayout());
        rememberPanel.setBackground(Color.WHITE);
        JCheckBox remember = new JCheckBox("Remember me");
        remember.setBackground(Color.WHITE);
        JLabel forgot = new JLabel("<HTML><U>Forgot password</U></HTML>");
        forgot.setForeground(Color.BLUE);
        forgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rememberPanel.add(remember, BorderLayout.WEST);
        rememberPanel.add(forgot, BorderLayout.EAST);
        rememberPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        // Status message
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Sign in
        JButton signIn = new JButton("Sign In");
        signIn.setBackground(new Color(59, 130, 246));
        signIn.setForeground(Color.WHITE);
        signIn.setFocusPainted(false);
        signIn.setFont(new Font("Arial", Font.BOLD, 14));
        signIn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signIn.setMaximumSize(new Dimension(200, 40));

        signIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = username.getText().trim();
                String pwd = new String(password.getPassword());
                String userType = facultyBtn.isSelected() ? "Faculty" : "Student";

                if (userID.isEmpty() || pwd.isEmpty()) {
                    statusLabel.setText("Please enter both username and password");
                    return;
                }

                boolean authenticated = authenticateUser(userID, pwd, userType);

                if (authenticated) {
                    statusLabel.setText("");
                    Session.setUser(userID, userType, pwd);
                    System.out.println("User logged in: " + userID + " (" + userType + ")");

                    JOptionPane.showMessageDialog(frame,
                            "Login successful! Welcome to the College Administration System.",
                            "Authentication Successful",
                            JOptionPane.INFORMATION_MESSAGE);

                    frame.dispose();

                    if (userType.equals("Student")) {
                        SwingUtilities.invokeLater(() -> {
                            try {
                                new StudentDashboard().setVisible(true);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null,
                                        "Error launching student dashboard: " + ex.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        });
                    } else if (userType.equals("Faculty")) {
                        SwingUtilities.invokeLater(() -> {
                            try {
                                new FacultyDashBoard().setVisible(true);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null,
                                        "Error launching faculty dashboard: " + ex.getMessage(),
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        });
                    }
                } else {
                    statusLabel.setText("Invalid username or password. Please try again.");
                }
            }
        });

        forgot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(frame,
                        "Please contact system administrator to reset your password.",
                        "Password Recovery",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JLabel footer = new JLabel("\u00a9 2024 College Administration System. All rights reserved.");
        footer.setFont(new Font("Arial", Font.PLAIN, 10));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(instituteLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(instituteCombo);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(userTypeLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(radioPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(username);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(password);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(rememberPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(statusLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(signIn);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        formPanel.add(footer);

        outerPanel.add(formPanel, new GridBagConstraints());

        frame.setContentPane(outerPanel);
        frame.setVisible(true);
    }

    private static boolean authenticateUser(String userID, String password, String userType) {
        String tableName = userType.equals("Student") ? "student" : "faculty";
        String idColumn = userType.equals("Student") ? "StudentID" : "FacultyID";

        String query = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ? AND Password = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userID);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Database error: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
