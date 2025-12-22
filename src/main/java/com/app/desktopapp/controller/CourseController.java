package com.app.desktopapp.controller;

import com.app.desktopapp.controller.action.AddCourseController;
import com.app.desktopapp.controller.action.EditCourseModal;
import com.app.desktopapp.model.Course;
import com.app.desktopapp.service.ApiResponse;
import com.app.desktopapp.service.CourseService;
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

public class CourseController {

    @FXML private TextField txtSearch;
    @FXML private TableView<Course> tableCourse;
    @FXML private TableColumn<Course, String> colCourseCode;
    @FXML private TableColumn<Course, String> colCourseName;
    @FXML private TableColumn<Course, Integer> colDesc;
    @FXML private TableColumn<Course, String> colSemester;


    @FXML private Button btnEdit, btnDelete, btnView;
    @FXML private Label lblRecordCount, lblTotalCredits, lblSemesterStats;

    private ObservableList<Course> courses = FXCollections.observableArrayList();

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
        colCourseCode.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("startDate"));
    }

    private void setupSelectionListener() {
        tableCourse.getSelectionModel().selectedItemProperty().addListener(
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
            ApiResponse<Course> res;

            if (isSearching && !txtSearch.getText().isBlank()) {
                res = CourseService.searchCourses(txtSearch.getText(), currentPage);
            } else {
                res = CourseService.getCourses(currentPage);
            }

            Platform.runLater(() -> {
                courses.setAll(res.getData());
                tableCourse.setItems(courses);

                totalPages = res.getTotalPages();
//                updateStatistics();
            });
        }).start();
    }

    /* ================== STATISTICS ================== */
//    private void updateStatistics() {
//        lblRecordCount.setText(courses.size() + " khóa học");
//
//        int totalCredits = courses.stream()
//                .mapToInt(Course::getCredits)
//                .sum();
//        lblTotalCredits.setText("Tổng tín chỉ: " + totalCredits);
//
//        long ky1 = courses.stream().filter(c -> "Kỳ 1".equals(c.getSemester())).count();
//        long ky2 = courses.stream().filter(c -> "Kỳ 2".equals(c.getSemester())).count();
//        long ky3 = courses.stream().filter(c -> "Kỳ 3".equals(c.getSemester())).count();
//        long ky4 = courses.stream().filter(c -> "Kỳ 4".equals(c.getSemester())).count();
//
//        lblSemesterStats.setText(
//                "Kỳ 1: " + ky1 +
//                        " | Kỳ 2: " + ky2 +
//                        " | Kỳ 3: " + ky3 +
//                        " | Kỳ 4: " + ky4
//        );
//    }

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
        showInfoAlert("Đã làm mới danh sách khóa học!");
    }

    @FXML
    private void handleAddCourse() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/modal/add-course-modal.fxml")
            );
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Thêm khóa học");
            dialog.getIcons().add(
                    new Image(getClass().getResourceAsStream("/images/logo.png"))
            );
            dialog.initModality(Modality.APPLICATION_MODAL);

            // set owner (quan trọng)
            Stage owner = (Stage) tableCourse.getScene().getWindow();
            dialog.initOwner(owner);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/css/add-student.css").toExternalForm()
            );
            dialog.setScene(scene);

            AddCourseController controller = loader.getController();
            controller.setStage(dialog);

            dialog.showAndWait();

            if (controller.isSaved()) {
                loadData(); // chỉ reload khi lưu
                System.out.println("Đã thêm: " + controller.getCourseName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditCourse() {
        Course selected = tableCourse.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfoAlert("Vui lòng chọn khóa học cần chỉnh sửa");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/modal/edit-course-modal.fxml"));
            Parent root = loader.load();

            EditCourseModal controller = loader.getController();

            Stage dialog = new Stage();
            dialog.setTitle("Chỉnh sửa khóa học");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.getIcons().add(
                    new Image(getClass().getResourceAsStream("/images/logo.png"))
            );

            controller.setStage(dialog);
            controller.setCourse(selected);

            dialog.showAndWait();

            if (controller.isUpdated()) {
                loadData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleDeleteCourse() {

        Course selected = tableCourse
                .getSelectionModel()
                .getSelectedItem();

        if (selected == null) {
            showInfoAlert("Vui lòng chọn khóa học cần xóa!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Xóa khóa học");
        alert.setContentText(
                "Bạn có chắc chắn muốn xóa khóa học \""
                        + selected.getCourseName() + "\"?"
        );

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

            boolean success =
                    CourseService.deleteCourse(
                            selected.getCourseCode()
                    );

            if (success) {
                showInfoAlert("Đã xóa khóa học thành công!");
                loadData(); // reload từ server
            } else {
                showInfoAlert("Xóa khóa học thất bại!");
            }
        }
    }


    @FXML
    private void handleViewDetails() {
        Course c = tableCourse.getSelectionModel().getSelectedItem();
        if (c != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Chi tiết khóa học");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Mã KH: " + c.getCourseCode() +
                            "\nTên: " + c.getCourseName() +
//                            "\nTín chỉ: " + c.getCredits() +
                            "\nHọc kỳ: " + c.getStartDate()
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
