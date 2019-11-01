package application.controllers;

import application.Main;
import application.models.ShareVariable;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    Stage curentStage = Main.getStage();

    @FXML
    private Text usernameText;

    @FXML
    private AnchorPane mainAnchorpane;

    @FXML
    private Button blokLapas;

    @FXML
    private Button bonNapi;

    @FXML
    private Button user;

    @FXML
    private Button riwayat;

    @FXML
    private Button napi;

    @FXML
    private Button pegawai;

    @FXML
    private Button subagian;

    @FXML
    private Button blokkamar;

    @FXML
    private Button logout;

    @FXML
    private Button notif;

    @FXML
    void doBlokKamar(ActionEvent event) {
        load_ui("/application/views/blok_kamar.fxml");

    }

    @FXML
    void doBonNapi(ActionEvent event) {
        load_ui("/application/views/napi.fxml");

    }

    @FXML
    void doDataNapi(ActionEvent event) {
        load_ui("/application/views/napi_data.fxml");

    }

    @FXML
    void doDataSubagian(ActionEvent event) {
        load_ui("/application/views/subagian.fxml");

    }

    @FXML
    void doDatapegawai(ActionEvent event) {
        load_ui("/application/views/pegawai.fxml");

    }

    @FXML
    void doLogout(ActionEvent event) {
        try {
            Parent dashboard = FXMLLoader.load(getClass().getResource("/application/views/login.fxml"));
            curentStage.setScene(new Scene(dashboard, 600, 400));

            curentStage.show();

            ShareVariable.setLoginid(null);
            ShareVariable.setSharehome(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doRiwayat(ActionEvent event) {
        load_ui("/application/views/riwayat.fxml");

    }

    @FXML
    void doUser(ActionEvent event) {
        load_ui("/application/views/admin.fxml");
    }

    @FXML
    void donNotif(ActionEvent event) {
//        load_ui("/application/views/lapas.fxml");
    }

    @FXML
    void dpBlokLapas(ActionEvent event) {
        load_ui("/application/views/lapas.fxml");
    }

    private void load_ui(String ui) {

        try {
            Parent dashboard = FXMLLoader.load(getClass().getResource(ui));
            curentStage.setScene(new Scene(dashboard));
            curentStage.show();


//            curentStage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(()->{
            usernameText.setText(ShareVariable.getUsername());

        });

        mainAnchorpane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        mainAnchorpane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());


    }
}
