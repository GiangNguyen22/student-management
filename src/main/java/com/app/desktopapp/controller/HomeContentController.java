package com.app.desktopapp.controller;

import com.app.desktopapp.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class HomeContentController {

    @FXML private Label lblTotalStudents;
    @FXML private Label lblTotalCourses;
    @FXML private Label lblTotalResults;
    @FXML private Label lblAvgScore;
    
    @FXML private TableView<Student> tableRecentStudents;
    @FXML private TableColumn<Student, String> colRecentCode;
    @FXML private TableColumn<Student, String> colRecentName;
    @FXML private TableColumn<Student, String> colRecentMajor;

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
        recentStudents.addAll(
            new Student("SV012", "Lý Thị Mai", "lythim@student.edu.vn", "KT", LocalDate.of(2000,12,3), "2018", "Nữ"),
            new Student("SV011", "Tạ Văn Long", "tavanl@student.edu.vn", "CNTT", LocalDate.of(2001,10,12), "2019", "Nam"),
            new Student("SV010", "Dương Thị Kim", "duongthij@student.edu.vn", "CNTT", LocalDate.of(2000,8,7), "2018", "Nữ"),
            new Student("SV009", "Ngô Văn Ích", "ngovani@student.edu.vn", "KT", LocalDate.of(2001,6,14), "2019", "Nam"),
            new Student("SV008", "Bùi Thị Hoa", "buithih@student.edu.vn", "CNTT", LocalDate.of(2000,4,25), "2018", "Nữ")
        );
        
        tableRecentStudents.setItems(recentStudents);
        
        lblTotalStudents.setText("156");
        lblTotalCourses.setText("24");
        lblTotalResults.setText("468");
        lblAvgScore.setText("7.8");
    }
}