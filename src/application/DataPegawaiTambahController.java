package application;

import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.ResourceBundle;

public class DataPegawaiTambahController implements Initializable {

    ObservableList globalListData = FXCollections.observableArrayList();

    ConnectionClass connectionClass = new ConnectionClass();
    Connection connection = connectionClass.getConnection();

    ObservableList listDataSubagian = FXCollections.observableArrayList();

    @FXML
    private TextField nip;

    @FXML
    private TextField nama;

    @FXML
    private ComboBox<String> subag;

    @FXML
    private ComboBox<String> jk;

    @FXML
    private Button btnTambah;

    @FXML
    void tambahData(ActionEvent event) {
        int rs = 0;
        int index =  subag.getSelectionModel().getSelectedIndex();
        ObservableList x = (ObservableList) globalListData.get(index);
        String y = (String) x.get(0);

        Boolean _jk = true;
        if (jk.getValue() != "laki-laki"){
            _jk = false;
        }

        try{
            if (ShareVariable.getPegawaiId() == null){
                String sql = "INSERT INTO data_pegawai " +
                        "(user_id, nip_pegawai, nama_pegawai, subag_pegawai, jenis_kelamin_pegawai, ttd_pegawai) " +
                        "VALUES (?,?,?,?,?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, "USR-"+ System.currentTimeMillis() +new Random().nextInt(99999999));
                statement.setString(2, nip.getText());
                statement.setString(3, nama.getText());
                statement.setString(4, y);
                statement.setBoolean(5, _jk);
                statement.setString(6, "lorem");
                rs = statement.executeUpdate();
            }else{
                String sql = "UPDATE data_pegawai SET " +
                        " nip_pegawai = ?, nama_pegawai = ?, subag_pegawai= ?, jenis_kelamin_pegawai = ? " +
                        "WHERE user_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, nip.getText());
                statement.setString(2, nama.getText());
                statement.setString(3, y);
                statement.setBoolean(4, _jk);
                statement.setString(5, ShareVariable.getPegawaiId());
                rs = statement.executeUpdate();
            }

            if (rs > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText("Selamat data pegawai sudah berhasil ditambahkan");
                alert.showAndWait();
                reset();

                ShareVariable.setSharePegawai(null);
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Peringatan");
                alert.setHeaderText("Data gagal ditambahkan");
                alert.showAndWait();
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("data gagal disimpan, tolong periska koneksi anda");
            alert.showAndWait();

            e.printStackTrace();
        }
    }

    void generateSubagList(){
        ObservableList dataList = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM master_subagian";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                ObservableList row = FXCollections.observableArrayList();

                dataList.add(rs.getString("nama"));
                row.add(rs.getString("subagian_id"));
                row.add(rs.getString("nama"));

                globalListData.add(row);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        subag.setItems(dataList);
    }


    void generateUpdatePegawai(){
        ModelDataPegawai data = ShareVariable.getSharePegawai();

        nip.setText(data.getNip());
        nama.setText(data.getNama());
        subag.getSelectionModel().select(data.getBagian());
        jk.getSelectionModel().select(data.getSk());
    }

    void reset(){
        nip.setText("");
        nama.setText("");
        btnTambah.setText("Tambah data pegawai");
        generateSubagList();

        ObservableList<String> jeniskl = FXCollections.observableArrayList("laki-laki", "perempuan");
        jk.setItems(jeniskl);

        try{
            BorderPane curentBorderpane = ShareVariable.getMainDashboardBoderpane();
            Parent stage_area = FXMLLoader.load(getClass().getResource("views/data_pegawai.fxml"));
            curentBorderpane.setCenter(stage_area);
        }catch(Exception e){

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reset();

        if(ShareVariable.getPegawaiId() != null){
            btnTambah.setText("Update data pegawai");
            generateUpdatePegawai();
        }else{
            btnTambah.setText("Tambah data pegawai");
        }

    }
}
