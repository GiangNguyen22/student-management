package com.app.desktopapp;

import com.app.desktopapp.utils.SceneUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        SceneUtil.setStage(stage);

        Parent root = FXMLLoader.load(
                getClass().getResource("/view/login.fxml")
        );

        stage.setTitle("Student Management");
        stage.setScene(new Scene(root));
        stage.getIcons().add(
                new Image(getClass().getResourceAsStream("/images/app-icon.png"))
        );
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
