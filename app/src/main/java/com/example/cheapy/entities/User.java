package com.example.cheapy.entities;

public class User {

    private String username;
    private String displayName;
    private String profilePic;
    private String homeAddress;
    private String workAddress;
    private String mail;
    private String phone;

    private double userLatitude;

    private double userLongitude;

    public User( String username,String displayName, String profilePic, String homeAddress, String workAddress, String mail, String phone, double userLatitude, double userLongitude) {
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.username=username;
        this.homeAddress = homeAddress;
        this.workAddress = workAddress;
        this.phone = phone;
        this.mail = mail;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

    public String getDisplayName() {
        return displayName;
    }

    public double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
