package com.app.desktopapp.controller;

import com.app.desktopapp.model.Result;
import com.app.desktopapp.service.ResultService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.List;
import java.util.stream.Collectors;

public class ResultController {

    @FXML private TableView<Result> tableResult;

    @FXML private TableColumn<Result, String> colStudentCode;
    @FXML private TableColumn<Result, String> colStudentName;
    @FXML private TableColumn<Result, String> colCourseCode;
    @FXML private TableColumn<Result, String> colCourseName;
    @FXML private TableColumn<Result, Double> colScore;

    @FXML private Button btnEdit, btnDelete, btnView;
    @FXML private Label lblRecordCount, lblAverageScore, lblPassFailStats;

    private ObservableList<Result> results = FXCollections.observableArrayList();
    private ObservableList<Result> filteredResults = FXCollections.observableArrayList();

    /* ================== INIT ================== */
    @FXML
    private void initialize() {
        setupTableColumns();
        loadResultsFromApi();
        tableResult.setItems(filteredResults);
        setupSelectionListener();
    }

    private void setupTableColumns() {
        colStudentCode.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getStudentCode()));
        colStudentName.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getStudentName()));
        colCourse.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCourse()));
        colScore.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getScore()).asObject());

        colScore.setCellFactory(tc -> new TableCell<Result, Double>() {
            @Override
            protected void updateItem(Double score, boolean empty) {
                super.updateItem(score, empty);
                setText(empty || score == null ? null : String.format("%.1f", score));
            }
        });
    }

    private void setupSelectionListener() {
        tableResult.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean hasSelection = newSel != null;
            btnEdit.setDisable(!hasSelection);
            btnDelete.setDisable(!hasSelection);
            btnView.setDisable(!hasSelection);
        });
    }

    private void loadResultsFromApi() {
        results.clear();
        List<Result> list = ResultService.getAllResults();
        if (list != null) results.addAll(list);
        filteredResults.setAll(results);
        updateStatistics();
    }

    private void updateStatistics() {
        lblRecordCount.setText(filteredResults.size() + "/" + results.size() + " kết quả");
        double avg = filteredResults.stream().mapToDouble(Result::getScore).average().orElse(0.0);
        lblAverageScore.setText("Điểm TB: " + String.format("%.1f", avg));
        long pass = filteredResults.stream().filter(r -> r.getScore() >= 5.0).count();
        long fail = filteredResults.stream().filter(r -> r.getScore() < 5.0).count();
        lblPassFailStats.setText("Đạt: " + pass + " | Không đạt: " + fail);
    }

    @FXML
    private void handleSearch() {
        String txt = txtSearch.getText().toLowerCase().trim();
        if (txt.isEmpty()) filteredResults.setAll(results);
        else filteredResults.setAll(results.stream()
                .filter(r -> r.getStudentCode().toLowerCase().contains(txt) ||
                             r.getStudentName().toLowerCase().contains(txt) ||
                             r.getCourse().toLowerCase().contains(txt))
                .collect(Collectors.toList()));
        updateStatistics();
    }

    /* ================== PAGINATION ================== */
    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        loadResultsFromApi();
        tableResult.getSelectionModel().clearSelection();
        showInfoAlert("Đã làm mới danh sách kết quả!");
    }

    @FXML
    private void handleAddResult() {
        // TODO: mở Dialog nhập liệu thực tế
        Result newResult = new Result("SVXXX", "Sinh viên mới", "Môn học mới", 0.0);
        Result created = ResultService.createResult(newResult);
        if (created != null) {
            results.add(created);
            filteredResults.setAll(results);
            updateStatistics();
            showInfoAlert("Thêm kết quả thành công!");
        }
    }

    @FXML
    private void handleEditResult() {
        Result sel = tableResult.getSelectionModel().getSelectedItem();
        if (sel != null) {
            // TODO: mở Dialog sửa điểm
            sel.setScore(sel.getScore() + 0.1);
            Result updated = ResultService.updateResult(sel);
            if (updated != null) {
                int idx = results.indexOf(sel);
                results.set(idx, updated);
                filteredResults.setAll(results);
                updateStatistics();
                showInfoAlert("Cập nhật kết quả thành công!");
            }
        }
    }

    @FXML
    private void handleDeleteResult() {
        Result sel = tableResult.getSelectionModel().getSelectedItem();
        if (sel != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa kết quả của " + sel.getStudentName() + "?");
            alert.setHeaderText("Xóa kết quả");
            if (alert.showAndWait().get() == ButtonType.OK) {
                boolean ok = ResultService.deleteResult(sel.getStudentCode(), sel.getCourse());
                if (ok) {
                    results.remove(sel);
                    filteredResults.remove(sel);
                    updateStatistics();
                    showInfoAlert("Xóa kết quả thành công!");
                } else showInfoAlert("Xóa kết quả thất bại!");
            }
        }
    }

    @FXML
    private void handleViewDetails() {
        Result sel = tableResult.getSelectionModel().getSelectedItem();
        if (sel != null) {
            String status = sel.getScore() >= 5.0 ? "Đạt" : "Không đạt";
            String details = String.format(
                    "MSSV: %s\nHọ tên: %s\nMôn học: %s\nĐiểm: %.1f\nTrạng thái: %s",
                    sel.getStudentCode(), sel.getStudentName(), sel.getCourse(), sel.getScore(), status
            );
            Alert alert = new Alert(Alert.AlertType.INFORMATION, details);
            alert.setHeaderText("Thông tin chi tiết");
            alert.setTitle("Chi tiết kết quả");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 2) handleViewDetails();
    }

    private void showInfoAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
