package application;

import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class BlokLapasController implements Initializable {

    private Connection connection;
    private ObservableList listBlok;
    private ObservableList listKamar;
    private String GlobalKamarid = "";

    @FXML
    private TableView<ModelBlokLapasNapi> tableBlokLapas;

    @FXML
    private TableColumn<ModelBlokLapasNapi,String> napiid;

    @FXML
    private TableColumn<ModelBlokLapasNapi, String> nama;

    @FXML
    private TableColumn<ModelBlokLapasNapi, String> blok;

    @FXML
    private TableColumn<ModelBlokLapasNapi, String> kamar;

    @FXML
    private TableColumn<ModelBlokLapasNapi, String> status;

    @FXML
    private TableColumn<ModelBlokLapasNapi, Button> action;

    @FXML
    private ComboBox<String> cbPilihBlok;

    @FXML
    private ComboBox<String> cbPilihKamar;

    @FXML
    private Button resetTable;

    @FXML
    void resetTable(ActionEvent event) {
        GlobalKamarid = "";
        generateTable();
    }

    @FXML
    void showCbKamarValue(ActionEvent event) {
        int index = cbPilihKamar.getSelectionModel().getSelectedIndex();
        ObservableList x = (ObservableList) listKamar.get(index);
        String y = (String) x.get(0);

        GlobalKamarid = y;

        generateTable();
    }

    @FXML
    void showCbValue(ActionEvent event) {
        listKamar = null;

        int index =  cbPilihBlok.getSelectionModel().getSelectedIndex();
        ObservableList x = (ObservableList) listBlok.get(index);
        String y = (String) x.get(0);

        ObservableList data = FXCollections.observableArrayList();
        listKamar = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM master_kamar WHERE master_blok_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, y);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                ObservableList row = FXCollections.observableArrayList();

                row.add(rs.getString("master_kamar_id"));
                row.add(rs.getString("nama_kamar"));
                listKamar.add(row);

                data.add(rs.getString("nama_kamar"));
            }

        }catch(Exception e){

        }

        cbPilihKamar.setItems(data);
    }

    void createListCOmbo(){
        ObservableList data = FXCollections.observableArrayList();
        listBlok = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM master_blok";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                ObservableList row = FXCollections.observableArrayList();
                row.add(rs.getString("blok_master_id"));
                row.add(rs.getString("blok_nama"));
                listBlok.add(row);

                data.add(rs.getString("blok_nama"));
            }

        }catch(Exception e){

        }


        cbPilihBlok.setItems(data);
    }

    void generateTable(){
        initTable();
        iniTableData();
    }

    void initTable(){
        initColumn();
    }

    void initColumn(){
        napiid.setCellValueFactory(new PropertyValueFactory<>("id"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        blok.setCellValueFactory(new PropertyValueFactory<>("blok"));
        kamar.setCellValueFactory(new PropertyValueFactory<>("kamar"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        action.setCellValueFactory(new PropertyValueFactory<>("book"));
        createaBtnView("book napi");
    }

    void iniTableData(){
        String addSql = "";
        if (GlobalKamarid != ""){
            addSql = "AND data_napi.napi_kamar = '" + GlobalKamarid+"'";
        }

        ObservableList<ModelBlokLapasNapi> data = FXCollections.observableArrayList();

        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        try{
            String sql = "SELECT * FROM data_napi " +
                    "LEFT JOIN master_kamar ON data_napi.napi_kamar = master_kamar.master_kamar_id " +
                    "LEFT JOIN master_blok ON master_kamar.master_blok_id = master_blok.blok_master_id " +
                    "WHERE data_napi.napi_published = 1 AND data_napi.napi_booked = 0 "+
                    addSql;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs =  preparedStatement.executeQuery();

            while (rs.next()){
                String st = "unbooked";
                if (rs.getBoolean("napi_booked")){
                    st = "booked";
                }
                data.add(new ModelBlokLapasNapi(
                        rs.getString("napi_id"),
                        rs.getString("napi_nama"),
                        rs.getString("blok_nama"),
                        rs.getString("nama_kamar"),
                        st,
                        new Button("book")
                ));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        tableBlokLapas.setItems(data);
    }

    void createaBtnView(String typebtn){
        Callback<TableColumn<ModelBlokLapasNapi, Button>, TableCell<ModelBlokLapasNapi, Button>> cellFactory =
                new Callback<TableColumn<ModelBlokLapasNapi, Button>, TableCell<ModelBlokLapasNapi, Button>>() {

                    @Override
                    public TableCell<ModelBlokLapasNapi, Button> call(TableColumn<ModelBlokLapasNapi, Button> param) {

                        final TableCell<ModelBlokLapasNapi, Button> cell = new TableCell<ModelBlokLapasNapi, Button>(){

                            final  Button btn = new Button(typebtn);

                            @Override
                            public void updateItem(Button item, boolean empty){
                                super.updateItem(item, empty);

                                if (empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    btn.setOnAction(event -> {
                                        ModelBlokLapasNapi napi = getTableView().getItems().get(getIndex());
                                        bookNapi(napi.getId(), napi);
                                    });

                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        action.setCellFactory(cellFactory);
    }

    void bookNapi(String id, ModelBlokLapasNapi obj){
        try {
            String sql = "SELECT * FROM data_napi WHERE napi_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()){

                if(rs.getBoolean("napi_booked")){
                    if (rs.getString("napi_booked_by").equals(ShareVariable.getUserid())){
                        doBookNapi(id, false, obj);
                    }else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Peringatan");
                        alert.setContentText("maaf napi ini sudah di bon oleh user lainnya");
                        alert.showAndWait();
                    }

                }else{
                    doBookNapi(id, true, obj);
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Peringatan");
                alert.setContentText("Terjadi kesalahan koneksi");
                alert.showAndWait();
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("mohon periksa koneksi anda");
            alert.showAndWait();
        }
    }

    void doBookNapi(String id,Boolean type, ModelBlokLapasNapi obj){
        try {
            String napiid = ShareVariable.getUserid();
            if (!type){
                napiid = "";
            }
            String sql = "UPDATE data_napi SET napi_booked = ? , napi_booked_by = ? WHERE napi_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBoolean(1, type);
            statement.setString(2, napiid);
            statement.setString(3, id);
            int rs = statement.executeUpdate();

            if (rs > 0 ){
                generateTable();

                ObservableList data = FXCollections.observableArrayList();
                data.add(new ModelBlokLapasNapi(
                        obj.getId(),
                        obj.getNama(),
                        obj.getBlok(),
                        obj.getKamar(),
                        obj.getStatus(),
                        new Button("oke")
                ));
                ShareVariable.setCartBookingNapi(data);
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Peringatan");
                alert.setContentText("Terjadi kesalahan koneksi, coba lagi");
                alert.showAndWait();
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("mohon periksa koneksi anda");
            alert.showAndWait();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ConnectionClass connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();

        createListCOmbo();
        generateTable();
    }
}

