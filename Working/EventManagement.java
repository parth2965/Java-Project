package Working;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.border.EmptyBorder;
import java.text.ParseException;
import javax.swing.table.DefaultTableModel;

public class EventManagement extends JFrame {
    private JTextField eventIdField, eventNameField, descriptionField, venueField;
    private JTextField dateField;
    private JRadioButton addRadio, updateRadio, deleteRadio;
    private JTable eventTable;
    private DefaultTableModel tableModel;
    private Connection connection;
    
    public EventManagement() {
        setTitle("Event Management");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Set light blue background
        getContentPane().setBackground(new Color(173, 216, 230));
        
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/university_db", "root", "Pratiksql@2004");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        
        // Main panel with light blue background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(173, 216, 230));
        
        // Operations panel (top)
        JPanel operationsPanel = new JPanel(new BorderLayout());
        operationsPanel.setBackground(new Color(173, 216, 230));
        
        JPanel radioPanel = new JPanel();
        radioPanel.setBackground(new Color(173, 216, 230));
        
        ButtonGroup operationGroup = new ButtonGroup();
        addRadio = new JRadioButton("Add Event");
        updateRadio = new JRadioButton("Update Event");
        deleteRadio = new JRadioButton("Delete Event");
        
        addRadio.setSelected(true);
        
        operationGroup.add(addRadio);
        operationGroup.add(updateRadio);
        operationGroup.add(deleteRadio);
        
        radioPanel.add(addRadio);
        radioPanel.add(updateRadio);
        radioPanel.add(deleteRadio);
        
        operationsPanel.add(radioPanel, BorderLayout.NORTH);
        
        // Form panel with light blue background
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(new Color(173, 216, 230));
        
        // Event ID
        formPanel.add(new JLabel("Event ID (EXXX format):"));
        eventIdField = new JTextField(10);
        formPanel.add(eventIdField);
        
        // Event Name
        formPanel.add(new JLabel("Event Name (min 4 chars):"));
        eventNameField = new JTextField(30);
        formPanel.add(eventNameField);
        
        // Description
        formPanel.add(new JLabel("Description (min 6 chars):"));
        descriptionField = new JTextField(50);
        formPanel.add(descriptionField);
        
        // Date
        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField(10);
        formPanel.add(dateField);
        
        // Venue
        formPanel.add(new JLabel("Venue (min 4 chars):"));
        venueField = new JTextField(30);
        formPanel.add(venueField);
        
        operationsPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(173, 216, 230));
        JButton executeButton = new JButton("Execute Operation");
        JButton clearButton = new JButton("Clear Fields");
        
        buttonPanel.add(executeButton);
        buttonPanel.add(clearButton);
        
        operationsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Table panel with light blue background
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Event List"));
        tablePanel.setBackground(new Color(173, 216, 230));
        
        String[] columns = {"EventID", "Event Name", "Description", "Date of Event", "Venue"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        eventTable = new JTable(tableModel);
        eventTable.setBackground(new Color(173, 216, 230));
        JScrollPane scrollPane = new JScrollPane(eventTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(operationsPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Event listeners
        executeButton.addActionListener(e -> executeOperation());
        clearButton.addActionListener(e -> clearFields());
        
        // Table selection listener
        eventTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && eventTable.getSelectedRow() != -1) {
                int row = eventTable.getSelectedRow();
                eventIdField.setText(eventTable.getValueAt(row, 0).toString());
                eventNameField.setText(eventTable.getValueAt(row, 1).toString());
                descriptionField.setText(eventTable.getValueAt(row, 2).toString());
                dateField.setText(eventTable.getValueAt(row, 3).toString());
                venueField.setText(eventTable.getValueAt(row, 4).toString());
            }
        });
        
        ActionListener radioListener = e -> configureFormForOperation();
        addRadio.addActionListener(radioListener);
        updateRadio.addActionListener(radioListener);
        deleteRadio.addActionListener(radioListener);
        
        loadEventData();
        configureFormForOperation();
        
        setVisible(true);
    }
    
    private void configureFormForOperation() {
        boolean isDelete = deleteRadio.isSelected();
        
        eventNameField.setEnabled(!isDelete);
        descriptionField.setEnabled(!isDelete);
        dateField.setEnabled(!isDelete);
        venueField.setEnabled(!isDelete);
    }
    
    private void executeOperation() {
        try {
            if (addRadio.isSelected()) {
                addEvent();
            } else if (updateRadio.isSelected()) {
                updateEvent();
            } else if (deleteRadio.isSelected()) {
                deleteEvent();
            }
            
            loadEventData();
            clearFields();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private boolean isValidEventId(String eventId) {
        return eventId.matches("^E\\d+$") && eventId.charAt(0) == 'E';
    }
    
    private boolean isValidDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    
    private void validateAddFields() throws IllegalArgumentException {
        if (eventIdField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID is required");
        }
        if (eventNameField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Event name is required");
        }
        if (descriptionField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (dateField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Date is required");
        }
        if (venueField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Venue is required");
        }
        
        String eventId = eventIdField.getText().trim();
        String eventName = eventNameField.getText().trim();
        String description = descriptionField.getText().trim();
        String dateStr = dateField.getText().trim();
        String venue = venueField.getText().trim();
        
        if (!isValidEventId(eventId)) {
            throw new IllegalArgumentException("Event ID must be in EXXX format (Uppercase E followed by numbers)");
        }
        if (eventName.length() < 4) {
            throw new IllegalArgumentException("Event name must be at least 4 characters");
        }
        if (description.length() < 6) {
            throw new IllegalArgumentException("Description must be at least 6 characters");
        }
        if (!isValidDate(dateStr)) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD format.");
        }
        if (venue.length() < 4) {
            throw new IllegalArgumentException("Venue must be at least 4 characters");
        }
    }
    
    private void validateUpdateFields() throws IllegalArgumentException {
        if (eventIdField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID is required");
        }
        
        String eventId = eventIdField.getText().trim();
        String eventName = eventNameField.getText().trim();
        String description = descriptionField.getText().trim();
        String dateStr = dateField.getText().trim();
        String venue = venueField.getText().trim();
        
        if (!isValidEventId(eventId)) {
            throw new IllegalArgumentException("Event ID must be in EXXX format (Uppercase E followed by numbers)");
        }
        if (!eventName.isEmpty() && eventName.length() < 4) {
            throw new IllegalArgumentException("Event name must be at least 4 characters");
        }
        if (!description.isEmpty() && description.length() < 6) {
            throw new IllegalArgumentException("Description must be at least 6 characters");
        }
        if (!dateStr.isEmpty() && !isValidDate(dateStr)) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD format.");
        }
        if (!venue.isEmpty() && venue.length() < 4) {
            throw new IllegalArgumentException("Venue must be at least 4 characters");
        }
    }
    
    private void addEvent() throws SQLException {
        validateAddFields();
        
        String eventId = eventIdField.getText().trim();
        String eventName = eventNameField.getText().trim();
        String description = descriptionField.getText().trim();
        String dateStr = dateField.getText().trim();
        String venue = venueField.getText().trim();
        
        // Check if event ID exists
        String checkSql = "SELECT COUNT(*) FROM event WHERE EventID = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, eventId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new IllegalArgumentException("Event ID already exists");
            }
        }
        
        String sql = "INSERT INTO event (EventID, Event_name, Description, Date_of_event, Venue) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, eventId);
            stmt.setString(2, eventName);
            stmt.setString(3, description);
            stmt.setString(4, dateStr);
            stmt.setString(5, venue);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Event added successfully");
            }
        }
    }
    
    private void updateEvent() throws SQLException {
        validateUpdateFields();
        
        String eventId = eventIdField.getText().trim();
        
        // Check if event exists
        String checkSql = "SELECT COUNT(*) FROM event WHERE EventID = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, eventId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new IllegalArgumentException("Event ID does not exist");
            }
        }
        
        StringBuilder sqlBuilder = new StringBuilder("UPDATE event SET ");
        boolean hasUpdates = false;
        
        if (!eventNameField.getText().trim().isEmpty()) {
            sqlBuilder.append("Event_name = ?, ");
            hasUpdates = true;
        }
        
        if (!descriptionField.getText().trim().isEmpty()) {
            sqlBuilder.append("Description = ?, ");
            hasUpdates = true;
        }
        
        if (!dateField.getText().trim().isEmpty()) {
            sqlBuilder.append("Date_of_event = ?, ");
            hasUpdates = true;
        }
        
        if (!venueField.getText().trim().isEmpty()) {
            sqlBuilder.append("Venue = ?, ");
            hasUpdates = true;
        }
        
        if (!hasUpdates) {
            throw new IllegalArgumentException("At least one field must be provided for update");
        }
        
        String sql = sqlBuilder.substring(0, sqlBuilder.length() - 2) + " WHERE EventID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int paramIndex = 1;
            
            if (!eventNameField.getText().trim().isEmpty()) {
                stmt.setString(paramIndex++, eventNameField.getText().trim());
            }
            
            if (!descriptionField.getText().trim().isEmpty()) {
                stmt.setString(paramIndex++, descriptionField.getText().trim());
            }
            
            if (!dateField.getText().trim().isEmpty()) {
                stmt.setString(paramIndex++, dateField.getText().trim());
            }
            
            if (!venueField.getText().trim().isEmpty()) {
                stmt.setString(paramIndex++, venueField.getText().trim());
            }
            
            stmt.setString(paramIndex, eventId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Event updated successfully");
            }
        }
    }
    
    private void deleteEvent() throws SQLException {
        if (eventIdField.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Event ID is required for deleting an event");
        }
        
        String eventId = eventIdField.getText().trim();
        
        if (!isValidEventId(eventId)) {
            throw new IllegalArgumentException("Event ID must be in EXXX format (Uppercase E followed by numbers)");
        }
        
        // Check if event exists
        String checkSql = "SELECT COUNT(*) FROM event WHERE EventID = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, eventId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new IllegalArgumentException("Event ID does not exist");
            }
        }
        
        // Check for participants
        String checkParticipationSql = "SELECT COUNT(*) FROM participates WHERE EventID = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkParticipationSql)) {
            checkStmt.setString(1, eventId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                int option = JOptionPane.showConfirmDialog(this, 
                    "This event has participants. Deleting it will also remove all participation records. Continue?",
                    "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (option != JOptionPane.YES_OPTION) {
                    return;
                }
                
                String deleteParticipationSql = "DELETE FROM participates WHERE EventID = ?";
                try (PreparedStatement deleteParticipatesStmt = connection.prepareStatement(deleteParticipationSql)) {
                    deleteParticipatesStmt.setString(1, eventId);
                    deleteParticipatesStmt.executeUpdate();
                }
            }
        }
        
        String sql = "DELETE FROM event WHERE EventID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, eventId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Event deleted successfully");
            }
        }
    }
    
    private void loadEventData() {
        try {
            tableModel.setRowCount(0);
            
            String sql = "SELECT * FROM event ORDER BY Date_of_event";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("EventID"),
                        rs.getString("Event_name"),
                        rs.getString("Description"),
                        rs.getString("Date_of_event"),
                        rs.getString("Venue")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading event data: " + e.getMessage(), 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void clearFields() {
        eventIdField.setText("");
        eventNameField.setText("");
        descriptionField.setText("");
        dateField.setText("");
        venueField.setText("");
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(EventManagement::new);
    }
}