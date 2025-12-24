package com.app.desktopapp.controller.action;

import com.app.desktopapp.dto.CourseRequestDTO;
import com.app.desktopapp.dto.LecturerDTO;
import com.app.desktopapp.service.CourseService;
import com.app.desktopapp.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class AddCourseController {

    @FXML private TextField txtCourseCode;
    @FXML private TextField txtCourseName;
    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;
    @FXML private TextArea txtDescription;
    @FXML
    private ComboBox<LecturerDTO> cbLecturer;


    private Stage stage;
    private boolean saved = false;

    private static final ObjectMapper mapper = new ObjectMapper();

    @FXML
    public void initialize() {
        loadLecturers();
    }

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

            showAlert("Vui lòng nhập đầy đủ mã và tên khóa học!");
            return;
        }

        LecturerDTO lecturer = cbLecturer.getValue();
        if (lecturer == null) {
            showAlert("Vui lòng chọn giảng viên!");
            return;
        }

        CourseRequestDTO req = new CourseRequestDTO();
        req.courseCode = txtCourseCode.getText();
        req.courseName = txtCourseName.getText();
        req.staffCode = lecturer.getStaffCode();
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

    private void loadLecturers() {
        List<LecturerDTO> lecturers =
                StudentService.getAllLecturer();

        cbLecturer.setItems(
                FXCollections.observableArrayList(lecturers)
        );
    }
}
