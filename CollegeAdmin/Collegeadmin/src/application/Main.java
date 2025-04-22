package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            
            // Create the scene
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            
            // Configure the stage
            primaryStage.setTitle("College Administration System");
            primaryStage.setScene(scene);
            
            // Add application icon if available
            try {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../resources/symbiosis_logo.png")));
            } catch (Exception e) {
                System.out.println("Icon could not be loaded: " + e.getMessage());
            }
            
            // Set dimensions for the window
            primaryStage.setWidth(1024);
            primaryStage.setHeight(768);
            
            // Make the window resizable and maximizable
            primaryStage.setResizable(true);
            
            // Show the stage
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}