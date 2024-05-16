package com.example.cheapy.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.cheapy.entities.User;

@Entity
public class Group {
    @PrimaryKey
    private int id;
    private User[] members;


    public Group(int id, User[] members) {
        this.id = id;
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User[] getMembers() {
        return members;
    }

    public void setMembers(User[] members) {
        this.members = members;
    }
}
