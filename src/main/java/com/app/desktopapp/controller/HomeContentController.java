package com.app.desktopapp.controller;

import com.app.desktopapp.model.Student;
import com.app.desktopapp.service.StudentService;
import com.app.desktopapp.service.CourseService;
import com.app.desktopapp.service.ResultService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class HomeContentController {

    @FXML private Label lblTotalStudents;
    @FXML private Label lblTotalCourses;
    @FXML private Label lblTotalResults;
    
    @FXML private TableView<Student> tableRecentStudents;
    @FXML private TableColumn<Student, String> colRecentCode;
    @FXML private TableColumn<Student, String> colRecentName;
    @FXML private TableColumn<Student, String> colRecentMajor;

    @FXML private Button btnViewAll;

    private ObservableList<Student> recentStudents = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupTableColumns();
        loadDashboardData();
    }

    private void setupTableColumns() {
        colRecentCode.setCellValueFactory(cellData -> {
            Student student = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(student.getStudentCode());
        });
        
        colRecentName.setCellValueFactory(cellData -> {
            Student student = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(student.getName());
        });
        
        colRecentMajor.setCellValueFactory(cellData -> {
            Student student = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(student.getMajor());
        });
    }

    private void loadDashboardData() {
        // Load dashboard statistics
        loadDashboardStats();
        
        // Load recent students (5 newest)
        loadRecentStudents();
    }

    private void loadDashboardStats() {
        try {
            // Get total students count
            var studentResponse = StudentService.getStudents(0);
            int totalStudents = 0;
            if (studentResponse != null && studentResponse.getData() != null) {
                totalStudents = studentResponse.getTotalPages() * 20;
                if (!studentResponse.getData().isEmpty()) {
                    totalStudents = (studentResponse.getTotalPages() - 1) * 20 + studentResponse.getData().size();
                }
            }
            lblTotalStudents.setText(String.valueOf(totalStudents));

            // Get total courses count
            var courseResponse = CourseService.getCourses(0);
            int totalCourses = 0;
            if (courseResponse != null && courseResponse.getData() != null) {
                totalCourses = courseResponse.getTotalPages() * 20;
                if (!courseResponse.getData().isEmpty()) {
                    totalCourses = (courseResponse.getTotalPages() - 1) * 20 + courseResponse.getData().size();
                }
            }
            lblTotalCourses.setText(String.valueOf(totalCourses));

            // Get total results count
            var results = ResultService.getAllResults();
            int totalResults = results != null ? results.size() : 0;
            lblTotalResults.setText(String.valueOf(totalResults));

        } catch (Exception e) {
            e.printStackTrace();
            // Set default values if API fails
            lblTotalStudents.setText("0");
            lblTotalCourses.setText("0");
            lblTotalResults.setText("0");
        }
    }

    private void loadRecentStudents() {
        try {
            // Get first page with recent students (assuming they are sorted by creation date)
            var response = StudentService.getStudents(0);
            if (response != null && response.getData() != null) {
                List<Student> allStudents = response.getData();
                // Take only first 5 students (newest)
                recentStudents.clear();
                recentStudents.addAll(allStudents.stream().limit(5).toList());
                tableRecentStudents.setItems(recentStudents);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Clear table if API fails
            recentStudents.clear();
            tableRecentStudents.setItems(recentStudents);
        }
    }

    @FXML
    private void handleViewAllStudents() {
        HomeController.getInstance().loadStudent();
    }
}