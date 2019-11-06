package application.controllers;

import application.Main;
import application.models.ModelDataPegawai;
import application.models.ShareVariable;
import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class DataPegawaiController  implements Initializable {

    private static ConnectionClass connectionClass = new ConnectionClass();
    private static Connection connection = connectionClass.getConnection();

    @FXML
    private TableView<ModelDataPegawai> tablePegawai;

    @FXML
    private TableColumn<ModelDataPegawai, String> nip;

    @FXML
    private TableColumn<ModelDataPegawai, String> nama;

    @FXML
    private TableColumn<ModelDataPegawai, String> subagain;

    @FXML
    private TableColumn<ModelDataPegawai, String> sk;

    @FXML
    private TableColumn<ModelDataPegawai, Button> action;

    @FXML
    private TableColumn<ModelDataPegawai, Button> update;

    @FXML
    private TableColumn<ModelDataPegawai, Button> view;

    @FXML
    private TableColumn<ModelDataPegawai, Button> delete;

    @FXML
    private Button btnTambah;

    @FXML
    private Button kembali;

    @FXML
    private TextField search;

    @FXML
    private AnchorPane mainAnchorpane;

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
    void openFormTambah(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/views/data_pegawai_tambah.fxml"));
            DataPegawaiTambahController dataPegawaiTambahController = new DataPegawaiTambahController(this);
            fxmlLoader.setController(dataPegawaiTambahController);
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent, 600, 500));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void initTable(){
        initColumn();
    }

    void  initColumn(){
        nip.setCellValueFactory(new PropertyValueFactory<>("nip"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        subagain.setCellValueFactory(new PropertyValueFactory<>("bagian"));
        sk.setCellValueFactory(new PropertyValueFactory<>("sk"));
        view.setCellValueFactory(new PropertyValueFactory<>("view"));
        update.setCellValueFactory(new PropertyValueFactory<>("update"));
        delete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        createaBtnView("view");
        createaBtnView("update");
        createaBtnView("delete");
    }

    void iniTableData(){
        ObservableList<ModelDataPegawai> data = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM data_pegawai " +
                    "INNER JOIN master_subagian ON data_pegawai.subag_pegawai = master_subagian.subagian_id " +
                    "WHERE data_pegawai.pegawai_published = 1";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()){
                String jk = "laki-laki";
                if (!rs.getBoolean("jenis_kelamin_pegawai")){
                    jk = "perempuan";
                }
                data.add(new ModelDataPegawai(
                        rs.getString("user_id"),
                        rs.getString("nip_pegawai"),
                        rs.getString("nama_pegawai"),
                        rs.getString("nama"),
                        jk,
                        rs.getBlob("ttd_pegawai"),
                        new Button("view"),
                        new Button("update"),
                        new Button("delete")

                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        tablePegawai.setItems(data);

        FilteredList<ModelDataPegawai> filteredList = new FilteredList<>(data, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(model_barang -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (model_barang.getNama().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(model_barang.getNip().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                    return true;
                }else if(model_barang.getBagian().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else{
                    return false;
                }
            });
        });

        SortedList<ModelDataPegawai> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tablePegawai.comparatorProperty());

        tablePegawai.setItems(sortedList);
    }

    void createaBtnView(String typebtn){
        Callback<TableColumn<ModelDataPegawai, Button>, TableCell<ModelDataPegawai, Button>> cellFactory =
                new Callback<TableColumn<ModelDataPegawai, Button>, TableCell<ModelDataPegawai, Button>>() {

                    @Override
                    public TableCell<ModelDataPegawai, Button> call(TableColumn<ModelDataPegawai, Button> param) {

                        final TableCell<ModelDataPegawai, Button> cell = new TableCell<ModelDataPegawai, Button>(){

                            final  Button btn = new Button(typebtn);

                            @Override
                            public void updateItem(Button item, boolean empty){
                                super.updateItem(item, empty);

                                if (empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    btn.setOnAction(event -> {
                                        ModelDataPegawai pegawai = getTableView().getItems().get(getIndex());
                                        System.out.println(pegawai);
                                        initBtnAction(typebtn, pegawai.getId(), pegawai);
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
            case "update" :
                update.setCellFactory(cellFactory);
                break;
            case "view" :
                view.setCellFactory(cellFactory);
                break;
            case "delete":
                delete.setCellFactory(cellFactory);
                break;
        }

    }

    void initBtnAction(String typebtn, String id, ModelDataPegawai obj){
        switch (typebtn) {
            case "update":
                acationUpdate(id, obj);
                break;
            case "view":
                actionView(id, obj);
                break;
            case "delete":
                actionDelete(id);
                break;
        }
    }

    void acationUpdate(String id, ModelDataPegawai obj){

        System.out.println("update data 3");


        ShareVariable.setPegawaiId(id);
        ShareVariable.setSharePegawai(obj);

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/application/views/data_pegawai_tambah.fxml"));
            DataPegawaiTambahController dataPegawaiTambahController = new DataPegawaiTambahController(this);
            fxmlLoader.setController(dataPegawaiTambahController);
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent, 600, 500));
            stage.show();

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    ShareVariable.setPegawaiId(null);
                    ShareVariable.setSharePegawai(null);
                    ShareVariable.setNapiId(null);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        int rs = 0;
        try {
//            String sql = "UPDATE data_pegawai SET nip_pegawai = ?, subag_pegawai = ? jenis_kelamin_pegawai = ? ";
        }catch (Exception e){

        }

    }

    void actionView(String id, ModelDataPegawai obj){

    }

    void actionDelete(String id){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText("Hapus data pegawai");
        alert.setContentText("Apakah anda yakin menghapus data ini ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try{
                String sql = "UPDATE data_pegawai SET pegawai_published = 0 WHERE user_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, id);

                int rs = statement.executeUpdate();
                if (rs > 0){
                    reset();
                    alert.close();
                }else{
                    alert.close();
                }
            }catch (Exception e){
                Alert myalert = new Alert(Alert.AlertType.ERROR);
                myalert.setTitle("connection error");
                myalert.setContentText("coba periksa koneksi anda");
                myalert.show();
            }
        }else{
            alert.close();
        }



    }

    public void reset(){
        initTable();
        iniTableData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       reset();

        mainAnchorpane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        mainAnchorpane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
    }
}
