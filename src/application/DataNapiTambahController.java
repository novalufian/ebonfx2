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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;

public class DataNapiTambahController implements Initializable {

    ObservableList blokData = FXCollections.observableArrayList();
    ObservableList listKamar = FXCollections.observableArrayList();

    ConnectionClass connectionClass ;
    Connection connection ;

    @FXML
    private TextField noRegis;

    @FXML
    private TextField namaLengkap;

    @FXML
    private ComboBox<String> jenisKelamin;

    @FXML
    private ComboBox<String> blokCombobox;

    @FXML
    private ComboBox<String> kamar;

    @FXML
    private Button btnSimpan;

    @FXML
    private Button btnBatal;

    @FXML
    void batalkan(ActionEvent event) {

    }

    @FXML
    void actionBlok(ActionEvent event) {
        listKamar = null;
        int index =  blokCombobox.getSelectionModel().getSelectedIndex();
        ObservableList x = (ObservableList) blokData.get(index);
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

        kamar.setItems(data);
    }

    @FXML
    void actionKamar(ActionEvent event) {
        int index = kamar.getSelectionModel().getSelectedIndex();
        ObservableList x = (ObservableList) listKamar.get(index);
        String y = (String) x.get(0);

        System.out.println(y);
    }

    @FXML
    void simpanData(ActionEvent event) {
        ObservableList x = (ObservableList) listKamar.get(kamar.getSelectionModel().getSelectedIndex());;

        String napiId =  "NPI-"+ System.currentTimeMillis() +new Random().nextInt(99999999);
        String napi_foto = "lorem";
        String noreg = noRegis.getText();
        String nama = namaLengkap.getText();
        String jk = jenisKelamin.getValue();
        String kmr = (String) x.get(0);
        Boolean _jk = false;
        if (jk == "laki-laki"){
            _jk = true;
        }

        try{
            String isNapi = ShareVariable.getNapiId();
            System.out.println(kamar.getItems());
            int rs = 0;
            if (isNapi != null){
                String sql = "UPDATE data_napi SET napi_no_reg = ?," +
                        "napi_nama = ?," +
                        "napi_kamar = ?," +
                        "napi_sex = ? " +
                        "WHERE napi_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, noreg);
                statement.setString(2, nama);
                statement.setString(3, kmr);
                statement.setBoolean(4,_jk);
                statement.setString(5, isNapi);
                rs = statement.executeUpdate();


            }else{

                String sql = "INSERT INTO data_napi (napi_id, napi_foto, napi_no_reg, napi_nama, napi_kamar, napi_sex) " +
                        "VALUES(?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, napiId);
                preparedStatement.setString(2, napi_foto);
                preparedStatement.setString(3, noreg);
                preparedStatement.setString(4, nama);
                preparedStatement.setString(5, kmr);
                preparedStatement.setBoolean(6, _jk);

                rs = preparedStatement.executeUpdate();

            }

            if (rs > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText("Selamat napi sudah berhasil ditambahkan");
                alert.showAndWait();
                resetForm();
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Peringatan");
                alert.setHeaderText("Data napi gagal diatambahkan");
                alert.showAndWait();
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText("Tolong periksa koneksi anda / hubungi pihak server");
            alert.showAndWait();
            e.printStackTrace();
        }




    }

    void actionUpdate(String id ){

    }

    void actionSimpan(){

    }


    private void comoBoxJK(){
        ObservableList<String> JK = FXCollections.observableArrayList("laki-laki", "perempuan");
        jenisKelamin.setItems(JK);
    }

    private void resetForm(){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String newNoreg = "LPS/"+formatter.format(date)+"/VII/"+ new Random().nextInt(999999);
        noRegis.setText(newNoreg);
        noRegis.disableProperty();

        namaLengkap.setText("");

        try{
            BorderPane curentBorderpane = ShareVariable.getMainDashboardBoderpane();
            Parent stage_area = FXMLLoader.load(getClass().getResource("views/data_napi.fxml"));
            curentBorderpane.setCenter(stage_area);
        }catch(Exception e){

        }



    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();

        comoBoxJK();
        resetForm();
        //set default no register
        createBlokList();

        String isNapi = ShareVariable.getNapiId();
        System.out.println(isNapi);
        if (isNapi != null){
            setEditForm(isNapi);
        }

    }

    void createBlokList(){
        ObservableList data = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM master_blok";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                ObservableList row = FXCollections.observableArrayList();
                row.add(rs.getString("blok_master_id"));
                row.add(rs.getString("blok_nama"));
                blokData.add(row);

                data.add(rs.getString("blok_nama"));

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        blokCombobox.setItems(data);
    }

    void setEditForm(String id){
        try{
            String sql = "SELECT * FROM data_napi " +
                    "LEFT JOIN master_kamar ON data_napi.napi_kamar = master_kamar.master_kamar_id " +
                    "LEFT JOIN master_blok ON master_kamar.master_blok_id = master_blok.blok_master_id " +
                    "LEFT JOIN master_subagian ON data_napi.napi_booked_by = master_subagian.subagian_id " +
                    "WHERE data_napi.napi_published = 1 AND data_napi.napi_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                String sex = "perempuan";
                if(rs.getBoolean("napi_sex")){
                    sex = "laki-laki";
                }
                noRegis.setText(rs.getString("napi_no_reg"));
                namaLengkap.setText(rs.getString("napi_nama"));
                jenisKelamin.getSelectionModel().select(sex);
                blokCombobox.getSelectionModel().select(rs.getString("blok_nama"));
                kamar.getSelectionModel().select(rs.getString("nama_kamar"));

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
