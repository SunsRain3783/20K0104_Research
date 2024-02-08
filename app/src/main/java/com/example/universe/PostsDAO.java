package com.example.universe;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostsDAO {

    @Insert
    long insert(PostsEntity entity);

    @Query("SELECT * FROM PostsTable")
    List<PostsEntity> getAll();

    @Query("DELETE FROM PostsTable")
    void deleteAll();

    @Query("SELECT * FROM PostsTable ORDER BY id DESC LIMIT 20")
    List<PostsEntity> getLatestPosts();

    @Query("SELECT * FROM PostsTable WHERE id = :id LIMIT 1")
    PostsEntity getPostById(int id);

    @Query("SELECT * FROM PostsTable WHERE name = :facilityName LIMIT 1")
    PostsEntity getPostByFacilityName(String facilityName);

    @Update
    void update(PostsEntity entity);
}
