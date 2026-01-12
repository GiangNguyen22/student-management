package com.app.desktopapp.controller.action;

import com.app.desktopapp.dto.CourseRequestDTO;
import com.app.desktopapp.model.Course;
import com.app.desktopapp.model.Staff;
import com.app.desktopapp.service.CourseService;
import com.app.desktopapp.service.StaffService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class EditCourseModal {

    @FXML private TextField txtCourseName;
    @FXML private TextField txtCourseCode;
    @FXML private TextArea txtDescription;
    @FXML private ComboBox<Staff> cbLecturer;
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
        // Set lecturer
        List<Staff> lecturers = StaffService.getStaffs().getData();
        cbLecturer.setItems(FXCollections.observableArrayList(lecturers));
        // Find the current lecturer
        Staff currentLecturer = lecturers.stream()
                .filter(staff -> staff.getStaffCode().equals(c.getStaffCode()))
                .findFirst()
                .orElse(null);
        cbLecturer.setValue(currentLecturer);
        dpStartDate.setValue(c.getStartDate());
        dpEndDate.setValue(c.getEndDate());
    }

    @FXML
    private void handleUpdate() {
        if (txtCourseName.getText().isBlank()) {
            showAlert("Tên khóa học không được để trống");
            return;
        }

        Staff lecturer = cbLecturer.getValue();
        if (lecturer == null) {
            showAlert("Vui lòng chọn giảng viên!");
            return;
        }

        CourseRequestDTO course = new CourseRequestDTO();
        course.setCourseCode(txtCourseCode.getText());
        course.setCourseName(txtCourseName.getText());
        course.setDescription(txtDescription.getText());
        course.setStaffCode(lecturer.getStaffCode());
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
