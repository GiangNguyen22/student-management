package com.app.desktopapp.controller.action;

import com.app.desktopapp.model.Result;
import com.app.desktopapp.service.ResultService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddResultDialogController {

    @FXML private TextField txtStudentCode;
    @FXML private TextField txtCourseCode;
    @FXML private TextField txtScore;
    @FXML private TextField txtGrade;
    @FXML private TextField txtSemester;
    @FXML private TextField txtTimeStudied;

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
        if (txtStudentCode.getText().isBlank() || txtCourseCode.getText().isBlank()) {
            showAlert("Vui lòng nhập đầy đủ mã sinh viên và mã môn học!");
            return;
        }

        Result result = new Result();
        result.setStudentCode(txtStudentCode.getText());
        result.setCourseCode(txtCourseCode.getText());
        
        try {
            double score = Double.parseDouble(txtScore.getText());
            if (score < 0.0 || score > 10.0) {
                showAlert("Điểm số phải nằm trong khoảng 0.0 - 10.0!");
                return;
            }
            result.setScore(score);
        } catch (NumberFormatException e) {
            showAlert("Vui lòng nhập điểm số hợp lệ!");
            return;
        }
        
        result.setGrade(txtGrade.getText());
        result.setSemester(txtSemester.getText());
        result.setTimeStudied(txtTimeStudied.getText());

        Result createdResult = ResultService.createResult(result);

        if (createdResult != null) {
            saved = true;
            showAlert("Thêm kết quả thành công");
            stage.close();
        } else {
            showAlert("Thêm kết quả thất bại");
        }
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}