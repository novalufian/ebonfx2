package application.libs;

import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Notification_libs {

    public void create_notif(String title , String text){
        Notifications.create()
                .title(title)
                .darkStyle()
                .text(text)
                .hideAfter(Duration.seconds(5))
                .showInformation();
    }
}
