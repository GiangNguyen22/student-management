package com.app.desktopapp.controller;

import com.app.desktopapp.controller.action.EditStudentController;
import com.app.desktopapp.model.Student;
import com.app.desktopapp.service.ApiResponse;
import com.app.desktopapp.service.StudentService;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Objects;

public class StudentController {

    @FXML private TextField txtSearch;
    @FXML private TableView<Student> tableStudent;
    @FXML private TableColumn<Student, String> colStudentCode;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, String> colMajor;
    @FXML private TableColumn<Student, LocalDate> colDob;
    @FXML private TableColumn<Student, String> colStartYear;
    @FXML private TableColumn<Student, String> colGender;

    @FXML private Button btnEdit, btnDelete, btnView;
    @FXML private Label lblRecordCount, lblGenderStats, lblMajorStats, lblPage;

    private ObservableList<Student> students = FXCollections.observableArrayList();

    private int currentPage = 0;
    private int totalPages = 1;
    private boolean isSearching = false;


    @FXML
    private void initialize() {
        setupTableColumns();
        setupSelectionListener();
        loadData();
    }

    private void setupTableColumns() {
        colStudentCode.setCellValueFactory(new PropertyValueFactory<>("studentCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colMajor.setCellValueFactory(new PropertyValueFactory<>("major"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colStartYear.setCellValueFactory(new PropertyValueFactory<>("startYear"));

        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colDob.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
            }
        });
    }

    private void setupSelectionListener() {
        tableStudent.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> {
                    boolean hasSelection = newSel != null;
                    btnEdit.setDisable(!hasSelection);
                    btnDelete.setDisable(!hasSelection);
                    btnView.setDisable(!hasSelection);
                }
        );
    }

    /* ================== LOAD DATA FROM API ================== */
    private void loadData() {
        new Thread(() -> {
            ApiResponse res;

            if (isSearching && !txtSearch.getText().isBlank()) {
                res = StudentService.searchStudents(txtSearch.getText(), currentPage);
            } else {
                res = StudentService.getStudents(currentPage);
            }

            Platform.runLater(() -> {
                students.setAll(res.data);
                tableStudent.setItems(students);

                totalPages = res.totalPages;
                if (lblPage != null) {
                    lblPage.setText((currentPage + 1) + " / " + totalPages);
                }

                updateStatistics();
            });
        }).start();
    }

    /* ================== STATISTICS ================== */
    private void updateStatistics() {
        lblRecordCount.setText(students.size() + " sinh viên");

        long male = students.stream().filter(s -> "Nam".equals(s.getGender())).count();
        long female = students.stream().filter(s -> "Nữ".equals(s.getGender())).count();
        lblGenderStats.setText("Nam: " + male + " | Nữ: " + female);

        long cntt = students.stream().filter(s -> "CNTT".equals(s.getMajor())).count();
        long kt = students.stream().filter(s -> "KT".equals(s.getMajor())).count();
        long qtkd = students.stream().filter(s -> "QTKD".equals(s.getMajor())).count();
        lblMajorStats.setText("CNTT: " + cntt + " | KT: " + kt + " | QTKD: " + qtkd);
    }

    /* ================== EVENT HANDLERS ================== */
    @FXML
    private void handleSearch() {
        isSearching = true;
        currentPage = 0;
        loadData();
    }

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        isSearching = false;
        currentPage = 0;
        loadData();
        showInfoAlert("Đã làm mới danh sách!");
    }

    @FXML
    private void nextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadData();
        }
    }

    @FXML
    private void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            loadData();
        }
    }

    @FXML
    private void handleAddStudent() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/modal/add-student-modal.fxml")
            );
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Thêm sinh viên");
            dialog.getIcons().add(
                    new Image(getClass().getResourceAsStream("/images/logo.png"))
            );
            dialog.initModality(Modality.APPLICATION_MODAL);

            // set owner (quan trọng)
            Stage owner = (Stage) tableStudent.getScene().getWindow();
            dialog.initOwner(owner);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/css/add-student.css").toExternalForm()
            );
            dialog.setScene(scene);

            AddStudentDialogController controller = loader.getController();
            controller.setStage(dialog);

            dialog.showAndWait();

            if (controller.isSaved()) {
                loadData(); // chỉ reload khi lưu
                System.out.println("Đã thêm: " + controller.getUsername());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleEditStudent() {
        Student selected = tableStudent.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showInfoAlert("Vui lòng chọn sinh viên cần chỉnh sửa");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/modal/edit-student-modal.fxml")
            );
            Parent root = loader.load();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/css/edit-student.css").toExternalForm()
            );

            EditStudentController controller = loader.getController();
            controller.setStudent(selected);

            Stage dialog = new Stage();
            dialog.setTitle("Chỉnh sửa sinh viên");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tableStudent.getScene().getWindow()); // khuyên dùng
            dialog.setScene(scene);

            dialog.getIcons().add(
                    new Image(getClass().getResourceAsStream("/images/logo.png"))
            );

            dialog.showAndWait();

            loadData(); // reload table sau khi sửa

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleDeleteStudent() {
        Student selected = tableStudent.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("Vui lòng chọn sinh viên để xóa");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Xóa sinh viên");
        alert.setContentText(
                "Bạn có chắc chắn muốn xóa sinh viên " + selected.getName() + "?"
        );

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {

                boolean ok = StudentService
                        .deleteStudent(selected.getStudentCode());

                if (ok) {
                    students.remove(selected);
                    updateStatistics();
                    showInfoAlert("Đã xóa sinh viên thành công!");
                } else {
                    showInfoAlert("Xóa sinh viên thất bại");
                }
            }
        });
    }


    @FXML
    private void handleViewDetails() {
        Student s = tableStudent.getSelectionModel().getSelectedItem();
        if (s != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Chi tiết sinh viên");
            alert.setHeaderText(null);
            alert.setContentText(
                    "MSSV: " + s.getStudentCode() +
                            "\nHọ tên: " + s.getName() +
                            "\nEmail: " + s.getEmail() +
                            "\nNgành: " + s.getMajor() +
                            "\nNgày sinh: " + s.getDob() +
                            "\nKhóa: " + s.getStartYear() +
                            "\nGiới tính: " + s.getGender()
            );
            alert.showAndWait();
        }
    }

    @FXML
    private void handleTableClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            handleViewDetails();
        }
    }

    private void showInfoAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
