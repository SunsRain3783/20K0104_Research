package com.example.universe;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomDAO {
    @Query("SELECT * FROM nishikan")
    List<RoomEntity> getall();

    @Query("DELETE FROM nishikan")
    void deleteall();

    @Query("DELETE FROM nishikan WHERE id = :x")
    void delete(int x);

    @Query("SELECT * FROM nishikan WHERE name = :name")
    RoomEntity findByName(String name);

    @Query("SELECT image FROM nishikan WHERE name = :name LIMIT 1")
    String getImageByName(String name);

    @Query("UPDATE nishikan SET tag = :newTag WHERE name = :name")
    void updateTag(String name, String newTag);

    @Query("SELECT * FROM nishikan WHERE name LIKE :searchText")
    List<RoomEntity> searchByName(String searchText);

    @Query("SELECT name FROM nishikan WHERE name LIKE :query LIMIT 10")
    List<String> getNamesMatchingQuery(String query);

    @Query("SELECT * FROM nishikan WHERE tag LIKE :tag")
    List<RoomEntity> searchByTag(String tag);

    @Update
    void update(RoomEntity entity);

    @Insert
    void insert(RoomEntity roomentity);
}
