package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class dashboardController implements Initializable {

    @FXML
    private BorderPane main_border_pane;


    @FXML
    private Button btn_blok_lapas;

    @FXML
    private Button btn_bon_napi;

    @FXML
    private Button btn_riwayat_bon;


    @FXML
    private Button btn_user_admin;

    @FXML
    private Button btn_data_pegawai;

    @FXML
    private Button btn_data_napi;

    @FXML
    private Button btn_data_subagian;

    @FXML
    private Button btn_blok_kamar;

    @FXML
    private Button btnLogout;

    @FXML
    void goToBlokLapas(ActionEvent event) {
        load_ui("views/blok_lapas.fxml");
    }

    @FXML
    void goToBonNapi(ActionEvent event) {
        load_ui("views/bon_napi.fxml");
    }

    @FXML
    void goToRiwayatBon(ActionEvent event) {
        load_ui("views/bon_riwayat.fxml");
    }

    @FXML
    void goTOUserAdmin(ActionEvent event) {
        load_ui("views/user_admin.fxml");
    }

    @FXML
    void goToBlokKamar(ActionEvent event) {
        load_ui("views/data_blok_kamar.fxml");
    }


    @FXML
    void goToDataNapi(ActionEvent event) {
        load_ui("views/data_napi.fxml");
    }

    @FXML
    void goToDataPegawai(ActionEvent event) {
        load_ui("views/data_pegawai.fxml");

    }

    @FXML
    void goToDataSubagian(ActionEvent event) {
        load_ui("views/data_subagian.fxml");
    }


    @FXML
    void logout(ActionEvent event) {

    }

    private void load_ui(String ui) {

        try {
            Parent stage_area = null;
            stage_area = FXMLLoader.load(getClass().getResource(ui));
            main_border_pane.setCenter(stage_area);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ShareVariable.setMainDashboardBoderpane(main_border_pane);
        load_ui("views/blok_lapas.fxml");

    }
}
