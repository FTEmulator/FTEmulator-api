package com.ftemulator.FTEmulator_api.entities.profile;

public class User {
    public String name;
    public String email;
    public String password;
    public String country;
    public int experience;
    public String photo;
    public String biography;

    // Mandatory
    public User(String name, String email, String password, String country) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.country = country;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCountry() {
        return country;
    }

    public int getExperience() {
        return experience;
    }

    public String getPhoto() {
        return photo;
    }

    public String getBiography() {
        return biography;
    }

    // Optional setters
    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
