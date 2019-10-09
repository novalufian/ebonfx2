module ebonpas.javafx {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires java.sql;

    requires mysql.connector.java;

    opens application;
}
