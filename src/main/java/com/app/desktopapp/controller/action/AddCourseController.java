package com.app.desktopapp.controller.action;

import com.app.desktopapp.dto.CourseRequestDTO;
import com.app.desktopapp.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddCourseController {

    @FXML private TextField txtCourseCode;
    @FXML private TextField txtCourseName;
    @FXML private TextField txtStaffCode;
    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;
    @FXML private TextArea txtDescription;

    private Stage stage;
    private boolean saved = false;

    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.registerModule(new JavaTimeModule());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void handleSave() {
        if (txtCourseCode.getText().isBlank() ||
                txtCourseName.getText().isBlank()) {

            showAlert("Vui lòng nhập đầy đủ mã và tên khóa học");
            return;
        }

        CourseRequestDTO req = new CourseRequestDTO();
        req.courseCode = txtCourseCode.getText();
        req.courseName = txtCourseName.getText();
        req.staffCode = txtStaffCode.getText();
        req.description = txtDescription.getText();
        req.startDate = dpStartDate.getValue();
        req.endDate = dpEndDate.getValue();

        boolean ok = CourseService.createCourse(req);

        if (ok) {
            saved = true;
            showAlert("Thêm khóa học thành công");
            stage.close();
        } else {
            showAlert("Thêm khóa học thất bại");
        }
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public String getCourseName(){return txtCourseName.getText();}
}
