package application;

public class ModelKamar {
    String blok, kamar;
    Integer no;

    public ModelKamar( Integer no,String blok, String kamar) {
        this.blok = blok;
        this.kamar = kamar;
        this.no = no;
    }

    public String getBlok() {
        return blok;
    }

    public void setBlok(String blok) {
        this.blok = blok;
    }

    public String getKamar() {
        return kamar;
    }

    public void setKamar(String kamar) {
        this.kamar = kamar;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
