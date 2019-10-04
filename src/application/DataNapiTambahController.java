package application;

import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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
    void simpanData(ActionEvent event) {
        String napiId =  "NPI-"+ System.currentTimeMillis() +new Random().nextInt(99999999);
        String napi_foto = "lorem";
        String noreg = noRegis.getText();
        String nama = namaLengkap.getText();
        String jk = jenisKelamin.getValue();
        String kamar = "kmr-090";
        Boolean _jk = false;
        if (jk == "laki-laki"){
            _jk = true;
        }

        System.out.println(noreg + " "+ nama + " "+ jk);

        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        try{
            String sql = "INSERT INTO data_napi (napi_id, napi_foto, napi_no_reg, napi_nama, napi_kamar, napi_sex) " +
                    "VALUES(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, napiId);
            preparedStatement.setString(2, napi_foto);
            preparedStatement.setString(3, noreg);
            preparedStatement.setString(4, nama);
            preparedStatement.setString(5, kamar);
            preparedStatement.setBoolean(6, _jk);

            int rs = preparedStatement.executeUpdate();
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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comoBoxJK();
        resetForm();
        //set default no register

    }
}
