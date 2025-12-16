package com.app.desktopapp.controller;

import com.app.desktopapp.model.Result;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.stream.Collectors;

public class ResultController {

    @FXML private TextField txtSearch;
    @FXML private TableView<Result> tableResult;
    @FXML private TableColumn<Result, String> colStudentCode;
    @FXML private TableColumn<Result, String> colStudentName;
    @FXML private TableColumn<Result, String> colCourse;
    @FXML private TableColumn<Result, Double> colScore;

    @FXML private Button btnEdit, btnDelete, btnView;
    @FXML private Label lblRecordCount, lblAverageScore, lblPassFailStats;

    private ObservableList<Result> results = FXCollections.observableArrayList();
    private ObservableList<Result> filteredResults = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTableColumns();
        loadFakeData();
        tableResult.setItems(results);
        filteredResults.setAll(results);
        updateStatistics();
        setupSelectionListener();
    }

    private void setupTableColumns() {
        // Sử dụng callback function thay vì PropertyValueFactory
        colStudentCode.setCellValueFactory(cellData -> {
            Result result = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(result.getStudentCode());
        });

        colStudentName.setCellValueFactory(cellData -> {
            Result result = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(result.getStudentName());
        });

        colCourse.setCellValueFactory(cellData -> {
            Result result = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(result.getCourse());
        });

        colScore.setCellValueFactory(cellData -> {
            Result result = cellData.getValue();
            return new javafx.beans.property.SimpleDoubleProperty(result.getScore()).asObject();
        });

        // Format score column to show 1 decimal place
        colScore.setCellFactory(tc -> new TableCell<Result, Double>() {
            @Override
            protected void updateItem(Double score, boolean empty) {
                super.updateItem(score, empty);
                if (empty || score == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f", score));
                }
            }
        });
    }

    private void setupSelectionListener() {
        tableResult.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                boolean hasSelection = newSelection != null;
                btnEdit.setDisable(!hasSelection);
                btnDelete.setDisable(!hasSelection);
                btnView.setDisable(!hasSelection);
            }
        );
    }

    private void loadFakeData() {
        results.clear();
        results.addAll(
            new Result("SV001", "Nguyễn Văn A", "Lập trình Java", 8.5),
            new Result("SV001", "Nguyễn Văn A", "Toán cao cấp", 7.8),
            new Result("SV001", "Nguyễn Văn A", "Cơ sở dữ liệu", 9.2),
            new Result("SV002", "Trần Thị B", "Toán cao cấp", 9.0),
            new Result("SV002", "Trần Thị B", "Lập trình Java", 8.8),
            new Result("SV002", "Trần Thị B", "Cơ sở dữ liệu", 8.5),
            new Result("SV003", "Lê Văn C", "Lập trình Java", 7.5),
            new Result("SV003", "Lê Văn C", "Toán cao cấp", 6.8),
            new Result("SV003", "Lê Văn C", "Cơ sở dữ liệu", 7.2),
            new Result("SV004", "Phạm Thị D", "Lập trình Java", 9.5),
            new Result("SV004", "Phạm Thị D", "Toán cao cấp", 9.8),
            new Result("SV004", "Phạm Thị D", "Cơ sở dữ liệu", 9.0),
            new Result("SV005", "Hoàng Văn E", "Lập trình Java", 8.2),
            new Result("SV005", "Hoàng Văn E", "Toán cao cấp", 7.5),
            new Result("SV005", "Hoàng Văn E", "Cơ sở dữ liệu", 8.0)
        );
    }

    private void updateStatistics() {
        lblRecordCount.setText(filteredResults.size() + "/" + results.size() + " kết quả");

        if (!filteredResults.isEmpty()) {
            double averageScore = filteredResults.stream()
                .mapToDouble(Result::getScore)
                .average()
                .orElse(0.0);
            lblAverageScore.setText("Điểm TB: " + String.format("%.1f", averageScore));
        } else {
            lblAverageScore.setText("Điểm TB: 0.0");
        }

        long passCount = filteredResults.stream().filter(r -> r.getScore() >= 5.0).count();
        long failCount = filteredResults.stream().filter(r -> r.getScore() < 5.0).count();
        lblPassFailStats.setText("Đạt: " + passCount + " | Không đạt: " + failCount);
    }

    // Event Handlers
    @FXML
    private void handleSearch() {
        String searchText = txtSearch.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            filteredResults.setAll(results);
        } else {
            filteredResults.setAll(results.stream()
                .filter(r -> r.getStudentCode().toLowerCase().contains(searchText) ||
                           r.getStudentName().toLowerCase().contains(searchText) ||
                           r.getCourse().toLowerCase().contains(searchText))
                .collect(Collectors.toList()));
        }

        tableResult.setItems(filteredResults);
        updateStatistics();
    }

    @FXML
    private void handleRefresh() {
        txtSearch.clear();
        filteredResults.setAll(results);
        tableResult.setItems(filteredResults);
        tableResult.getSelectionModel().clearSelection();
        updateStatistics();
        showInfoAlert("Đã làm mới danh sách kết quả!");
    }

    @FXML
    private void handleAddResult() {
        showInfoAlert("Chức năng thêm kết quả sẽ được phát triển sau!");
    }

    @FXML
    private void handleEditResult() {
        Result selected = tableResult.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showInfoAlert("Chỉnh sửa kết quả: " + selected.getStudentName() + " - " + selected.getCourse());
        }
    }

    @FXML
    private void handleDeleteResult() {
        Result selected = tableResult.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Xóa kết quả");
            alert.setContentText("Bạn có chắc chắn muốn xóa kết quả của " + selected.getStudentName() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                results.remove(selected);
                filteredResults.remove(selected);
                updateStatistics();
                showInfoAlert("Đã xóa kết quả thành công!");
            }
        }
    }

    @FXML
    private void handleViewDetails() {
        Result selected = tableResult.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String status = selected.getScore() >= 5.0 ? "Đạt" : "Không đạt";
            String details = String.format(
                "MSSV: %s\n" +
                "Họ tên: %s\n" +
                "Môn học: %s\n" +
                "Điểm: %.1f\n" +
                "Trạng thái: %s",
                selected.getStudentCode(), selected.getStudentName(),
                selected.getCourse(), selected.getScore(), status
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Chi tiết kết quả");
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
