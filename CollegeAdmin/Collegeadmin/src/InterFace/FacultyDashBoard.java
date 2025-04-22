package InterFace;

import javax.swing.*;
import java.awt.*;

public class FacultyDashBoard extends JFrame {
    public FacultyDashBoard() {
        setTitle("Faculty Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 700);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Heading
        JLabel heading = new JLabel("Faculty Dashboard", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 32));
        mainPanel.add(heading, BorderLayout.NORTH);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(40, 400, 40, 400)); // horizontal padding
        buttonPanel.setOpaque(false);

        // Font for buttons
        Font buttonFont = new Font("Arial", Font.PLAIN, 18);

        // Buttons
        JButton viewUpdateResultBtn = new JButton("View and Update Result");
        JButton updateDeleteEventBtn = new JButton("Add/Update/Delete Event");
        JButton insertDeleteStudentBtn = new JButton("Insert/Delete Student records");
        JButton logoutBtn = new JButton("Logout"); // Added logout button

        JButton[] buttons = {
            viewUpdateResultBtn,
            updateDeleteEventBtn,
            insertDeleteStudentBtn,
            logoutBtn  // Add the logout button to the array
        };

        for (JButton btn : buttons) {
            btn.setFont(buttonFont);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(400, 45));
            btn.setPreferredSize(new Dimension(400, 45));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(60, 120, 180));
            btn.setForeground(Color.black);
            buttonPanel.add(btn);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        }

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Action Listeners
        viewUpdateResultBtn.addActionListener(e -> {
            // Replace with the actual implementation for viewing/updating results
            new ViewUpdateResult();
        });

        updateDeleteEventBtn.addActionListener(e -> {
            new EventManagement();
        });

        insertDeleteStudentBtn.addActionListener(e -> {
            new StudentRecordManagement(); // Open the StudentRecordManagement window
        });
        
        // Logout button action listener
        logoutBtn.addActionListener(e -> {
            // Clear session data
            Session.clearSession();
            
            // Close current window
            dispose();
            
            // Open login screen
            SwingUtilities.invokeLater(() -> {
                try {
                    // Assuming CollegeAdminLogin has a static method createAndShowGUI
                    new CollegeAdminLogin().main(new String[]{});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(FacultyDashBoard::new);
    }
}