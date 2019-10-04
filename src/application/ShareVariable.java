package application;

public class ShareVariable {

    public static String getUserid() {
//        return userid;
        return "USR-8068201811180337385788";
    }

    public static void setUserid(String userid) {
        ShareVariable.userid = userid;
    }

    public static String userid = null;

}
