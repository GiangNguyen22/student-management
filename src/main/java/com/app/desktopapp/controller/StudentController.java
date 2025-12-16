package com.app.desktopapp.controller;

import com.app.desktopapp.model.Student;
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
    @FXML private Label lblRecordCount, lblGenderStats, lblMajorStats;

    private ObservableList<Student> students = FXCollections.observableArrayList();
    private ObservableList<Student> filteredStudents = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTableColumns();
        loadFakeData();
        tableStudent.setItems(students);
        filteredStudents.setAll(students);
        updateStatistics();
        setupSelectionListener();
    }

    private void setupTableColumns() {
        colStudentCode.setCellValueFactory(cellData -> {
            Student student = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(student.getStudentCode());
        });
        
        colName.setCellValueFactory(cellData -> {
            Student student = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(student.getName());
        });
        
        colEmail.setCellValueFactory(cellData -> {
            Student student = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(student.getEmail());
        });
        
        colMajor.setCellValueFactory(cellData -> {
            Student student = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(student.getMajor());
        });
        
        colDob.setCellValueFactory(cellData -> {
            Student student = cellData.getValue();
            return new javafx.beans.property.SimpleObjectProperty<>(student.getDob());
        });
        
        colStartYear.setCellValueFactory(cellData -> {
            Student student = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(student.getStartYear());
        });
        
        colGender.setCellValueFactory(cellData -> {
            Student student = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(student.getGender());
        });
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

    private void loadFakeData() {
        students.clear();
        students.addAll(
            new Student("SV001", "Nguyễn Văn An", "nguyenvana@student.edu.vn", "CNTT", LocalDate.of(2000,1,15), "2018", "Nam"),
            new Student("SV002", "Trần Thị Bình", "tranthib@student.edu.vn", "KT", LocalDate.of(2000,3,22), "2018", "Nữ"),
            new Student("SV003", "Lê Văn Cường", "levanc@student.edu.vn", "CNTT", LocalDate.of(2001,5,8), "2019", "Nam"),
            new Student("SV004", "Phạm Thị Dung", "phamthid@student.edu.vn", "QTKD", LocalDate.of(2000,7,12), "2018", "Nữ"),
            new Student("SV005", "Hoàng Văn Em", "hoangvane@student.edu.vn", "CNTT", LocalDate.of(2001,9,30), "2019", "Nam"),
            new Student("SV006", "Vũ Thị Phương", "vuthif@student.edu.vn", "KT", LocalDate.of(2000,11,5), "2018", "Nữ"),
            new Student("SV007", "Đỗ Văn Giang", "dovang@student.edu.vn", "QTKD", LocalDate.of(2001,2,18), "2019", "Nam"),
            new Student("SV008", "Bùi Thị Hoa", "buithih@student.edu.vn", "CNTT", LocalDate.of(2000,4,25), "2018", "Nữ"),
            new Student("SV009", "Ngô Văn Ích", "ngovani@student.edu.vn", "KT", LocalDate.of(2001,6,14), "2019", "Nam"),
            new Student("SV010", "Dương Thị Kim", "duongthij@student.edu.vn", "CNTT", LocalDate.of(2000,8,7), "2018", "Nữ"),
            new Student("SV011", "Tạ Văn Long", "tavanl@student.edu.vn", "CNTT", LocalDate.of(2001,10,12), "2019", "Nam"),
            new Student("SV012", "Lý Thị Mai", "lythim@student.edu.vn", "KT", LocalDate.of(2000,12,3), "2018", "Nữ")
        );
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

    // Event Handlers
    @FXML
    private void handleSearch() {
        String searchText = txtSearch.getText().toLowerCase().trim();
        
        if (searchText.isEmpty()) {
            filteredStudents.setAll(students);
        } else {
            filteredStudents.setAll(students.stream()
                .filter(s -> s.getStudentCode().toLowerCase().contains(searchText) ||
                           s.getName().toLowerCase().contains(searchText) ||
                           s.getEmail().toLowerCase().contains(searchText) ||
                           s.getMajor().toLowerCase().contains(searchText))
                .collect(Collectors.toList()));
        }
        
        tableStudent.setItems(filteredStudents);
        updateStatistics();
    }

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        filteredStudents.setAll(students);
        tableStudent.setItems(filteredStudents);
        tableStudent.getSelectionModel().clearSelection();
        updateStatistics();
        showInfoAlert("Đã làm mới danh sách sinh viên!");
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
                "MSSV: %s\n" +
                "Họ tên: %s\n" +
                "Email: %s\n" +
                "Chuyên ngành: %s\n" +
                "Ngày sinh: %s\n" +
                "Khóa: %s\n" +
                "Giới tính: %s",
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
