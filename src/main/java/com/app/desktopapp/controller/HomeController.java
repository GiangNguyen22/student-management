package com.app.desktopapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;

public class HomeController {

    @FXML private AnchorPane contentPane;
    @FXML private VBox sidebar;
    private static HomeController instance;

    @FXML
    private void initialize() {
        instance = this;
        Button homeBtn = (Button) sidebar.getChildren().get(1);
        setActiveButton(homeBtn);
        loadFXML("/view/home_content.fxml");
//        sidebarHoverEffect();
    }

    public static HomeController getInstance() {
        return instance;
    }

//    /** Hiệu ứng hover sidebar */
//    private void sidebarHoverEffect() {
//        for (Node node : sidebar.getChildren()) {
//            if (node instanceof Button btn) {
//                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;"));
//                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
//            }
//        }
//    }

    @FXML private void loadHome(ActionEvent event) {
        setActiveButton((Button) event.getSource());
        loadFXML("/view/home_content.fxml"); }
    public void loadStudent(ActionEvent event) {
        setActiveButton((Button) event.getSource());
        loadFXML("/view/student.fxml"); }
    @FXML private void loadCourse(ActionEvent event) {
        setActiveButton((Button) event.getSource());
        loadFXML("/view/course.fxml"); }
    @FXML private void loadResult(ActionEvent event) {
        setActiveButton((Button) event.getSource());
        loadFXML("/view/result.fxml"); }
    @FXML private void loadStaff(ActionEvent event) {
        setActiveButton((Button) event.getSource());
        loadFXML("/view/staff.fxml"); }

    /** Load FXML con vào contentPane và neo đầy đủ */
    private void loadFXML(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent pane = loader.load();  // <-- Sửa từ AnchorPane sang Parent

            contentPane.getChildren().setAll(pane);

            // Neo đầy đủ nếu contentPane vẫn là AnchorPane
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load FXML: " + path);
        }
    }
    private void setActiveButton(Button activeButton) {
        sidebar.getChildren().forEach(node -> {
            if (node instanceof Button btn) {
                btn.getStyleClass().remove("active");
            }
        });
        activeButton.getStyleClass().add("active");
    }

    public void loadStudent() {
        // tìm button "Quản lý Sinh viên" trong sidebar
        for (Node node : sidebar.getChildren()) {
            if (node instanceof Button btn &&
                    "Quản lý Sinh viên".equals(btn.getText())) {

                setActiveButton(btn);
                break;
            }
        }
        loadFXML("/view/student.fxml");
    }


}
