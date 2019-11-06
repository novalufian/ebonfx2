package application.controllers;

import application.connectifity.Template_error;
import application.models.ModelDataPegawai;
import application.models.ShareVariable;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.ResourceBundle;

public class DataPegawaiTambahController implements Initializable {


    ConnectionClass connectionClass = new ConnectionClass();
    Connection connection = connectionClass.getConnection();
    Template_error template_error = new Template_error();
    ObservableList globalListData = FXCollections.observableArrayList();
    ObservableList listDataSubagian = FXCollections.observableArrayList();

    FileChooser fileChooser = new FileChooser();
    File chosefile = null;

    DataPegawaiController dataPegawaiController;

    public DataPegawaiTambahController(DataPegawaiController dp) {
        dataPegawaiController = dp;
    }

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
    private ImageView ttdImage;

    @FXML
    private Button chooseFile;

    @FXML
    void dochooseFile(ActionEvent event) {
        Stage stage = (Stage) btnTambah.getScene().getWindow();
        chosefile = fileChooser.showOpenDialog(stage);

        if(chosefile != null){
            System.out.println(chosefile.toURI().toString());
        }
        Image image = new Image(chosefile.toURI().toString());
        ttdImage.setImage(image);

    }
    @FXML
    void tambahData(ActionEvent event) {

        if (nip.getText().trim().isEmpty() || nama.getText().trim().isEmpty() || jk.getSelectionModel().getSelectedIndex() < 0 || subag.getSelectionModel().getSelectedIndex() < 0 ){
            template_error.warning("Perinagtan", "silahakan isi form terlebih dahulu");
        }else{

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

                    FileInputStream fis = new FileInputStream(chosefile);
                    statement.setBinaryStream(6, (FileInputStream) fis, (int) chosefile.length());
                    rs = statement.executeUpdate();
                }else{
                    String sql = "UPDATE data_pegawai SET " +
                            " nip_pegawai = ?, nama_pegawai = ?, subag_pegawai= ?, jenis_kelamin_pegawai = ?, ttd_pegawai = ? " +
                            "WHERE user_id = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, nip.getText());
                    statement.setString(2, nama.getText());
                    statement.setString(3, y);
                    statement.setBoolean(4, _jk);
                    statement.setString(6, ShareVariable.getPegawaiId());

                    if (chosefile != null){
                        FileInputStream fis = new FileInputStream(chosefile);
                        statement.setBinaryStream(5, (FileInputStream) fis, (int) chosefile.length());
                    }else{
                        statement.setBinaryStream(5, ShareVariable.getSharePegawai().getTtd().getBinaryStream());
                    }

                    rs = statement.executeUpdate();
                }

                if (rs > 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Berhasil");
                    alert.setHeaderText("Selamat data pegawai sudah berhasil ditambahkan");
                    alert.showAndWait();
                    reset();
                    dataPegawaiController.reset();
                    ShareVariable.setSharePegawai(null);

                    Stage stage = (Stage) btnTambah.getScene().getWindow();
                    stage.close();

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
        System.out.printf(data.getNama());

        nip.setText(data.getNip());
        nama.setText(data.getNama());
        subag.getSelectionModel().select(data.getBagian());
        jk.getSelectionModel().select(data.getSk());

        try {
            InputStream is = data.getTtd().getBinaryStream();
            Image img = new Image(is);
            ttdImage.setImage(img);
        }catch (Exception e){}

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
