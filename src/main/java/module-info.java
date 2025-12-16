module com.app.desktopapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;

    opens com.app.desktopapp to javafx.fxml;

    opens com.app.desktopapp.controller to javafx.fxml;

    opens com.app.desktopapp.model to javafx.fxml, javafx.base;

    exports com.app.desktopapp;
}