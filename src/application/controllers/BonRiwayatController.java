package application.controllers;

import application.Main;
import application.models.ModelBon;
import application.models.ShareVariable;
import application.connectifity.ConnectionClass;
import application.connectifity.Template_error;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class BonRiwayatController implements Initializable {

    private static Template_error template_error = new Template_error();

    @FXML
    private AnchorPane mainAnchorpane;

    @FXML
    private TableView<ModelBon> tableBonRiwayat;

    @FXML
    private TableColumn<ModelBon, String> bonid;

    @FXML
    private TableColumn<ModelBon, String> keterangan;

    @FXML
    private TableColumn<ModelBon, String> status;

    @FXML
    private TableColumn<ModelBon, String> subagian;

    @FXML
    private TableColumn<ModelBon, String> jammasuk;

    @FXML
    private TableColumn<ModelBon, String> jamkeluar;

    @FXML
    private TableColumn<ModelBon, String> pengajuan;

    @FXML
    private TableColumn<ModelBon, Button> action;

    @FXML
    private TableColumn<ModelBon, Button> prosesbtn;

    @FXML
    private TextField search;

    @FXML
    private Button btnSearch;

    @FXML
    private Button kembali;

    @FXML
    void doKembali(ActionEvent event) {
        try {
            Stage curentStage = Main.getStage();
            Parent dashboard = FXMLLoader.load(getClass().getResource(ShareVariable.getSharehome()));
            curentStage.setScene(new Scene(dashboard));
            curentStage.show();
//            curentStage.setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doSearch(KeyEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        createTable();

        Stage curentStage = Main.getStage();
        mainAnchorpane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        mainAnchorpane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
    }

    private void initTable(){
        initColumn();
    }

    private void initColumn(){

        bonid.setCellValueFactory(new PropertyValueFactory<>("bonid"));
        keterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        subagian.setCellValueFactory(new PropertyValueFactory<>("bonSubagian"));
        jammasuk.setCellValueFactory(new PropertyValueFactory<>("jam_masuk"));
        jamkeluar.setCellValueFactory(new PropertyValueFactory<>("jam_keluar"));
        pengajuan.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        action.setCellValueFactory(new PropertyValueFactory<>("view"));
        prosesbtn.setCellValueFactory(new PropertyValueFactory<>("proses"));
        createaBtnView("view");
        createaBtnView("proses");

    }

    private void createTable(){
        ObservableList<ModelBon> dataRiwayatBon = FXCollections.observableArrayList();
        String isId = "";
        String userId = ShareVariable.getUserid();
        if (isId != ""){
            isId = "WHERE bon.bon_user = "+userId;
        }
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        try{
            String sql = "SELECT * FROM bon " +
                    "LEFT JOIN data_pegawai ON data_pegawai.user_id = bon.bon_user " +
                    "LEFT JOIN master_subagian ON master_subagian.subagian_id = bon.bon_subagian " +
                    isId +
                    "ORDER BY bon.bon_created_at DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                dataRiwayatBon.add(new ModelBon(
                        rs.getString("bon_id"),
                        rs.getString("bon_keterangan"),
                        rs.getString("bon_status"),
                        rs.getString("nama"),
                        rs.getString("bon_jam_masuk"),
                        rs.getString("bon_jam_keluar"),
                        rs.getString("bon_created_at"),
                        new Button("view"),
                        new Button("proses")
                ));

            }
        }catch(Exception e){
            e.printStackTrace();

        }
        tableBonRiwayat.setItems(dataRiwayatBon);

        FilteredList<ModelBon> filteredList = new FilteredList<>(dataRiwayatBon, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(model_barang -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (model_barang.getBonid().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(model_barang.getKeterangan().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                    return true;
                }else if(model_barang.getBonSubagian().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else{
                    return false;
                }
            });
        });

        SortedList<ModelBon> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableBonRiwayat.comparatorProperty());

        tableBonRiwayat.setItems(sortedList);
    }

    private void createaBtnView(String typebtn){
        Callback<TableColumn<ModelBon, Button>, TableCell<ModelBon, Button>> cellFactory =
                new Callback<TableColumn<ModelBon, Button>, TableCell<ModelBon, Button>>() {

                    @Override
                    public TableCell<ModelBon, Button> call(TableColumn<ModelBon, Button> param) {

                        final TableCell<ModelBon, Button> cell = new TableCell<ModelBon, Button>(){

                            final  Button btn = new Button(typebtn);

                            @Override
                            public void updateItem(Button item, boolean empty){
                                super.updateItem(item, empty);

                                if (empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    btn.setOnAction(event -> {
                                        ModelBon data = getTableView().getItems().get(getIndex());
                                        switch (typebtn){
                                            case "view":
                                                break;
                                            case "proses":
                                                initBtnAction(data);
                                                break;
                                        }
                                    });

                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        switch (typebtn){
            case "view":
                action.setCellFactory(cellFactory);
                break;
            case "proses":
                prosesbtn.setCellFactory(cellFactory);
                break;
        }
    }

    void initBtnAction(ModelBon obj){
        switch (obj.getStatus()){
            case "menunggu" :
                doKonfirmasi(obj);
                break;
            case "terima":
                doTerima(obj);
                break;
            case "ditolak":
                template_error.warning("Pemberitahuan","bon anda telah ditolak, silahkan melakukan bon lagi");
                break;
            case "arsip":
                template_error.success("Pemberitahuan","bon anda telah diarsipkan, silahkan melakukan bon lagi");
                break;
            case "selesai":
                template_error.success("Pemberitahuan","bon anda telah selesai, silahkan melakukan bon lagi");
                break;
        }

    }

    void doTerima(ModelBon obj){
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Konfirmasi");
        alert1.setContentText("Apakah bon sudah selesai");

        Optional<ButtonType> result1 = alert1.showAndWait();
        if (result1.get() == ButtonType.OK){
            updateStatus(obj, "selesai");
        }
    }

    void doKonfirmasi(ModelBon obj){
        if(ShareVariable.getUserRole() == "admin"){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setContentText("Apakah anda ingin menerima bon ini.");

            ButtonType buttonTypeOne = new ButtonType("tolak");
            ButtonType buttonTypeTwo = new ButtonType("terima");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne){

                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Tolak bon");
                alert1.setContentText("apakah anda yakin menolak bon ini ?");

                Optional<ButtonType> result1 = alert1.showAndWait();
                if (result1.get() == ButtonType.OK){
                    updateStatus(obj, "tolak");
                }
            } else if (result.get() == buttonTypeTwo) {
                updateStatus(obj, "terima");
            }
        }else{
            template_error.success("Pemberitahuan","bon anda masih menunggu konfirmasi dari admin");

        }
    }

    void updateStatus(ModelBon obj, String status){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();


        try {
            connection.setAutoCommit(false);

            String sql = "UPDATE bon SET bon_status = ? WHERE bon_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, status);
            statement.setString(2, obj.getBonid());

            int rs = statement.executeUpdate();
            if(rs > 0){
                initTable();
                createTable();
                if (status == "selesai"){
                    resetStatusNaip(obj);
                }
            }

            connection.commit();
            connection.setAutoCommit(true);

        }catch (Exception e){
            template_error.error(e);
        }
    }

    void resetStatusNaip(ModelBon obj){
        obj.getBonid();
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        try {
            String sql = "Select * from bon_detail WHEHRE bon_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, obj.getBonid());
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                String sqlreset = "UPDATE data_napi SET napi_booked = ? , napi_booked_by = ? WHERE napi_id = ?";
                PreparedStatement statement1 = connection.prepareStatement(sqlreset);
                statement1.setBoolean(1, false);
                statement1.setString(2, "");
                statement1.setString(3, rs.getString("napi_id"));

                int rs1 = statement1.executeUpdate();
                if (rs1 > 0){
                    System.out.printf("success update");
                }
            }

        }catch (Exception e){
            template_error.error(e);
        }

    }

}
