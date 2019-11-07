package application.connectifity;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class ConnectionClass {

//    private String uname = "ssFGUbaB52";
//    private String pass = "2TI9aSQNDe";
//    private String host = "jdbc:mysql://remotemysql.com:3306/ssFGUbaB52";
//
//    private String uname = "root";
//    private String pass = "leon1108";
//    private String host = "jdbc:mysql://192.168.1.2:3306/ebonpas";

    private String host;
    private String uname;
    private String pass ;
    private String db;
    public Connection connection;

    public Connection getConnection(){

        try {
            Scanner scanner = new Scanner(new File("config.txt"));
            while (scanner.hasNext()) {
                String[] rl = scanner.next().split("=");
                switch (rl[0]){
                    case "host":
                        host = rl[1];
                        break;

                    case "uname":
                        uname = rl[1];
                        break;

                    case "pass":
                        pass=rl[1];
                        break;

                    case "db":
                        db=rl[1];
                        break;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+db ,uname, pass);
        }catch (Exception e){
            e.printStackTrace();
        }

        return connection;

    }
}
