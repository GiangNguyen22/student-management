package com.app.desktopapp.controller.action;

import com.app.desktopapp.model.Student;
import com.app.desktopapp.service.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class EditStudentController {

    @FXML
    private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private DatePicker dpDob;
    @FXML private TextField txtMajor;
    @FXML private TextField txtYear;

    private Student student; // student đang sửa

    public void setStudent(Student student) {
        this.student = student;

        // Đổ dữ liệu lên form
        txtName.setText(student.getName());
        txtEmail.setText(student.getEmail());
        dpDob.setValue(student.getDob());
        txtMajor.setText(student.getMajor());
        txtYear.setText(student.getStartYear());
    }

    @FXML
    private void handleSave() {

        Map<String, Object> updateData = new HashMap<>();
        updateData.put("fullName", txtName.getText());
        updateData.put("email", txtEmail.getText());
        updateData.put("dob", dpDob.getValue());
        updateData.put("majorName", txtMajor.getText());
        updateData.put("startYear", txtYear.getText());

        boolean ok = StudentService.updateStudent(
                student.getStudentCode(),
                updateData
        );

        if (ok) {
            showAlert(Alert.AlertType.INFORMATION,
                    "Thành công", "Cập nhật sinh viên thành công");
            close();
        } else {
            showAlert(Alert.AlertType.ERROR,
                    "Lỗi", "Cập nhật sinh viên thất bại");
        }
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        ((Stage) txtName.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }
}
