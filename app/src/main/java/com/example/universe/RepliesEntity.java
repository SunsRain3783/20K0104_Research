package com.example.universe;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RepliesTable")
public class RepliesEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int post_id;
    private String user;
    private String content;
    private String name;
    private String roomimage;
    private String time;

    public RepliesEntity(int post_id, String user, String content, String name, String roomimage, String time){
        this.post_id = post_id;
        this.user = user;
        this.content = content;
        this.name = name;
        this.roomimage = roomimage;
        this.time = time;
    }

    public int getId(){
        return this.id;
    }

    public int getPost_id() {return this.post_id; }

    public String getUser(){
        return this.user;
    }

    public String getContent(){
        return this.content;
    }

    public String getName(){
        return this.name;
    }

    public String getRoomimage(){
        return this.roomimage;
    }

    public String getTime(){
        return this.time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
