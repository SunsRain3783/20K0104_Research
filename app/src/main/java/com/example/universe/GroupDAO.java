package com.example.universe;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GroupDAO {
    @Insert
    long insert(GroupEntity entity);
    @Query("SELECT * FROM groupTable")
    List<GroupEntity> getAll();

    @Query("DELETE FROM groupTable")
    void deleteAll();

    @Query("SELECT * FROM groupTable WHERE id = :id")
    GroupEntity getGroupById(int id);

    @Query("DELETE FROM groupTable WHERE id = :x")
    void delete(int x);

    @Update
    void update(GroupEntity groupEntity);

}
