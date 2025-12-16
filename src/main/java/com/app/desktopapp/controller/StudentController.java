package com.app.desktopapp.controller;

import com.app.desktopapp.model.Student;
import com.app.desktopapp.service.ApiResponse;
import com.app.desktopapp.service.StudentService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;

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
        showInfoAlert("Chức năng thêm sinh viên sẽ được phát triển sau!");
    }

    @FXML
    private void handleEditStudent() {
        Student selected = tableStudent.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showInfoAlert("Chỉnh sửa sinh viên: " + selected.getName());
        }
    }

    @FXML
    private void handleDeleteStudent() {
        Student selected = tableStudent.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Xóa sinh viên");
            alert.setContentText("Bạn có chắc chắn muốn xóa sinh viên " + selected.getName() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                students.remove(selected);
                updateStatistics();
                showInfoAlert("Đã xóa sinh viên thành công!");
            }
        }
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
