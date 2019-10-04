package application;

import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class BonRiwayatController implements Initializable {

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
    private TextField search;

    @FXML
    private Button btnSearch;

    @FXML
    void doSearch(KeyEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        createTable();
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

            if (rs.next()){
                dataRiwayatBon.add(new ModelBon(
                        rs.getString("bon_id"),
                        rs.getString("bon_keterangan"),
                        rs.getString("bon_status"),
                        rs.getString("nama"),
                        rs.getString("bon_jam_masuk"),
                        rs.getString("bon_jam_keluar"),
                        rs.getString("bon_created_at"),
                        new Button("view")
                ));

            }
        }catch(Exception e){
            e.printStackTrace();

        }

        tableBonRiwayat.setItems(dataRiwayatBon);

    }
}
