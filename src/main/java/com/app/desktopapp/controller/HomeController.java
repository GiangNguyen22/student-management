package com.app.desktopapp.controller;

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
        loadHome();
        sidebarHoverEffect();
    }

    public static HomeController getInstance() {
        return instance;
    }

    /** Hiệu ứng hover sidebar */
    private void sidebarHoverEffect() {
        for (Node node : sidebar.getChildren()) {
            if (node instanceof Button btn) {
                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;"));
                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
            }
        }
    }

    @FXML private void loadHome() { loadFXML("/view/home_content.fxml"); }
    public void loadStudent() { loadFXML("/view/student.fxml"); }
    @FXML private void loadCourse() { loadFXML("/view/course.fxml"); }
    @FXML private void loadResult() { loadFXML("/view/result.fxml"); }
    @FXML private void loadStaff() { loadFXML("/view/staff.fxml"); }

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
}
