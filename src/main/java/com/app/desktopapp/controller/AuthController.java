package com.app.desktopapp.controller;

import java.io.IOException;

import com.app.desktopapp.service.ApiService;
import com.app.desktopapp.utils.SceneUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AuthController {

    // Login fields
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblStatus;

    // Register fields
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label registerStatus;

    // ==================== LOGIN ====================
   @FXML
private void handleLogin() {
    boolean success = ApiService.getInstance()
            .login(txtUsername.getText(), txtPassword.getText());

    if (success) {
        lblStatus.setText("Login success");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) lblStatus.getScene().getWindow(); // lấy stage hiện tại
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            lblStatus.setText("Failed to load Home scene");
        }

    } else {
        lblStatus.setText("Login failed");
    }
}

    @FXML
    private void openRegister() {
        SceneUtil.switchScene("/view/register.fxml");
    }

    @FXML
    private void openLogin() {
        SceneUtil.switchScene("/view/login.fxml");
    }

    @FXML
    private void openHome() {
        SceneUtil.switchScene("/view/home.fxml");
    }

    @FXML
    private void openStudent() {
        SceneUtil.switchScene("/view/student.fxml");
    }

    // ==================== REGISTER ====================
   @FXML
   private void handleRegister() {
       String username = usernameField.getText();
       String password = passwordField.getText();
       String confirm = confirmPasswordField.getText();

       if (!password.equals(confirm)) {
           registerStatus.setText("Passwords do not match!");
           return;
       }
       if (username.isBlank() || password.isBlank()) {
           registerStatus.setText("Username and password are required!");
           return;
       }

       // Gọi API với username và password
       boolean success = ApiService.getInstance().register(username, password);

       if (success) {
           registerStatus.setStyle("-fx-text-fill: green;");
           registerStatus.setText("Register success!");
           openLogin();
       } else {
           registerStatus.setStyle("-fx-text-fill: red;");
           registerStatus.setText("Register failed!");
       }
   }
}
