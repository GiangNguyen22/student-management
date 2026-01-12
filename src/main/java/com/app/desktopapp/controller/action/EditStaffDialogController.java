package com.app.desktopapp.controller.action;

import com.app.desktopapp.model.Staff;
import com.app.desktopapp.service.StaffService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class EditStaffDialogController {

    @FXML private TextField txtUsername;
    @FXML private TextField txtName;
    @FXML private TextField txtPosition;
    @FXML private TextField txtMajor;
    @FXML private TextField txtStaffCode;
    @FXML private TextField txtGender;

    private Staff staff; // staff đang sửa

    /* ================= SET DATA ================= */
    public void setStaff(Staff staff) {
        this.staff = staff;

        // Đổ dữ liệu lên form
        txtUsername.setText(staff.getUsername());
        txtName.setText(staff.getName());
        txtPosition.setText(staff.getPosition());
        txtMajor.setText(staff.getMajorName());
        txtStaffCode.setText(staff.getStaffCode());
        txtGender.setText(staff.getGender());
    }

    /* ================= ACTIONS ================= */
    @FXML
    private void handleSave() {

        Staff updateData = new Staff();
        updateData.setName(txtName.getText());
        updateData.setStaffCode(txtStaffCode.getText());
        updateData.setPosition(txtPosition.getText());
        updateData.setMajorName(txtMajor.getText());
        updateData.setGender(txtGender.getText());



        boolean ok = StaffService.handleEdit(
                staff.getUsername(),
                updateData
        );

        if (ok) {
            showAlert(Alert.AlertType.INFORMATION,
                    "Thành công", "Cập nhật nhân viên thành công");
            close();
        } else {
            showAlert(Alert.AlertType.ERROR,
                    "Lỗi", "Cập nhật nhân viên thất bại");
        }
    }

    @FXML
    private void handleCancel() {
        close();
    }

    /* ================= UTILS ================= */
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
