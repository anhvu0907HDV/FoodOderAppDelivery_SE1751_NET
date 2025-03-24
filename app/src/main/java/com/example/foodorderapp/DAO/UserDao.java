package com.example.foodorderapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.example.foodorderapp.Entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user_table")
    List<User> getAllUsers();

    @Query("SELECT * FROM user_table WHERE username = :username")
    User getUserByUsername(String username);
    @Query("SELECT * FROM user_table WHERE email = :email")
    User getUserByEmail(String email);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Query("DELETE FROM user_table WHERE username = :username")
    void deleteUserByUsername(String username);

    @Query("DELETE FROM user_table")
    void deleteAllUsers();

    // delete by email
    @Query("DELETE FROM user_table WHERE email = :email")
    void deleteUserByEmail(String email);

    // delete by id
    @Query("DELETE FROM user_table WHERE id = :id")
    void deleteUserById(int id);

    // update username by id
    @Query("UPDATE user_table SET username = :username WHERE id = :id")
    void updateUsernameById(int id, String username);

    // count users
    @Query("SELECT COUNT(*) FROM user_table")
    int countUsers();

    // update all users
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAllUsers(List<User> users);
    @Update
    void updateUser(User user);
}
