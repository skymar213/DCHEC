package com.example.dchec;

public class Association {
    String Uid , id , localisation ,password , phoneNumber , userName;

    public Association() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Association(String uid, String id, String localisation, String password, String phoneNumber, String userName) {
        Uid = uid;
        this.id = id;
        this.localisation = localisation;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
    }
}
