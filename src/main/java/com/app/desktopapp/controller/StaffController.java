package com.app.desktopapp.controller;

import com.app.desktopapp.controller.action.AddStaffDialogController;
import com.app.desktopapp.controller.action.EditStaffDialogController;
import com.app.desktopapp.model.Staff;
import com.app.desktopapp.service.ApiResponse;
import com.app.desktopapp.service.StaffService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StaffController {

    @FXML
    private TableView<Staff> tableStaff;
    @FXML
    private TableColumn<Staff, String> colStaffCode;
    @FXML
    private TableColumn<Staff, String> colName;
    @FXML
    private TableColumn<Staff, String> colPosition;
    @FXML
    private TableColumn<Staff, String> colMajor;

    @FXML
    private Button btnEdit, btnDelete, btnView;
    @FXML
    private Label lblRecordCount;

    private final ObservableList<Staff> staffs = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colStaffCode.setCellValueFactory(new PropertyValueFactory<>("staffCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colMajor.setCellValueFactory(new PropertyValueFactory<>("majorName"));
        

        tableStaff.getSelectionModel().selectedItemProperty().addListener(
                (obs, o, n) -> {
                    boolean ok = n != null;
                    btnEdit.setDisable(!ok);
                    btnDelete.setDisable(!ok);
                    btnView.setDisable(!ok);
                });

        loadData();
    }

    private void loadData() {
        new Thread(() -> {
            ApiResponse res = StaffService.getStaffs();
            Platform.runLater(() -> {
                staffs.setAll(res.data);
                tableStaff.setItems(staffs);
                lblRecordCount.setText("Tổng: " + staffs.size());
            });
        }).start();
    }

    @FXML
    private void handleAddStaff() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/modal/add-staff-modal.fxml"));
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Thêm nhân viên");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tableStaff.getScene().getWindow());
            dialog.getIcons().add(
                    new Image(getClass().getResourceAsStream("/images/logo.png")));

            Scene scene = new Scene(root);
            dialog.setScene(scene);

            AddStaffDialogController controller = loader.getController();
            controller.setStage(dialog);

            dialog.showAndWait();

            if (controller.isSaved()) {
                loadData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditStaff() {
        try {
            // 1️⃣ Lấy staff đang được chọn
            Staff selectedStaff = tableStaff.getSelectionModel().getSelectedItem();
            if (selectedStaff == null) {
                return;
            }

            // 2️⃣ Load FXML
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/modal/edit-staff-modal.fxml"));
            Parent root = loader.load();

            // 3️⃣ Lấy controller & set data
            EditStaffDialogController controller = loader.getController();
            controller.setStaff(selectedStaff);

            // 4️⃣ Tạo dialog
            Stage dialog = new Stage();
            dialog.setTitle("Sửa nhân viên");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tableStaff.getScene().getWindow());
            dialog.getIcons().add(
                    new Image(getClass().getResourceAsStream("/images/logo.png")));

            dialog.setScene(new Scene(root));
            dialog.showAndWait();

            // 5️⃣ Reload lại table sau khi đóng dialog
            loadData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleDeleteStaff() {
        Staff s = tableStaff.getSelectionModel().getSelectedItem();
        if (s == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Xóa nhân viên " + s.getName() + "?",
                ButtonType.OK, ButtonType.CANCEL);

        alert.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                boolean ok = StaffService.deleteStaff(s.getUsername());
                if (ok) {
                    staffs.remove(s);
                    lblRecordCount.setText("Tổng: " + staffs.size());
                } else {
                    showErrorAlert("Xóa nhân viên thất bại");
                }
            }
        });
    }

    @FXML
    private void handleViewDetails() {
        Staff s = tableStaff.getSelectionModel().getSelectedItem();
        if (s == null)
            return;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Chi tiết nhân viên");
        alert.setHeaderText(null);
        alert.setContentText(
                "Username: " + s.getUsername() + "\n" +
                "Họ tên: " + s.getName() + "\n" +
                "Chức vụ: " + s.getPosition() + "\n" +
                "Ngành: " + s.getMajorName() + "\n" +
                "Mã NV: " + s.getStaffCode() + "\n" +
                "Giới tính: " + s.getGender());
        alert.showAndWait();
    }

    private void showErrorAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setContentText(msg);
        alert.show();
    }
}
