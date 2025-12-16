package com.app.desktopapp.controller;

import com.app.desktopapp.service.ApiService;
import com.app.desktopapp.utils.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuthController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblStatus;

    @FXML
    private void handleLogin() {
        boolean success = ApiService.getInstance()
                .login(txtUsername.getText(), txtPassword.getText());

        if (success) {
            lblStatus.setText("Login success");
//            SceneUtil.switchScene("/view/student.fxml");
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
    private void openHome(){
        SceneUtil.switchScene("/view/home.fxml");
    }
    @FXML
    private void openStudent() {
        SceneUtil.switchScene("/view/student.fxml");
    }
}
