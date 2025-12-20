package com.app.desktopapp.controller;

import com.app.desktopapp.dto.CreateStudentRequest;
import com.app.desktopapp.service.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddStudentDialogController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtName;
    @FXML private DatePicker dpDob;
    @FXML private TextField txtStudentCode;
    @FXML private TextField txtMajorName;
    @FXML private TextField txtYear;

    private Stage stage;
    private boolean saved = false;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void handleSave() {
        if (txtUsername.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            showAlert("Lỗi", "Username và Email không được để trống");
            return;
        }
        CreateStudentRequest req = new CreateStudentRequest();
        req.setUsername(txtUsername.getText());
        req.setEmail(txtEmail.getText());
        req.setPassword(txtPassword.getText());
        req.setName(txtName.getText());
        req.setDob(dpDob.getValue());
        req.setStudentCode(txtStudentCode.getText());
        req.setMajorName(txtMajorName.getText());
        req.setYear(txtYear.getText());
        req.setRole(req.getRole());

        boolean ok = StudentService.createStudent(req);

        if (ok) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Thêm sinh viên thành công!");
            alert.showAndWait();

            saved = true;
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Lỗi");
            alert.setContentText("Không thể thêm sinh viên");
            alert.show();
        }

    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Getter để lấy dữ liệu
    public String getUsername() { return txtUsername.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public String getPassword() { return txtPassword.getText(); }
    public String getName() { return txtName.getText(); }
    public LocalDate getDob() { return dpDob.getValue(); }
    public String getStudentCode() { return txtStudentCode.getText(); }
    public String getMajorName() { return txtMajorName.getText(); }
    public String getYear() { return txtYear.getText(); }
}
