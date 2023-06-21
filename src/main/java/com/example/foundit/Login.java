package com.example.foundit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class Login extends Application {
    private static final String USER_FILE = "users.txt";
    private Map<String, String> userCredentials;

    @Override
    public void init() {
        userCredentials = readUserCredentials();
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, Color.WHITE);

        Image icon = new Image(getFileUrl("C:\\Users\\nmary\\OneDrive\\Desktop\\UN ORGANISED\\ILLUSTRATIONS\\1.jpeg"));
        stage.getIcons().add(icon);

        // Left Pane with Image
        ImageView leftImageView = new ImageView(getFileUrl("C:\\Users\\nmary\\OneDrive\\Desktop\\UN ORGANISED\\ILLUSTRATIONS\\1.jpeg"));
        leftImageView.setFitWidth(700);
        leftImageView.setPreserveRatio(true);
        leftImageView.setSmooth(true);
        leftImageView.setCache(true);

        // Right Pane with Login Form
        GridPane rightPane = new GridPane();
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setHgap(10);
        rightPane.setVgap(10);
        rightPane.setPadding(new Insets(50));

        Label amritaLogTex = new Label("Amrita ID:");
        amritaLogTex.setStyle("-fx-font-weight: bold;");
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #a3113e; -fx-text-fill: white;");
        loginButton.setOnAction(event -> {
            // Perform login logic here
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (authenticateUser(username, password)) {
                openNewPage(stage);
            } else {
                showErrorDialog("Invalid username or password");
            }
        });

        Button signupButton = new Button("Signup");
        signupButton.setStyle("-fx-background-color: #a3113e; -fx-text-fill: white;");
        signupButton.setOnAction(event -> {
            openSignupPage(stage);
        });

        rightPane.setPadding(new Insets(20));
        rightPane.add(amritaLogTex, 0, 0);
        rightPane.add(usernameLabel, 0, 1);
        rightPane.add(usernameField, 1, 1);
        rightPane.add(passwordLabel, 0, 2);
        rightPane.add(passwordField, 1, 2);
        rightPane.add(loginButton, 1, 3);
        rightPane.add(signupButton, 1, 4);

        HBox loginContainer = new HBox(rightPane);
        loginContainer.setAlignment(Pos.CENTER);
        root.setLeft(leftImageView);
        root.setCenter(loginContainer);

        stage.setTitle("Amrita Integrated Management System");
        stage.setWidth(1080);
        stage.setHeight(720);
        stage.setScene(scene);
        stage.show();
    }

    private String getFileUrl(String filePath) {
        try {
            return new File(filePath).toURI().toURL().toExternalForm();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, String> readUserCredentials() {
        Map<String, String> credentials = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String username = parts[0].trim();
                    String password = parts[1].trim();
                    credentials.put(username, password);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return credentials;
    }

    private boolean authenticateUser(String username, String password) {
        return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
    }

    private void openNewPage(Stage stage) {
        // Code to open a new page after successful login
        // Replace with your own implementation
        System.out.println("Login successful! Opening new page...");
    }

    private void openSignupPage(Stage stage) {
        Stage signupStage = new Stage();
        GridPane signupPane = new GridPane();
        signupPane.setAlignment(Pos.CENTER);
        signupPane.setHgap(10);
        signupPane.setVgap(10);
        signupPane.setPadding(new Insets(50));

        Label signupLabel = new Label("Signup");
        signupLabel.setStyle("-fx-font-weight: bold;");
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label reenterPasswordLabel = new Label("Re-enter Password:");
        PasswordField reenterPasswordField = new PasswordField();

        Button signupButton = new Button("Sign up");
        signupButton.setStyle("-fx-background-color: #a3113e; -fx-text-fill: white;");
        signupButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String reenterPassword = reenterPasswordField.getText();

            if (!password.equals(reenterPassword)) {
                showErrorDialog("Passwords do not match");
                return;
            }

            if (userCredentials.containsKey(username)) {
                showErrorDialog("Username already exists");
                return;
            }

            userCredentials.put(username, password);
            saveUserCredentials();

            signupStage.close();
            showSuccessDialog("Signup successful! Please login.");
        });

        signupPane.setPadding(new Insets(20));
        signupPane.add(signupLabel, 0, 0);
        signupPane.add(usernameLabel, 0, 1);
        signupPane.add(usernameField, 1, 1);
        signupPane.add(passwordLabel, 0, 2);
        signupPane.add(passwordField, 1, 2);
        signupPane.add(reenterPasswordLabel, 0, 3);
        signupPane.add(reenterPasswordField, 1, 3);
        signupPane.add(signupButton, 1, 4);

        Scene signupScene = new Scene(signupPane);
        signupStage.setTitle("Signup");
        signupStage.setWidth(400);
        signupStage.setHeight(300);
        signupStage.setScene(signupScene);
        signupStage.show();
    }

    private void saveUserCredentials() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}