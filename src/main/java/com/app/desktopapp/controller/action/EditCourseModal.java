package com.app.desktopapp.controller.action;

import com.app.desktopapp.dto.CourseRequestDTO;
import com.app.desktopapp.model.Course;
import com.app.desktopapp.service.CourseService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditCourseModal {

    @FXML private TextField txtCourseName;
    @FXML private TextField txtCourseCode;
    @FXML private TextArea txtDescription;
    @FXML private TextField txtStaffCode;
    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;

    private Stage stage;
    private Course course;
    private boolean updated = false;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /** Đổ dữ liệu vào form */
    public void setCourse(Course c) {
        this.course = c;

        txtCourseName.setText(c.getCourseName());
        txtCourseCode.setText(c.getCourseCode());
        txtDescription.setText(c.getDescription());
        txtStaffCode.setText(c.getStaffCode());
        dpStartDate.setValue(c.getStartDate());
        dpEndDate.setValue(c.getEndDate());
    }

    @FXML
    private void handleUpdate() {
        if (txtCourseName.getText().isBlank()) {
            showAlert("Tên khóa học không được để trống");
            return;
        }
        CourseRequestDTO course = new CourseRequestDTO();
        course.setCourseCode(txtCourseCode.getText());
        course.setCourseName(txtCourseName.getText());
        course.setDescription(txtDescription.getText());
        course.setStaffCode(txtStaffCode.getText());
        course.setStartDate(dpStartDate.getValue());
        course.setEndDate(dpEndDate.getValue());

        boolean ok = CourseService.updateCourse(course);

        if (ok) {
            updated = true;
            showAlert("Cập nhật khóa học thành công");
            stage.close();
        } else {
            showAlert("Cập nhật khóa học thất bại");
        }
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    public boolean isUpdated() {
        return updated;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
