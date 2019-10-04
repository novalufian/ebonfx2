package application;

import application.connectifity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DataBlokKamarController implements Initializable {

    private static ObservableList arrayDataListBlok = FXCollections.observableArrayList();
    private static Connection connection;
    private static ObservableList dataArrayBlok;

    @FXML
    private TableView<ModelBlok> tableBlok;

    @FXML
    private TableColumn<ModelBlok, Integer> no;

    @FXML
    private TableColumn<ModelBlok, String> blok;

    @FXML
    private TableView<ModelKamar> tableKamar;

    @FXML
    private TableColumn<ModelKamar, Integer> kamarno;

    @FXML
    private TableColumn<ModelKamar, String> kamarblok;

    @FXML
    private TableColumn<ModelKamar, String> kamar;

    @FXML
    private TextField tambakBlokTxt;

    @FXML
    private Button btnTambahBlok;

    @FXML
    private ComboBox<?> listBlok;

    @FXML
    private TextField tambahkamarTxt;

    @FXML
    private Button btnTambahKamar;

    @FXML
    void ambahKamar(ActionEvent event) {

    }

    @FXML
    void tambahBlokBaru(ActionEvent event) {

    }

    void generateTableKamar(){
        initTableKamar();
        initTableKamarData();
    }

    void initTableKamar(){
           kamarno.setCellValueFactory(new PropertyValueFactory<>("no"));
           kamarblok.setCellValueFactory(new PropertyValueFactory<>("blok"));
           kamar.setCellValueFactory(new PropertyValueFactory<>("kamar"));
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
                        rs.getString("blok_nama"),
                        rs.getString("nama_kamar")
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
                        rs.getString("blok_nama")
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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ConnectionClass connectionClass = new ConnectionClass();
        connection = connectionClass.getConnection();

        generateTableKamar();
        generateTableBlok();
    }
}
