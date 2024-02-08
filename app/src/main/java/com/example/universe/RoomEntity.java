package com.example.universe;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nishikan")
public class RoomEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String seat;
    public String time;
    public String elec;
    public String detail;
    public String tag;
    public String image;

    public RoomEntity(String name, String seat, String time, String elec, String detail, String tag, String image){
        this.name = name;
        this.seat = seat;
        this.time = time;
        this.elec = elec;
        this.detail = detail;
        this.tag = tag;
        this.image = image;
    }

    public int getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public String getSeat(){
        return this.seat;
    }
    public String getTime(){
        return this.time;
    }
    public String getElec(){
        return this.elec;
    }
    public String getDetail(){
        return this.detail;
    }

    public String getTag(){
        return this.tag;
    }
    public String getImage() {return image;}

    public void setTag(String tag) {
        this.tag = tag;
    }
}
