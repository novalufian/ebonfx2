package application;

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

public class DataNapiController implements Initializable {

    private static ObservableList blokData = FXCollections.observableArrayList();
    private static ConnectionClass connectionClass;
    private static Connection connection;

    @FXML
    private TableView<ModelDataNapi> table_data_pegawai;

    @FXML
    private TableColumn<ModelDataNapi, String> col_id;

    @FXML
    private TableColumn<ModelDataNapi, String> col_nama;

    @FXML
    private TableColumn<ModelDataNapi, String> col_no_reg;

    @FXML
    private TableColumn<ModelDataNapi, String> col_blok;

    @FXML
    private TableColumn<ModelDataNapi, String> col_kamar;

    @FXML
    private TableColumn<ModelDataNapi, Button> col_action;

    @FXML
    private TableColumn<ModelDataNapi, Button> colUpdate;

    @FXML
    private TableColumn<ModelDataNapi, Button> colView;

    @FXML
    private TableColumn<ModelDataNapi, Button> colDelete;

    @FXML
    private Button btn_add_data_napi;

    @FXML
    private Button kembali;

    @FXML
    private TextField search;

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
    void openNapiForm(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/data_napi_tambah.fxml"));
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

    }

    private void initTable(){
        initCol();
    }

    private void initCol(){
        col_id.setCellValueFactory(new PropertyValueFactory<>("napiid"));
        col_nama.setCellValueFactory(new PropertyValueFactory<>("napifoto"));
        col_no_reg.setCellValueFactory(new PropertyValueFactory<>("napinoreg"));
        col_kamar.setCellValueFactory(new PropertyValueFactory<>("napinama"));
        col_blok.setCellValueFactory(new PropertyValueFactory<>("napikamar"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("update"));
        colView.setCellValueFactory(new PropertyValueFactory<>("view"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        createaBtnView("update");
        createaBtnView("view");
        createaBtnView("delete");
    }

    private void createTable(){
        ObservableList<ModelDataNapi> data_napi = FXCollections.observableArrayList();



        try{
            String selectData = "SELECT * FROM data_napi " +
                    "LEFT JOIN master_kamar ON data_napi.napi_kamar = master_kamar.master_kamar_id " +
                    "LEFT JOIN master_blok ON master_kamar.master_blok_id = master_blok.blok_master_id " +
                    "LEFT JOIN master_subagian ON data_napi.napi_booked_by = master_subagian.subagian_id " +
                    "WHERE data_napi.napi_published = 1";
            PreparedStatement preparedStatement = connection.prepareStatement(selectData);
            ResultSet rs = preparedStatement.executeQuery();

            int x = 1;
            while (rs.next()){
                data_napi.add(new ModelDataNapi(
                        rs.getString("napi_id"),
                        rs.getString("napi_nama"),
                        rs.getString("napi_no_reg"),
                        rs.getString("nama_kamar"),
                        rs.getString("blok_nama"),
                        new Button("update"),
                        new Button("view"),
                        new Button("delete")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        table_data_pegawai.setItems(data_napi);

        FilteredList<ModelDataNapi> filteredList = new FilteredList<>(data_napi, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(model_barang -> {
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (model_barang.getNapiid().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else if(model_barang.getNapinama().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                    return true;
                }else if(model_barang.getNapikamar().toLowerCase().indexOf(lowerCaseFilter) != -1){
                    return true;
                }else{
                    return false;
                }
            });
        });

        SortedList<ModelDataNapi> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table_data_pegawai.comparatorProperty());

        table_data_pegawai.setItems(sortedList);
    }

    public void generateTable(){
        initTable();
        createTable();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();

        generateTable();

        String uid = ShareVariable.getUserid();

    }

    void createaBtnView(String typebtn){
        Callback<TableColumn<ModelDataNapi, Button>, TableCell<ModelDataNapi, Button>> cellFactory =
                new Callback<TableColumn<ModelDataNapi, Button>, TableCell<ModelDataNapi, Button>>() {

            @Override
                    public TableCell<ModelDataNapi, Button> call(TableColumn<ModelDataNapi, Button> param) {

                        final TableCell<ModelDataNapi, Button> cell = new TableCell<ModelDataNapi, Button>(){

                            final  Button btn = new Button(typebtn);

                            @Override
                            public void updateItem(Button item, boolean empty){
                                super.updateItem(item, empty);

                                if (empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    btn.setOnAction(event -> {
                                        ModelDataNapi napi = getTableView().getItems().get(getIndex());
                                        initBtnAction(typebtn, napi.getNapiid());
                                        if (typebtn == "update"){}
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
                colUpdate.setCellFactory(cellFactory);
                break;
            case "view" :
                colView.setCellFactory(cellFactory);
                break;
            case "delete":
                colDelete.setCellFactory(cellFactory);
                break;
        }

    }

    void initBtnAction(String typebtn, String id){
        switch (typebtn) {
            case "update":
                acationUpdate(id);
                break;
            case "view":
                actionView(id);
                break;
            case "delete":
                actionDelete(id);
                break;
        }
    }

    void acationUpdate(String id){
        ShareVariable.setNapiId(id);
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/data_napi_tambah.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent, 600, 500));
            stage.show();
        }catch (Exception e){
            Alert myalert = new Alert(Alert.AlertType.ERROR);
            myalert.setTitle("connection error");
            myalert.setContentText("coba periksa koneksi anda");
        }

    }

    void  actionView(String id){

    }

    void actionDelete(String id){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText("Hapus data napi");
        alert.setContentText("Apakah anda yakin menghapus data ini ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            try{
                String sql = "UPDATE data_napi SET napi_published = 0 WHERE napi_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, id);
                int rs = statement.executeUpdate();

                if (rs > 0){
                    generateTable();
                    alert.close();
                }else{
                    alert.close();
                }

            }catch (Exception e){
                Alert myalert = new Alert(Alert.AlertType.ERROR);
                myalert.setTitle("connection error");
                myalert.setContentText("coba periksa koneksi anda");
            }

        } else {
            alert.close();
        }
    }

    void createListBlok(){
        ObservableList bloklist = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM master_blok";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                ObservableList row = FXCollections.observableArrayList();
                row.add(rs.getString("master_blok_id"));
                row.add(rs.getString("blok_nama"));
                blokData.add(row);

                bloklist.add(rs.getString("blok_nama"));
            }
        }catch (Exception e){

        }



    }
}
