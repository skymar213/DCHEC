package com.example.dchec;

public class User {
    String nom;
    String prenom;
    String email;
    String profilePicture;

    public User() {
    }

    public User(String nom,String prenom ,String email , String profilePicture) {
        this.nom = nom;
        this.prenom = prenom;
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom(){return prenom;}

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}


