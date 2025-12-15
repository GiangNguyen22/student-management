package com.app.desktopapp.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneUtil {

    private static Stage primaryStage;

    // gọi 1 lần ở MainApp
    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void switchScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(
                    SceneUtil.class.getResource(fxmlPath)
            );
            primaryStage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
