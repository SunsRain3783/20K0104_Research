package com.example.universe;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RepliesDAO {

    @Insert
    long insert(RepliesEntity entity);

    @Query("SELECT * FROM RepliesTable")
    List<RepliesEntity> getAll();

    @Query("SELECT * FROM RepliesTable WHERE post_id = :postId")
    List<RepliesEntity> getRepliesForPost(int postId);

    @Query("DELETE FROM RepliesTable")
    void deleteAll();

    @Update
    void update(RepliesEntity entity);
}