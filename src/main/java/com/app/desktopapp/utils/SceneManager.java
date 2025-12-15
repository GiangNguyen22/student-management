package com.app.desktopapp.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage stage;

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchScene(Scene scene) {
        stage.setScene(scene);
    }
}
