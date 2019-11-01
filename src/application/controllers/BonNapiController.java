package application.controllers;

import application.Main;
import application.models.ModelBlokLapasNapi;
import application.models.ModelDataNapi;
import application.models.ShareVariable;
import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class BonNapiController implements Initializable {

    private static ConnectionClass connectionClass = new ConnectionClass();
    private static Connection connection ;
    private static String subagianId ;

    @FXML
    private ImageView kopSurat;

    @FXML
    private Text textSubag;

    @FXML
    private Text TextNomor;

    @FXML
    private TableView<ModelBlokLapasNapi> tableBonNapi;

    @FXML
    private TableColumn<ModelBlokLapasNapi, String> bonNO;

    @FXML
    private TableColumn<ModelBlokLapasNapi, String> bonNama;

    @FXML
    private TableColumn<ModelBlokLapasNapi, String> bonBlok;

    @FXML
    private TableColumn<ModelBlokLapasNapi, String> bonKamar;

    @FXML
    private TableColumn<ModelBlokLapasNapi, Button> action;

    @FXML
    private Text tanggal;
    @FXML
    private ImageView imgTtd;
    @FXML
    private Text namaTerang;

    @FXML
    private Text nipFooter;

    @FXML
    private TextField keperluan;

    @FXML
    private DatePicker jamMasuk;

    @FXML
    private DatePicker jamKeluar;

    @FXML
    private Button btnBonNapi;

    @FXML
    private Button btnBatalkanBon;

    @FXML
    private Button kembali;

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
    void batalkanBon(ActionEvent event) {
        clearCartBon();
    }

    void clearCartBon(){
        ObservableList<ModelBlokLapasNapi> data = ShareVariable.getCartBookingNapi();
        data.forEach(item ->{
            try {
                String sql = "UPDATE data_napi SET napi_booked = ?, napi_booked_by = ? WHERE napi_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setBoolean(1, false);
                statement.setString(2, "");
                statement.setString(3, item.getId());

                int rs = statement.executeUpdate();
                if (rs > 0){

                }else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Peringatan");
                    alert.setHeaderText("bon gagal dibersihkan");
                    alert.showAndWait();
                }
            }catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("bon gagal dibatalkan, periksa koneksi anda");
                alert.showAndWait();
            }
        });
    }

    @FXML
    void bookNapi(ActionEvent event) {
        String bonid = "BON-"+ System.currentTimeMillis() +new Random().nextInt(99999999);
        try {

            connection.setAutoCommit(false);

            String sql = "INSERT INTO bon (bon_id, bon_user, bon_keterangan, bon_status, bon_jam_masuk, bon_jam_keluar, bon_subagian)" +
                    "VALUES (?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,bonid);
            statement.setString(2, ShareVariable.getUserid());
            statement.setString(3, keperluan.getText());
            statement.setString(4, "menunggu");
            statement.setDate(5, Date.valueOf(jamMasuk.getValue()));
            statement.setDate(6, Date.valueOf(jamKeluar.getValue()));
            statement.setString(7, subagianId);

            int rs = statement.executeUpdate();
            if (rs > 0){
                doSimpanDetailBon(bonid);
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Peringatan");
                alert.setHeaderText("Data gagal ditambahkan");
                alert.showAndWait();
            }

            connection.commit();
            connection.setAutoCommit(true);

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Data gagal disimpan, periksa koneksi anda");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    void doSimpanDetailBon(String bonid){
        ObservableList<ModelBlokLapasNapi> data = ShareVariable.getCartBookingNapi();
        AtomicInteger indexdata = new AtomicInteger();
            data.forEach(item -> {
                try {
                    String sql = "INSERT INTO bon_detail (bon_detail_id, bon_id, napi_id) " +
                            "VALUES (?,?,?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, "BDT-"+ System.currentTimeMillis() +new Random().nextInt(99999999));
                    statement.setString(2, bonid);
                    statement.setString(3, item.getId());

                    int rs = statement.executeUpdate();
                    if (rs > 0 ){
                        clearCartBon();
                        indexdata.getAndIncrement();
                        if (indexdata.equals(data.size())){
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("SUKSES");
                            alert.setHeaderText("Bon berhasil dikirim");
                            alert.showAndWait();
                        }

                    }else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Peringatan");
                        alert.setHeaderText("Bon agagal dikirim");
                        alert.showAndWait();
                    }
                }catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Bon gagal dikirim, periksa koneksi anda");
                    alert.showAndWait();
                    e.printStackTrace();
                }
            });

    }

    void generateTable(){
        bonNO.setCellValueFactory(new PropertyValueFactory<>("id"));
        bonNama.setCellValueFactory( new PropertyValueFactory<>("nama"));
        bonBlok.setCellValueFactory(new PropertyValueFactory<>("blok"));
        bonKamar.setCellValueFactory(new PropertyValueFactory<>("kamar"));
        action.setCellValueFactory(new PropertyValueFactory<>("book"));
        createaBtnView("delete");
    }

    void generateTableData(){
        ObservableList<ModelDataNapi> data = FXCollections.observableArrayList();
        ObservableList<ModelBlokLapasNapi> cartdata = ShareVariable.getCartBookingNapi();

        tableBonNapi.setItems(cartdata);

    }

    void reset(){
        fullfillForm();
        TextNomor.setText("BLK/90/VII/2019");
        generateTable();
        generateTableData();

        ObservableList<ModelBlokLapasNapi> data = ShareVariable.getCartBookingNapi();
        if (data.size() <= 0){
            btnBatalkanBon.setDisable(true);
            btnBonNapi.setDisable(true);
            keperluan.setDisable(true);
            jamKeluar.setDisable(true);
            jamMasuk.setDisable(true);
        }
    }

    void fullfillForm(){
        String userid = ShareVariable.getUserid();

        try {
            String sql = "SELECT * FROM data_pegawai " +
                    "LEFT JOIN master_subagian ON data_pegawai.subag_pegawai = master_subagian.subagian_id " +
                    "WHERE  data_pegawai.user_id = '"+userid+"'";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();


            while (rs.next()){
                namaTerang.setText(rs.getString("nama_pegawai"));
                nipFooter.setText(rs.getString("nip_pegawai"));
                textSubag.setText(rs.getString("nama"));
                subagianId =rs.getString("subag_pegawai");
                Blob blob = rs.getBlob("ttd_pegawai");
                InputStream inputStream = blob.getBinaryStream();
                Image image = new Image(inputStream);
                inputStream.close();
                System.out.println(inputStream);
                imgTtd.setImage(image);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createaBtnView(String typebtn){
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
                                        initBtnAction(getIndex(), napi);
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

    void initBtnAction(Integer index, ModelBlokLapasNapi obj){
        ObservableList<ModelBlokLapasNapi> data = ShareVariable.getCartBookingNapi();
        System.out.println(data);
        data.remove(0);
        ShareVariable.removeCartBookingNapi(index);

        try {
            String sql = "UPDATE data_napi SET napi_booked = ?, napi_booked_by = ? WHERE napi_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBoolean(1, false);
            statement.setString(2, "");
            statement.setString(3, obj.getId());

            int rs = statement.executeUpdate();
            if (rs > 0 ){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("SUKSES");
                alert.setHeaderText("Napi berhasil dihapus dari list bon");
                alert.showAndWait();

                reset();

                System.out.println(data);
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("PERINGATAN");
                alert.setHeaderText("Napi gagal dihapus dari list bon");
                alert.showAndWait();
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Napi gagal dihapus dari list bon, periksa koneksi anda");
            alert.showAndWait();
        }



    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = connectionClass.getConnection();
        reset();

        mainAnchorpane.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        mainAnchorpane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());

    }
}
