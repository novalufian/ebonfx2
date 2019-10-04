package application;

import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DataPegawaiController  implements Initializable {

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
    private Button btnTambah;

    @FXML
    void openFormTambah(ActionEvent event) {

    }

    void initTable(){
        initColumn();
    }

    void  initColumn(){
        nip.setCellValueFactory(new PropertyValueFactory<>("nip"));
        nama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        subagain.setCellValueFactory(new PropertyValueFactory<>("bagian"));
        sk.setCellValueFactory(new PropertyValueFactory<>("sk"));
        action.setCellValueFactory(new PropertyValueFactory<>("action"));
    }

    void iniTableData(){
        ObservableList<ModelDataPegawai> data = FXCollections.observableArrayList();

        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

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
                        rs.getString("nip_pegawai"),
                        rs.getString("nama_pegawai"),
                        rs.getString("nama"),
                        jk,
                        new Button("view")

                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        tablePegawai.setItems(data);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        iniTableData();
    }
}
