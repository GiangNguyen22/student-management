package com.app.desktopapp.controller.action;

import com.app.desktopapp.model.Staff;
import com.app.desktopapp.service.StaffService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddStaffDialogController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtName;
    @FXML private TextField txtPosition;
    @FXML private TextField txtMajor;
    @FXML private TextField txtStaffCode;
    @FXML private TextField txtGender;

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

        if (isBlank(txtUsername) || isBlank(txtName) || isBlank(txtPosition)
                || isBlank(txtMajor) || isBlank(txtStaffCode) || isBlank(txtGender)) {
            alert("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        Staff staff = new Staff();
        staff.setUsername(txtUsername.getText().trim()); // 🔥 QUAN TRỌNG
        staff.setName(txtName.getText().trim());
        staff.setPosition(txtPosition.getText().trim());
        staff.setMajorName(txtMajor.getText().trim());
        staff.setStaffCode(txtStaffCode.getText().trim());
        staff.setGender(txtGender.getText().trim());

        boolean ok = StaffService.createStaff(staff);

        if (ok) {
            saved = true;
            alert("Thêm nhân viên thành công");
            stage.close();
        } else {
            alert("Thêm nhân viên thất bại");
        }
    }

    @FXML
    private void handleCancel() {
        stage.close();
    }

    private boolean isBlank(TextField t) {
        return t.getText() == null || t.getText().trim().isEmpty();
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
