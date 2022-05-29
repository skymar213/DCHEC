package com.example.dchec;

public class User {
    String userName;
    String nickName;
    String email;
    String profilePicture;

    public User() {
    }

    public User(String userName, String nickName, String email , String profilePicture) {
        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
        this.profilePicture = profilePicture;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName(){return nickName;}

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}


