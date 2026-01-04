package com.app.desktopapp.controller;

import com.app.desktopapp.controller.action.AddResultDialogController;
import com.app.desktopapp.controller.action.EditResultDialogController;
import com.app.desktopapp.model.Result;
import com.app.desktopapp.service.ResultService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class ResultController {

    @FXML
    private TableView<Result> tableResult;

    @FXML
    private TableColumn<Result, String> colStudentCode;
    @FXML
    private TableColumn<Result, String> colStudentName;
    @FXML
    private TableColumn<Result, String> colCourseCode;
    @FXML
    private TableColumn<Result, String> colCourseName;
    @FXML
    private TableColumn<Result, Double> colScore;
    @FXML
    private TableColumn<Result, String> colGrade;
    @FXML
    private TableColumn<Result, String> colSemester;
    @FXML
    private TableColumn<Result, String> colTimeStudied;

    @FXML
    private Button btnView;
    @FXML
    private Label lblRecordCount, lblAverageScore, lblPassFailStats;
    @FXML
    private TextField txtSearch;

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
        colStudentCode.setCellValueFactory(
                cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getStudentCode()));
        colStudentName.setCellValueFactory(
                cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getStudentName()));
        colCourseCode.setCellValueFactory(
                cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCourseCode()));
        colCourseName.setCellValueFactory(
                cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCourseName()));
        colScore.setCellValueFactory(
                cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getScore()).asObject());
        colGrade.setCellValueFactory(
                cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getGrade()));
        colSemester.setCellValueFactory(
                cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getSemester()));
        colTimeStudied.setCellValueFactory(
                cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTimeStudied()));

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
            // boolean hasSelection = newSel != null;
            // btnEdit.setDisable(!hasSelection);
            // btnDelete.setDisable(!hasSelection);
            btnView.setDisable(newSel == null);
        });
    }

    private void loadResultsFromApi() {
        results.clear();
        List<Result> list = ResultService.getAllResults();
        if (list != null)
            results.addAll(list);
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
        if (txt.isEmpty())
            filteredResults.setAll(results);
        else
            filteredResults.setAll(results.stream()
                    .filter(r -> r.getStudentCode().toLowerCase().contains(txt) ||
                            r.getStudentName().toLowerCase().contains(txt) ||
                            r.getCourseCode().toLowerCase().contains(txt) ||
                            r.getCourseName().toLowerCase().contains(txt))
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
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/modal/add-result-modal.fxml"));
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Thêm kết quả học tập");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tableResult.getScene().getWindow());
            dialog.getIcons().add(
                    new Image(getClass().getResourceAsStream("/images/logo.png")));

            Scene scene = new Scene(root);
            dialog.setScene(scene);

            AddResultDialogController controller = loader.getController();
            controller.setStage(dialog);

            dialog.showAndWait();

            if (controller.isSaved()) {
                loadResultsFromApi();
                showInfoAlert("Thêm kết quả thành công!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showInfoAlert("Thêm kết quả thất bại!");
        }
    }

    @FXML
    private void handleEditResult() {
        Result sel = tableResult.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showInfoAlert("Vui lòng chọn kết quả cần chỉnh sửa!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/view/modal/edit-result-modal.fxml"));
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Chỉnh sửa kết quả học tập");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(tableResult.getScene().getWindow());
            dialog.getIcons().add(
                    new Image(getClass().getResourceAsStream("/images/logo.png")));

            Scene scene = new Scene(root);
            dialog.setScene(scene);

            EditResultDialogController controller = loader.getController();
            controller.setResult(sel);

            dialog.showAndWait();

            loadResultsFromApi();
            showInfoAlert("Cập nhật kết quả thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            showInfoAlert("Cập nhật kết quả thất bại!");
        }
    }

    @FXML
    private void handleDeleteResult() {
        Result sel = tableResult.getSelectionModel().getSelectedItem();
        if (sel != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Bạn có chắc chắn muốn xóa kết quả của " + sel.getStudentName() + "?");
            alert.setHeaderText("Xóa kết quả");
            if (alert.showAndWait().get() == ButtonType.OK) {
                boolean ok = ResultService.deleteResult(sel.getStudentCode(), sel.getCourseCode());
                if (ok) {
                    results.remove(sel);
                    filteredResults.remove(sel);
                    updateStatistics();
                    showInfoAlert("Xóa kết quả thành công!");
                } else
                    showInfoAlert("Xóa kết quả thất bại!");
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
                    sel.getStudentCode(), sel.getStudentName(), sel.getCourseName(), sel.getScore(), status);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, details);
            alert.setHeaderText("Thông tin chi tiết");
            alert.setTitle("Chi tiết kết quả");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 2)
            handleViewDetails();
    }

    private void showInfoAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
