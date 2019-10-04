package application;

import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DataNapiController implements Initializable {

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
    void openNapiForm(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/data_napi_tambah.fxml"));
        Parent parent = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent, 600, 500));
        stage.show();

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
    }

    private void createTable(){
        ObservableList<ModelDataNapi> data_napi = FXCollections.observableArrayList();

        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        try{
            String selectData = "SELECT * FROM data_napi " +
                    "LEFT JOIN master_kamar ON data_napi.napi_kamar = master_kamar.master_kamar_id " +
                    "LEFT JOIN master_blok ON master_kamar.master_blok_id = master_blok.blok_master_id " +
                    "LEFT JOIN master_subagian ON data_napi.napi_booked_by = master_subagian.subagian_id " +
                    "WHERE data_napi.napi_published = 1";
            PreparedStatement preparedStatement = connection.prepareStatement(selectData);
            ResultSet rs = preparedStatement.executeQuery();

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
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        createTable();

        String uid = ShareVariable.getUserid();

        System.out.println("load "+ uid);
    }
}
