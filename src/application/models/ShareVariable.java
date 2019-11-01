package application.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;

public class ShareVariable {
    public static String sharehome = null;
    public static String username = null;
    public static String userRole = null;
    public static String userid = null;
    public static String napiId = null;
    public static String pegawaiId = null;
    public static BorderPane mainDashboardBoderpane = null;
    public static ObservableList<ModelBlokLapasNapi> cartBookingNapi = FXCollections.observableArrayList();

    public static ObservableList<ModelBlokLapasNapi> getCartBookingNapi() {
        return cartBookingNapi;
    }

    public static void setCartBookingNapi(ObservableList<ModelBlokLapasNapi> cartBookingNapi) {
        ShareVariable.cartBookingNapi.addAll(cartBookingNapi);
    }

    public static void removeCartBookingNapi(Integer index) {
        ShareVariable.cartBookingNapi.remove(index);
    }

    public static void removeAllCartBookingNapi(ObservableList<ModelBlokLapasNapi> cartBookingNapi) {
        ShareVariable.cartBookingNapi.removeAll(cartBookingNapi);
    }

    public static String getLoginid() {
        return loginid;
    }

    public static void setLoginid(String loginid) {
        ShareVariable.loginid = loginid;
    }

    public static String loginid = null;

    public static ModelDataPegawai getSharePegawai() {
        return sharePegawai;
    }

    public static void setSharePegawai(ModelDataPegawai sharePegawai) {
        ShareVariable.sharePegawai = sharePegawai;
    }

    public static ModelDataPegawai sharePegawai = null;

    public static String getPegawaiId() {return pegawaiId;}
    public static void setPegawaiId(String pegawaiId) {ShareVariable.pegawaiId = pegawaiId;}
    public static BorderPane getMainDashboardBoderpane() {return mainDashboardBoderpane;}
    public static void setMainDashboardBoderpane(BorderPane mainDashboardBoderpane) {ShareVariable.mainDashboardBoderpane = mainDashboardBoderpane;}
    public static String getNapiId() {return napiId;}
    public static void setNapiId(String napiId) {ShareVariable.napiId = napiId;}
    public static String getUserid() {
        return userid;
//        return "USR-8068201811180337385788";
    }
    public static void setUserid(String userid) {
        ShareVariable.userid = userid;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ShareVariable.username = username;
    }

    public static String getSharehome() {
        return sharehome;
    }

    public static void setSharehome(String sharehome) {
        ShareVariable.sharehome = sharehome;
    }

    public static String getUserRole() {
        return userRole;
    }

    public static void setUserRole(String userRole) {
        ShareVariable.userRole = userRole;
    }
}
