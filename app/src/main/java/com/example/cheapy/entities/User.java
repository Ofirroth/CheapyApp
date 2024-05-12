package com.example.cheapy.entities;

public class User {

    private String username;
    private String displayName;
    private String profilePic;

    public User( String username,String displayName, String profilePic) {
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.username=username;
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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
