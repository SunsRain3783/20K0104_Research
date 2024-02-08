package com.example.universe;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "groupTable")
public class GroupEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int participateFlag;
    public int openFlag;
    public String groupName;
    public String participants;
    public String detail;
    public String image;

    public GroupEntity(int participateFlag, int openFlag, String groupName, String participants, String detail, String image){
        this.participateFlag = participateFlag;
        this.openFlag = openFlag;
        this.groupName = groupName;
        this.participants = participants;
        this.detail = detail;
        this.image = image;
    }

    public int getId(){
        return this.id;
    }

    public int getParticipateFlag(){
        return this.participateFlag;
    }

    public int getOpenFlag(){
        return this.openFlag;
    }

    public String getGroupName(){
        return this.groupName;
    }

    public String getParticipants(){ return this.participants; }

    public String getDetail() {return this.detail;}

    public String getImage() {return this.image;}

    public void setId(int id) {
        this.id = id;
    }

    public void setParticipateFlag(int participateFlag) {
        this.participateFlag = participateFlag;
    }
}
