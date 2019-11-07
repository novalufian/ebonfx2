package application.controllers;

import application.connectifity.Template_error;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ConfigController implements Initializable {

    Template_error error_template = new Template_error();

    @FXML
    private TextField host;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private TextField database;

    @FXML
    private Button simpan;

    @FXML
    void doSimpan(ActionEvent event) {
        try {
            PrintWriter writer = new PrintWriter("config.txt", "UTF-8");
            writer.println("host="+host.getText().trim());
            writer.println("uname="+username.getText().trim());
            writer.println("pass="+password.getText().trim());
            writer.println("db="+database.getText().trim());
            writer.flush();
            writer.close();

            System.out.println();
            if (!writer.checkError()){
                error_template.success("Berhasil", "config connection sudah diperbarui");
                Stage stage = (Stage) simpan.getScene().getWindow();
                stage.close();
            }else{
                error_template.warning("Peringatan", "data gagal diperbarui");
            }

        }catch (Exception e){
            error_template.error(e);
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Scanner scanner = new Scanner(new File("config.txt"));
            while (scanner.hasNext()) {
                String[] rl = scanner.next().split("=");
                System.out.println(rl[0]);
                switch (rl[0]){
                    case "host":
                        host.setText(rl[1]);
                        break;

                    case "uname":
                        username.setText(rl[1]);
                        break;

                    case "pass":
                        password.setText(rl[1]);
                        break;

                    case "db":
                        database.setText(rl[1]);
                        break;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
