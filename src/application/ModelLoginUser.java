package application;

import javafx.scene.control.Button;

public class ModelLoginUser {

    public ModelLoginUser(int no, String id, String nip, String nama, String username, String password, String roleuser, Button view, Button update, Button delete) {
        this.no = no;
        this.id = id;
        this.nip = nip;
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.roleuser = roleuser;
        this.view = view;
        this.update = update;
        this.delete = delete;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleuser() {
        return roleuser;
    }

    public void setRoleuser(String roleuser) {
        this.roleuser = roleuser;
    }

    public Button getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }

    public Button getUpdate() {
        return update;
    }

    public void setUpdate(Button update) {
        this.update = update;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    int no;
    String id,nip, nama, username, password, roleuser;
    Button view, update, delete;


}
