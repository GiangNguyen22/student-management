package com.app.desktopapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.Node;
import java.io.IOException;

public class HomeController {

    @FXML private AnchorPane contentPane;
    @FXML private VBox sidebar;

    @FXML
    private void initialize() {
        loadHome();
        sidebarHoverEffect();
    }

    private void sidebarHoverEffect() {
        for (Node node : sidebar.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;"));
                btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;"));
            }
        }
    }

    @FXML private void loadHome() { loadFXML("/view/home_content.fxml"); }
    @FXML private void loadStudent() { loadFXML("/view/student.fxml"); }
    @FXML private void loadCourse() { loadFXML("/view/course.fxml"); }
    @FXML private void loadResult() { loadFXML("/view/result.fxml"); }

    private void loadFXML(String path) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(path));
            contentPane.getChildren().setAll(pane);
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
