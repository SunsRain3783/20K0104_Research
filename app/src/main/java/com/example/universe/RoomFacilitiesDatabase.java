package com.example.universe;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RoomEntity.class, GroupEntity.class, InformationEntity.class, PostsEntity.class, RepliesEntity.class}, version = 57, exportSchema = false)
public abstract class RoomFacilitiesDatabase extends RoomDatabase {

    public abstract RoomDAO roomdao();
    public abstract GroupDAO groupdao();
    public abstract InformationDAO informationdao();
    public abstract PostsDAO postsdao();
    public abstract RepliesDAO repliesdao();
}
