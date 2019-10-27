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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.ResourceBundle;

public class DataBlokKamarController implements Initializable {

    private static ObservableList arrayDataListBlok = FXCollections.observableArrayList();
    private static ObservableList dataArrayBlok;
    private static String globalBlokId = null;
    private static String globalKamarid = null;

    private static ConnectionClass connectionClass = new ConnectionClass();
    private static Connection connection = connectionClass.getConnection();

    @FXML
    private TableView<ModelBlok> tableBlok;

    @FXML
    private TableColumn<ModelBlok, Integer> no;

    @FXML
    private TableColumn<ModelBlok, String> blok;

    @FXML
    private TableColumn<ModelBlok, Button> actionBlok;


    @FXML
    private TableView<ModelKamar> tableKamar;

    @FXML
    private TableColumn<ModelKamar, Integer> kamarno;

    @FXML
    private TableColumn<ModelKamar, String> kamarblok;

    @FXML
    private TableColumn<ModelKamar, String> kamar;

    @FXML
    private TableColumn<ModelKamar, Button> action;

    @FXML
    private TextField tambakBlokTxt;

    @FXML
    private Button btnTambahBlok;

    @FXML
    private ComboBox<String> listBlok;

    @FXML
    private TextField tambahkamarTxt;

    @FXML
    private Button btnTambahKamar;

    @FXML
    private Button kembali;

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
    void ambahKamar(ActionEvent event) {
        int index = listBlok.getSelectionModel().getSelectedIndex();
        ObservableList x = (ObservableList) arrayDataListBlok.get(index);
        String y = (String) x.get(0);
        int rs = 0;

        try{
            if (btnTambahKamar.getText() == "Update kamar"){
                String sql = "UPDATE master_kamar SET master_blok_id = ? , nama_kamar = ? WHERE master_kamar_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,y);
                statement.setString(2, tambahkamarTxt.getText());
                statement.setString(3, globalKamarid);
                rs = statement.executeUpdate();

            }else{
                String sql = "INSERT INTO master_kamar (master_kamar_id, master_blok_id, nama_kamar) VALUES (?,?,?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,"KMR-"+ System.currentTimeMillis() +new Random().nextInt(99999999));
                statement.setString(2, y);
                statement.setString(3, tambahkamarTxt.getText());
                rs = statement.executeUpdate();
            }

            if (rs > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("sukses");
                alert.setContentText("selamat data kamar telah ditambahkan");
                alert.show();
                reset();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("peringatan");
                alert.setContentText("data gagal ditambahkan");
                alert.show();
            }

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setContentText("data gagal ditambahkan, periksa koneksi anda");
            alert.show();
        }

    }

    @FXML
    void tambahBlokBaru(ActionEvent event) {
        int rs = 0;
        try{
            if (btnTambahBlok.getText() == "Update blok"){
                String sql = "UPDATE master_blok SET blok_nama = ? WHERE blok_master_id = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, tambakBlokTxt.getText());
                statement.setString(2, globalBlokId);

                rs = statement.executeUpdate();

            }else{
                String sql = "INSERT INTO master_blok (blok_master_id, blok_nama) VALUES (?,?) ";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, "BLK-"+ System.currentTimeMillis() +new Random().nextInt(99999999));
                statement.setString(2, tambakBlokTxt.getText());

                rs =statement.executeUpdate();
            }


            if (rs > 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("sukses");
                alert.setContentText("selamat data blok telah ditambahkan");
                alert.show();
                reset();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("peringatan");
                alert.setContentText("data gagal ditambahkan");
                alert.show();
            }

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("error");
            alert.setContentText("data gagal ditambahkan, periksa koneksi anda");
            alert.show();
        }

    }

    void generateTableKamar(){
        initTableKamar();
        initTableKamarData();
    }

    void initTableKamar(){
           kamarno.setCellValueFactory(new PropertyValueFactory<>("no"));
           kamarblok.setCellValueFactory(new PropertyValueFactory<>("blok"));
           kamar.setCellValueFactory(new PropertyValueFactory<>("kamar"));
           action.setCellValueFactory(new PropertyValueFactory<>("action"));
           createaBtnViewKamar();
    }

    void initTableKamarData(){
        ObservableList<ModelKamar> data = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM master_kamar " +
                    "INNER JOIN master_blok ON master_kamar.master_blok_id = master_blok.blok_master_id " +
                    "WHERE master_kamar.kamar_published = 1 " +
                    "ORDER BY kamar_created_at DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            int x = 1;
            while (rs.next()){
                data.add(new ModelKamar(
                        x++,
                        rs.getString("master_kamar_id"),
                        rs.getString("blok_nama"),
                        rs.getString("nama_kamar"),
                        new Button("update")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        tableKamar.setItems(data);
    }

    void generateTableBlok(){
        initTbaleBlok();
        initTableBlokData();
    }

    void initTbaleBlok(){
        no.setCellValueFactory(new PropertyValueFactory<>("no"));
        blok.setCellValueFactory(new PropertyValueFactory<>("blok"));
        actionBlok.setCellValueFactory(new PropertyValueFactory<>("action"));
        createaBtnViewBlok();
    }

    void initTableBlokData(){

        ObservableList<ModelBlok> data = FXCollections.observableArrayList();
        ObservableList listdata = FXCollections.observableArrayList();

        try{
            String sql = "SELECT * FROM master_blok";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();

            int x = 1;
            while (rs.next()){

                ObservableList row = FXCollections.observableArrayList();

                data.add(new ModelBlok(
                        x++,
                        rs.getString("blok_master_id"),
                        rs.getString("blok_nama"),
                        new Button("update")
                ));

                row.add(rs.getString("blok_master_id"));
                row.add(rs.getString("blok_nama"));

                arrayDataListBlok.add(row);

                listdata.add(rs.getString("blok_nama"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        tableBlok.setItems(data);
        listBlok.setItems(listdata);

        System.out.println(arrayDataListBlok);
    }

    void reset(){
        generateTableKamar();
        generateTableBlok();
        globalBlokId = null;
        globalKamarid = null;

        tambakBlokTxt.setText("");
        tambahkamarTxt.setText("");

        btnTambahKamar.setText("Tambah kamar");
        btnTambahBlok.setText("Tambak blok");
    }

    private void createaBtnViewBlok(){
        Callback<TableColumn<ModelBlok, Button>, TableCell<ModelBlok, Button>> cellFactory =
                new Callback<TableColumn<ModelBlok, Button>, TableCell<ModelBlok, Button>>() {

                    @Override
                    public TableCell<ModelBlok, Button> call(TableColumn<ModelBlok, Button> param) {

                        final TableCell<ModelBlok, Button> cell = new TableCell<ModelBlok, Button>(){

                            final  Button btn = new Button("update");

                            @Override
                            public void updateItem(Button item, boolean empty){
                                super.updateItem(item, empty);

                                if (empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    btn.setOnAction(event -> {
                                        ModelBlok blok = getTableView().getItems().get(getIndex());
                                        System.out.println(blok.getId());
                                        initBtnActionBlok(blok, blok.getId());
                                    });

                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        switch ("update"){
            case "update" :
                actionBlok.setCellFactory(cellFactory);
                break;
        }

    }

    void initBtnActionBlok(ModelBlok object , String id){
        tambakBlokTxt.setText(object.getBlok());
        globalBlokId = id;

        btnTambahBlok.setText("Update blok");

    }

    private void createaBtnViewKamar(){
        Callback<TableColumn<ModelKamar, Button>, TableCell<ModelKamar, Button>> cellFactory =
                new Callback<TableColumn<ModelKamar, Button>, TableCell<ModelKamar, Button>>() {

                    @Override
                    public TableCell<ModelKamar, Button> call(TableColumn<ModelKamar, Button> param) {

                        final TableCell<ModelKamar, Button> cell = new TableCell<ModelKamar, Button>(){

                            final  Button btn = new Button("update");

                            @Override
                            public void updateItem(Button item, boolean empty){
                                super.updateItem(item, empty);

                                if (empty){
                                    setGraphic(null);
                                    setText(null);
                                }else{
                                    btn.setOnAction(event -> {
                                        ModelKamar kamar = getTableView().getItems().get(getIndex());
                                        System.out.println(kamar.getId());
                                        initBtnActionKamar(kamar, kamar.getId());
                                    });

                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        switch ("update"){
            case "update" :
                action.setCellFactory(cellFactory);
                break;
        }

    }

    void initBtnActionKamar(ModelKamar object , String id){
        System.out.println(object);
        globalKamarid = id;
        String x = object.getBlok();
        listBlok.getSelectionModel().select(x);
        tambahkamarTxt.setText(object.getKamar());

        btnTambahKamar.setText("Update kamar");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        reset();
    }
}
