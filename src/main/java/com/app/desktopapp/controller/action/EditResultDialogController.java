package com.app.desktopapp.controller.action;

import com.app.desktopapp.model.Result;
import com.app.desktopapp.service.ResultService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditResultDialogController {

    @FXML private TextField txtStudentCode;
    @FXML private TextField txtCourseCode;
    @FXML private TextField txtScore;
    @FXML private TextField txtGrade;
    @FXML private TextField txtSemester;
    @FXML private TextField txtTimeStudied;

    private Result result; // result đang sửa

    public void setResult(Result result) {
        this.result = result;

        // Đổ dữ liệu lên form
        txtStudentCode.setText(result.getStudentCode());
        txtCourseCode.setText(result.getCourseCode());
        txtScore.setText(String.valueOf(result.getScore()));
        txtGrade.setText(result.getGrade());
        txtSemester.setText(result.getSemester());
        txtTimeStudied.setText(result.getTimeStudied());
    }

    @FXML
    private void handleSave() {
        if (txtScore.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập điểm số!");
            return;
        }

        try {
            double score = Double.parseDouble(txtScore.getText());
            if (score < 0.0 || score > 10.0) {
                showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Điểm số phải nằm trong khoảng 0.0 - 10.0!");
                return;
            }
            result.setScore(score);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Cảnh báo", "Vui lòng nhập điểm số hợp lệ!");
            return;
        }
        result.setGrade(txtGrade.getText());
        result.setSemester(txtSemester.getText());
        result.setTimeStudied(txtTimeStudied.getText());

        Result updatedResult = ResultService.updateResult(result);

        if (updatedResult != null) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Cập nhật kết quả thành công");
            close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Cập nhật kết quả thất bại");
        }
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        ((Stage) txtStudentCode.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }
}