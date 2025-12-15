package com.app.desktopapp.controller;

import com.app.desktopapp.model.Student;
import com.app.desktopapp.service.ApiResponse;
import com.app.desktopapp.service.StudentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class StudentController {

    @FXML
    private TableView<Student> tableStudent;
    @FXML private TableColumn<Student, String> colStudentCode;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colEmail;
    @FXML private TableColumn<Student, String> colMajor;
    @FXML private TableColumn<Student, String> colGender;
    @FXML private TableColumn<Student, LocalDate> colDob;
    @FXML private TableColumn<Student, String> colStartYear;

    @FXML private TextField txtSearch;
    @FXML private Label lblPage;

    private int currentPage = 0;
    private int totalPages = 1;

    @FXML
    public void initialize() {
        colStudentCode.setCellValueFactory(new PropertyValueFactory<>("studentCode"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDob.setCellFactory(col -> new TableCell<Student, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
            }
        });
        colMajor.setCellValueFactory(new PropertyValueFactory<>("major"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colStartYear.setCellValueFactory(new PropertyValueFactory<>("startYear"));

        loadData();
    }

    private boolean isSearching = false;

    private void loadData() {
        new Thread(() -> {
            ApiResponse res;
            if (isSearching && !txtSearch.getText().isBlank()) {
                res = StudentService.searchStudents(txtSearch.getText(), currentPage);
            } else {
                res = StudentService.getStudents(currentPage);
            }

            ObservableList<Student> obsList = FXCollections.observableArrayList(res.students);

            // Update UI trên FX thread
            javafx.application.Platform.runLater(() -> {
                tableStudent.setItems(obsList);
                totalPages = res.totalPages;
                lblPage.setText((currentPage + 1) + " / " + totalPages);
            });
        }).start();
    }

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
}

