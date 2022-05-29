package com.example.dchec;

public class Posts {
    public String userName , uid , title , postImage , description , price ;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Posts(){

    }
    public Posts(String userName,String title,String postImage){
        this.userName = userName;
        this.title = title;
        this.postImage = postImage;

    }

    public Posts(String userName, String uid, String title, String postImage, String description) {
        this.userName = userName;
        this.uid = uid;
        this.title = title;
        this.postImage = postImage;
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
