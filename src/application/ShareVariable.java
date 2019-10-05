package application;

public class ShareVariable {
    public static String userid = null;
    public static String napiId = null;

    public static String getNapiId() {return napiId;}
    public static void setNapiId(String napiId) {ShareVariable.napiId = napiId;}

    public static String getUserid() {
//        return userid;
        return "USR-8068201811180337385788";
    }

    public static void setUserid(String userid) {
        ShareVariable.userid = userid;
    }


}
