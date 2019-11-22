module ebonpas.javafx {

    requires javafx.web;
    requires javafx.swing;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    requires controlsfx;

    exports application.controllers;
    exports application.models;

    opens application.views;
    opens application.controllers;
    opens application.models;
    opens application;

}

//module ebonpas.javafx {
//
//
//
////    exports application.controller;
////    exports Application.model;
//
//        opens Application.controller;
//        opens Application.model;
//        opens Application.view;
//        opens Application;
//
//        }