package application;

import application.connectifity.ConnectionClass;
import application.connectifity.Template_error;
import application.libs.Notification_libs;
import application.models.ModelBlokLapasNapi;
import application.models.ShareVariable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class Main extends Application {

    private static Stage currentStage;
    Notification_libs notification_libs = new Notification_libs();
    Template_error template_error = new Template_error();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));

        currentStage = primaryStage;

        currentStage.setTitle("Hello World");
        currentStage.setScene(new Scene(root, 600, 400));
        currentStage.show();
        currentStage.setMaximized(true);



//        ScheduledExecutorService scheduledExecutorService;
//        scheduledExecutorService = Executors.newScheduledThreadPool(1);
//
//        scheduledExecutorService.scheduleAtFixedRate(() -> {
//            // get a random integer between 0-10
//            Integer random = ThreadLocalRandom.current().nextInt(5);
//
//            // Update the chart
//            Platform.runLater(() -> {
//                // put random number with current time
//                if (ShareVariable.getUserRole() != null){
//                    notification_libs.create_notif("warning", "woooooooiii cok");
//                    System.out.println(ShareVariable.getUserRole());
//                }
//            });
//        }, 0, 5, TimeUnit.SECONDS);
//
        currentStage.setOnCloseRequest(event -> {
//            scheduledExecutorService.shutdown();
            roolbackDraftBookNapi();
            System.out.println("close yaa");
        });

    }

    void roolbackDraftBookNapi(){
        ObservableList<ModelBlokLapasNapi> data = ShareVariable.getCartBookingNapi();
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        try {
            connection.setAutoCommit(false);
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
                        template_error.warning("Peringatan", "bon gagal diremove");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    template_error.error(e);
                }
            });

            connection.commit();
            connection.setAutoCommit(true);

        }catch (Exception e){
            template_error.error(e);
        }
    }

    void call_notification(){
        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();

        try {
            String sql = "SELECT * FROM notif WHERE notif_user_role = ?";
        }catch (Exception e){

        }
    }

    public static Stage getStage(){
        return currentStage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
