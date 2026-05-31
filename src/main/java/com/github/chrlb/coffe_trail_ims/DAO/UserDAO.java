/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
/**
 *
 * @author user
 */
public class UserDAO {
  Connection conn;
  PreparedStatement pstmt;
  
  String sql;
  
  public UserDAO(){
    this.conn = DBConnection.getInstance().getDBConnection();
  }
  
  public ResultSet getUserInfoByUsername(String input_username){
    try{
      sql = """
            SELECT * 
            FROM tbl_users 
            WHERE username = ?
            AND isActive = 1;
      """;
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, input_username);
      
      //execute the sql command then store the result to ResultSet object
      return pstmt.executeQuery();
      
    }catch(SQLException ex){
      ex.printStackTrace();
      return null;
    }
  }
  
  public ResultSet getUserInfoByUserID(int user_id){
    try{
      sql = """
            SELECT * 
            FROM tbl_users 
            WHERE userID = ?
            AND isActive = 1;
      """;
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, user_id);
      
      //execute the sql command then store the result to ResultSet object
      return pstmt.executeQuery();
      
    }catch(SQLException ex){
      ex.printStackTrace();
      return null;
    }
  }
  
  public ResultSet getCurrentUser(){
    try{
      
      sql = """
            SELECT * 
            FROM tbl_users 
            WHERE logoutDate IS NULL
              AND isActive = 1;
      """;
      pstmt = conn.prepareStatement(sql);
      
      //execute the sql command then store the result to ResultSet object
      return pstmt.executeQuery();
      
    }catch(SQLException ex){
      ex.printStackTrace();
      return null;
    }
  }
  
  public void recordUserLoginLog(int loggedin_user_ID){
    try{
      sql = "INSERT INTO tbl_userlogs(userID) VALUES(?)";
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, loggedin_user_ID);

      pstmt.executeUpdate();
      System.out.println("Login Successful!");
    }catch(Exception ex){
      ex.printStackTrace();
    }
    
  }
  
  public void recordUserLogoutLog(int loggedin_user_ID){
    try{
      sql = """
          UPDATE tbl_userlogs 
          SET logoutDate = CURRENT_TIMESTAMP 
          WHERE userID = ? 
          AND logoutDate IS NULL
        """;
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, loggedin_user_ID);
      pstmt.executeUpdate();
      
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
}
