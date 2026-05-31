/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.Services;

import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author user
 */
public class UserSession {
  
  private static UserSession instance = null;
  private  int userID = 0;
  private  String username = null;
  
  
  public void createSession(ResultSet userInfo){
    try {
      this.userID = userInfo.getInt("userID");
      this.username = userInfo.getString("username");
      
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
  
  public void resetSession(){
    this.userID = 0;
    this.username = null;
  }
  
  public boolean isUserAdmin(){
    return userID == 1;
  }
  
  public  static UserSession getInstance(){
    if(instance == null) {
      instance = new UserSession();
    }
    return instance;
  }
  
  public int getUserID(){
    return userID;
  }
}
