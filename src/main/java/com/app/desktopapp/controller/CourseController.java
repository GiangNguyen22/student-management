package com.app.desktopapp.controller;

import com.app.desktopapp.model.Course;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.stream.Collectors;

public class CourseController {

    @FXML private TextField txtSearch;
    @FXML private TableView<Course> tableCourse;
    @FXML private TableColumn<Course, String> colCourseCode;
    @FXML private TableColumn<Course, String> colCourseName;
    @FXML private TableColumn<Course, Integer> colCredits;
    @FXML private TableColumn<Course, String> colSemester;

    @FXML private Button btnEdit, btnDelete, btnView;
    @FXML private Label lblRecordCount, lblTotalCredits, lblSemesterStats;

    private ObservableList<Course> courses = FXCollections.observableArrayList();
    private ObservableList<Course> filteredCourses = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTableColumns();
        loadFakeData();
        tableCourse.setItems(courses);
        filteredCourses.setAll(courses);
        updateStatistics();
        setupSelectionListener();
    }

    private void setupTableColumns() {
        // Sử dụng callback function thay vì PropertyValueFactory
        colCourseCode.setCellValueFactory(cellData -> {
            Course course = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(course.getCode());
        });

        colCourseName.setCellValueFactory(cellData -> {
            Course course = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(course.getName());
        });

        colCredits.setCellValueFactory(cellData -> {
            Course course = cellData.getValue();
            return new javafx.beans.property.SimpleIntegerProperty(course.getCredits()).asObject();
        });

        colSemester.setCellValueFactory(cellData -> {
            Course course = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(course.getSemester());
        });
    }

    private void setupSelectionListener() {
        tableCourse.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                boolean hasSelection = newSelection != null;
                btnEdit.setDisable(!hasSelection);
                btnDelete.setDisable(!hasSelection);
                btnView.setDisable(!hasSelection);
            }
        );
    }

    private void loadFakeData() {
        courses.clear();
        courses.addAll(
            new Course("MH001", "Toán cao cấp 1", 3, "Kỳ 1"),
            new Course("MH002", "Lập trình Java cơ bản", 4, "Kỳ 1"),
            new Course("MH003", "Cơ sở dữ liệu", 3, "Kỳ 1"),
            new Course("MH004", "Mạng máy tính", 3, "Kỳ 1"),
            new Course("MH005", "Toán cao cấp 2", 3, "Kỳ 2"),
            new Course("MH006", "Lập trình Web", 4, "Kỳ 2"),
            new Course("MH007", "Phân tích thiết kế hệ thống", 3, "Kỳ 2"),
            new Course("MH008", "Hệ điều hành", 3, "Kỳ 2"),
            new Course("MH009", "Trí tuệ nhân tạo", 3, "Kỳ 3"),
            new Course("MH010", "Bảo mật thông tin", 3, "Kỳ 3"),
            new Course("MH011", "Thiết kế UI/UX", 3, "Kỳ 3"),
            new Course("MH012", "Lập trình Mobile", 4, "Kỳ 3"),
            new Course("MH013", "Cloud Computing", 3, "Kỳ 4"),
            new Course("MH014", "DevOps & CI/CD", 3, "Kỳ 4"),
            new Course("MH015", "Đồ án tốt nghiệp", 5, "Kỳ 4")
        );
    }

    private void updateStatistics() {
        lblRecordCount.setText(filteredCourses.size() + "/" + courses.size() + " khóa học");

        int totalCredits = filteredCourses.stream().mapToInt(Course::getCredits).sum();
        lblTotalCredits.setText("Tổng tín chỉ: " + totalCredits);

        long ky1Count = filteredCourses.stream().filter(c -> "Kỳ 1".equals(c.getSemester())).count();
        long ky2Count = filteredCourses.stream().filter(c -> "Kỳ 2".equals(c.getSemester())).count();
        long ky3Count = filteredCourses.stream().filter(c -> "Kỳ 3".equals(c.getSemester())).count();
        long ky4Count = filteredCourses.stream().filter(c -> "Kỳ 4".equals(c.getSemester())).count();
        lblSemesterStats.setText("Kỳ 1: " + ky1Count + " | Kỳ 2: " + ky2Count + " | Kỳ 3: " + ky3Count + " | Kỳ 4: " + ky4Count);
    }

    // Event Handlers
    @FXML
    private void handleSearch() {
        String searchText = txtSearch.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            filteredCourses.setAll(courses);
        } else {
            filteredCourses.setAll(courses.stream()
                .filter(c -> c.getCode().toLowerCase().contains(searchText) ||
                           c.getName().toLowerCase().contains(searchText) ||
                           c.getSemester().toLowerCase().contains(searchText))
                .collect(Collectors.toList()));
        }

        tableCourse.setItems(filteredCourses);
        updateStatistics();
    }

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        filteredCourses.setAll(courses);
        tableCourse.setItems(filteredCourses);
        tableCourse.getSelectionModel().clearSelection();
        updateStatistics();
        showInfoAlert("Đã làm mới danh sách khóa học!");
    }

    @FXML
    private void handleAddCourse() {
        showInfoAlert("Chức năng thêm khóa học sẽ được phát triển sau!");
    }

    @FXML
    private void handleEditCourse() {
        Course selected = tableCourse.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showInfoAlert("Chỉnh sửa khóa học: " + selected.getName());
        }
    }

    @FXML
    private void handleDeleteCourse() {
        Course selected = tableCourse.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Xóa khóa học");
            alert.setContentText("Bạn có chắc chắn muốn xóa khóa học " + selected.getName() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                courses.remove(selected);
                filteredCourses.remove(selected);
                updateStatistics();
                showInfoAlert("Đã xóa khóa học thành công!");
            }
        }
    }

    @FXML
    private void handleViewDetails() {
        Course selected = tableCourse.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String details = String.format(
                "Mã khóa học: %s\n" +
                "Tên khóa học: %s\n" +
                "Số tín chỉ: %d\n" +
                "Học kỳ: %s",
                selected.getCode(), selected.getName(), selected.getCredits(), selected.getSemester()
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Chi tiết khóa học");
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
