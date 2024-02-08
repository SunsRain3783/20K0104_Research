package com.example.universe;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "InformationTable")
public class InformationEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String detail;
    private int flag; // 既読かどうかの判断
    private String label;
    private String time;
    private String name;
    private String image;

    public InformationEntity(String title, String detail, int flag, String label, String time, String name, String image){
        this.title = title;
        this.detail = detail;
        this.flag = flag;
        this.label = label;
        this.time = time;
        this.name = name;
        this.image = image;
    }

    public int getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDetail(){
        return this.detail;
    }

    public int getFlag(){
        return this.flag;
    }

    public String getLabel(){
        return this.label;
    }

    public String getTime(){
        return this.time;
    }

    public String getName(){
        return this.name;
    }

    public String getImage(){
        return this.image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail){
        this.detail = detail;
    }

    public void setFlag(int flag){
        this.flag = flag;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public void setTime(String time){
        this.time = time;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setImage(String image){
        this.image = image;
    }
}
