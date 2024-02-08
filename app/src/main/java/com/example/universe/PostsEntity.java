package com.example.universe;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PostsTable")
public class PostsEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int like;
    private String user;
    private String content;
    private String name;
    private String roomimage;
    private String image;
    private String time;

    public PostsEntity(int like, String user, String content, String name, String roomimage, String image, String time){
        this.like = like;
        this.user = user;
        this.content = content;
        this.name = name;
        this.roomimage = roomimage;
        this.image = image;
        this.time = time;
    }

    public int getId(){
        return this.id;
    }

    public int getLike(){
        return this.like;
    }

    public String getUser(){
        return this.user;
    }

    public String getContent(){
        return this.content;
    }

    public String getRoomimage() { return this.roomimage; }

    public String getName(){
        return this.name;
    }

    public String getImage() { return this.image; }

    public String getTime(){
        return this.time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLike(int like) { this.like = like; }
}
