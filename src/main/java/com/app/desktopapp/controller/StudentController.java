package com.app.desktopapp.controller;

import com.app.desktopapp.model.Student;
import com.app.desktopapp.service.ApiResponse;
import com.app.desktopapp.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.stream.Collectors;

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
    @FXML private Label lblPage, lblRecordCount, lblGenderStats, lblMajorStats;

    private ObservableList<Student> students = FXCollections.observableArrayList();
    private ObservableList<Student> filteredStudents = FXCollections.observableArrayList();

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
        colStudentCode.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStudentCode()));
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colMajor.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMajor()));
        colDob.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDob()));
        colStartYear.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStartYear()));
        colGender.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGender()));
    }

    private void setupSelectionListener() {
        tableStudent.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                boolean hasSelection = newSelection != null;
                btnEdit.setDisable(!hasSelection);
                btnDelete.setDisable(!hasSelection);
                btnView.setDisable(!hasSelection);
            }
        );
    }

    private void loadData() {
        new Thread(() -> {
            ApiResponse res;
            if (isSearching && !txtSearch.getText().isBlank()) {
                res = StudentService.searchStudents(txtSearch.getText(), currentPage);
            } else {
                res = StudentService.getStudents(currentPage);
            }

            students.setAll(res.students);
            filteredStudents.setAll(students);
            totalPages = res.totalPages;

            javafx.application.Platform.runLater(() -> {
                tableStudent.setItems(filteredStudents);
                lblPage.setText((currentPage + 1) + " / " + totalPages);
                updateStatistics();
            });
        }).start();
    }

    private void updateStatistics() {
        lblRecordCount.setText(filteredStudents.size() + "/" + students.size() + " sinh viên");
        long maleCount = filteredStudents.stream().filter(s -> "Nam".equals(s.getGender())).count();
        long femaleCount = filteredStudents.stream().filter(s -> "Nữ".equals(s.getGender())).count();
        lblGenderStats.setText("Nam: " + maleCount + " | Nữ: " + femaleCount);

        long cnttCount = filteredStudents.stream().filter(s -> "CNTT".equals(s.getMajor())).count();
        long ktCount = filteredStudents.stream().filter(s -> "KT".equals(s.getMajor())).count();
        long qtkdCount = filteredStudents.stream().filter(s -> "QTKD".equals(s.getMajor())).count();
        lblMajorStats.setText("CNTT: " + cnttCount + " | KT: " + ktCount + " | QTKD: " + qtkdCount);
    }

    // --- Event Handlers ---
    @FXML
    private void handleSearch() {
        isSearching = true;
        currentPage = 0;
        loadData();
    }

    @FXML
    private void handleClearSearch() {
        txtSearch.clear();
        isSearching = false;
        currentPage = 0;
        loadData();
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
    private void handleRefresh() {
        txtSearch.clear();
        isSearching = false;
        currentPage = 0;
        loadData();
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
                filteredStudents.remove(selected);
                updateStatistics();
                showInfoAlert("Đã xóa sinh viên thành công!");
            }
        }
    }

    @FXML
    private void handleViewDetails() {
        Student selected = tableStudent.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String details = String.format(
                "MSSV: %s\nHọ tên: %s\nEmail: %s\nChuyên ngành: %s\nNgày sinh: %s\nKhóa: %s\nGiới tính: %s",
                selected.getStudentCode(), selected.getName(), selected.getEmail(),
                selected.getMajor(), selected.getDob(), selected.getStartYear(), selected.getGender()
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Chi tiết sinh viên");
            alert.setHeaderText("Thông tin chi tiết");
            alert.setContentText(details);
            alert.showAndWait();
        }
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            handleViewDetails();
        }
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
