package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class LoginController implements Initializable {

    @FXML
    private ComboBox<String> instituteComboBox;

    @FXML
    private RadioButton facultyRadio;

    @FXML
    private RadioButton staffRadio;

    @FXML
    private RadioButton studentRadio;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMeCheckbox;

    @FXML
    private Button signInButton;

    @FXML
    private Hyperlink forgotPasswordLink;

    private ToggleGroup userTypeGroup;

    // List of institutes to populate the combo box
    private ObservableList<String> institutes = FXCollections.observableArrayList(
        "Computer Science and Engineering(CSE)",
        "Artificial Intelligence and Machine learning(AIML)",
        "Electronics and Telecommunications(ENTC)",
        "Robotics And Automation(RNA)",
        "Civil Engineering(CIVIL)",
        "Mechanical Engineering(MECH)"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up institutes dropdown
        instituteComboBox.setItems(institutes);
        instituteComboBox.setPromptText("Please select department"); // Set the prompt text
        instituteComboBox.setValue(null); // Ensure no initial selection

        // Set up toggle group for radio buttons
        userTypeGroup = new ToggleGroup();
        facultyRadio.setToggleGroup(userTypeGroup);
        staffRadio.setToggleGroup(userTypeGroup);
        studentRadio.setToggleGroup(userTypeGroup);

        // Ensure no radio button is selected by default
        userTypeGroup.selectToggle(null);
    }

    @FXML
    void handleSignIn(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate input fields
        if (username.isEmpty()) {
            showAlert("Please enter your username.");
            return;
        }

        if (password.isEmpty()) {
            showAlert("Please enter your password.");
            return;
        }

        // Get selected user type
        String userType = "";
        if (facultyRadio.isSelected()) userType = "Faculty";
        else if (studentRadio.isSelected()) userType = "Student";

        // Here you would normally authenticate against a database
        // For now, just print login details to console
        System.out.println("Login Attempt:");
        System.out.println("Department " + instituteComboBox.getValue());
        System.out.println("User Type: " + userType);
        System.out.println("Username: " + username);
        System.out.println("Remember Me: " + rememberMeCheckbox.isSelected());

        // Show success message (in a real app, you'd navigate to the next screen)
        showSuccessAlert("Login successful!");
    }

    @FXML
    void handleForgotPassword(ActionEvent event) {
        showAlert("Password reset functionality will be implemented in future updates.");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}