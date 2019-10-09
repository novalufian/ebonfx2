package application.connectifity;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {

    private String uname = "ssFGUbaB52";
    private String pass = "BX8sPhSjTS";
    private String host = "jdbc:mysql://remotemysql.com:3306/ssFGUbaB52";
    public Connection connection;

    public Connection getConnection(){

        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(host ,uname, pass);
        }catch (Exception e){
            e.printStackTrace();
        }

        return connection;

    }
}
