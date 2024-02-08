package com.example.universe;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface InformationDAO {

    @Insert
    long insert(InformationEntity entity);

    @Query("SELECT * FROM InformationTable")
    List<InformationEntity> getAll();

    @Query("SELECT * FROM InformationTable WHERE title = :title")
    InformationEntity findByTitle(String title);

    @Query("SELECT * FROM InformationTable WHERE name = :facilityName")
    InformationEntity getByName(String facilityName);

    @Query("SELECT * FROM InformationTable WHERE name <> ''")
    List<InformationEntity> getInformationsWithNotEmptyName();

    @Query("DELETE FROM InformationTable")
    void deleteAll();

    @Update
    void update(InformationEntity entity);
}
