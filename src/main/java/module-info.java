module com.app.desktopapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;

    opens com.app.desktopapp to javafx.fxml;

    opens com.app.desktopapp.controller to javafx.fxml;

    opens com.app.desktopapp.model to com.fasterxml.jackson.databind;

    opens com.app.desktopapp.dto to com.fasterxml.jackson.databind;

    exports com.app.desktopapp;

    exports com.app.desktopapp.model;
}