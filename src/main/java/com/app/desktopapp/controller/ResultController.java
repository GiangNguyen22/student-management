package com.app.desktopapp.controller;

import com.app.desktopapp.model.Result;
import com.app.desktopapp.service.ApiResponse;
import com.app.desktopapp.service.ResultService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.event.MouseEvent;

public class ResultController {

    @FXML private TableView<Result> tableResult;

    @FXML private TableColumn<Result, String> colStudentCode;
    @FXML private TableColumn<Result, String> colStudentName;
    @FXML private TableColumn<Result, String> colCourseCode;
    @FXML private TableColumn<Result, String> colCourseName;
    @FXML private TableColumn<Result, Double> colScore;
    @FXML private TableColumn<Result, String> colGrade;
    @FXML private TableColumn<Result, String> colSemester;
    @FXML private TableColumn<Result, String> colTimeStudied;

    @FXML private TextField txtSearch;
    @FXML private Button btnSearch;
    @FXML private Button btnPrev;
    @FXML private Button btnNext;
    @FXML private Label lblPage;

    private int currentPage = 0;
    private int totalPages = 1;

    private final ObservableList<Result> data =
            FXCollections.observableArrayList();

    /* ================== INIT ================== */
    @FXML
    public void initialize() {
        setupTable();
        loadResults();
    }

    /* ================== TABLE CONFIG ================== */
    private void setupTable() {
        colStudentCode.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getStudentCode()
                )
        );
        colStudentName.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getStudentName()
                )
        );
        colCourseCode.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getCourseCode()
                )
        );
        colCourseName.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getCourseName()
                )
        );
        colScore.setCellValueFactory(
                c -> new javafx.beans.property.SimpleObjectProperty<>(
                        c.getValue().getScore()
                )
        );
        colGrade.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getGrade()
                )
        );
        colSemester.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getSemester()
                )
        );
        colTimeStudied.setCellValueFactory(
                c -> new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getTimeStudied()
                )
        );

        tableResult.setItems(data);
    }

    /* ================== LOAD ================== */
    private void loadResults() {
        ApiResponse<Result> res =
                ResultService.getResults(currentPage);

        data.setAll(res.getData());
        totalPages = res.getTotalPages();
        updatePagination();
    }

    /* ================== SEARCH ================== */
    @FXML
    private void onSearch() {
        currentPage = 0;
        String keyword = txtSearch.getText();

        ApiResponse<Result> res =
                ResultService.searchResults(keyword, currentPage);

        data.setAll(res.getData());
        totalPages = res.getTotalPages();
        updatePagination();
    }

    /* ================== PAGINATION ================== */
    @FXML
    private void onPrev() {
        if (currentPage > 0) {
            currentPage--;
            reload();
        }
    }

    @FXML
    private void onNext() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            reload();
        }
    }

    private void reload() {
        String keyword = txtSearch.getText();
        if (keyword != null && !keyword.isBlank()) {
            onSearch();
        } else {
            loadResults();
        }
    }

    private void updatePagination() {
        if(lblPage!=null){
            lblPage.setText(
                    (currentPage + 1) + " / " + totalPages
            );
            btnPrev.setDisable(currentPage == 0);
            btnNext.setDisable(currentPage >= totalPages - 1);
        }

    }
//    @FXML
//    private void handleSearch() {
//        String searchText = txtSearch.getText().toLowerCase().trim();
//
//        if (searchText.isEmpty()) {
//            filteredResults.setAll(results);
//        } else {
//            filteredResults.setAll(results.stream()
//                    .filter(r -> r.getStudentCode().toLowerCase().contains(searchText) ||
//                            r.getStudentName().toLowerCase().contains(searchText) ||
//                            r.getCourse().toLowerCase().contains(searchText))
//                    .collect(Collectors.toList()));
//        }
//
//        tableResult.setItems(filteredResults);
//        updateStatistics();
//    }

//    @FXML
//    private void handleRefresh() {
//        txtSearch.clear();
//        filteredResults.setAll(results);
//        tableResult.setItems(filteredResults);
//        tableResult.getSelectionModel().clearSelection();
//        updateStatistics();
//        showInfoAlert("Đã làm mới danh sách kết quả!");
//    }
//
//    @FXML
//    private void handleAddResult() {
//        showInfoAlert("Chức năng thêm kết quả sẽ được phát triển sau!");
//    }
//
//    @FXML
//    private void handleEditResult() {
//        Result selected = tableResult.getSelectionModel().getSelectedItem();
//        if (selected != null) {
//            showInfoAlert("Chỉnh sửa kết quả: " + selected.getStudentName() + " - " + selected.getCourse());
//        }
//    }

//    @FXML
//    private void handleDeleteResult() {
//        Result selected = tableResult.getSelectionModel().getSelectedItem();
//        if (selected != null) {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Xác nhận xóa");
//            alert.setHeaderText("Xóa kết quả");
//            alert.setContentText("Bạn có chắc chắn muốn xóa kết quả của " + selected.getStudentName() + "?");
//
//            if (alert.showAndWait().get() == ButtonType.OK) {
//                results.remove(selected);
//                filteredResults.remove(selected);
//                updateStatistics();
//                showInfoAlert("Đã xóa kết quả thành công!");
//            }
//        }
//    }

//    @FXML
//    private void handleViewDetails() {
//        Result selected = tableResult.getSelectionModel().getSelectedItem();
//        if (selected != null) {
//            String status = selected.getScore() >= 5.0 ? "Đạt" : "Không đạt";
//            String details = String.format(
//                    "MSSV: %s\n" +
//                            "Họ tên: %s\n" +
//                            "Môn học: %s\n" +
//                            "Điểm: %.1f\n" +
//                            "Trạng thái: %s",
//                    selected.getStudentCode(), selected.getStudentName(),
//                    selected.getCourse(), selected.getScore(), status
//            );
//
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Chi tiết kết quả");
//            alert.setHeaderText("Thông tin chi tiết");
//            alert.setContentText(details);
//            alert.showAndWait();
//        }
//    }

//    @FXML
//    private void handleTableClick(MouseEvent event) {
//        if (event.getClickCount() == 2) {
//            handleViewDetails();
//        }
//    }
}
