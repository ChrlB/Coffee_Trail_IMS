/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 *
 * @author user
 */
public class DBConnection {
  
  private static DBConnection instance = null;
  private  Connection conn;
  
  private DBConnection(){
    try{
      Properties prop = new Properties();
      prop.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
      
      String url = prop.getProperty("DB_URL");
      String user = prop.getProperty("DB_USER");
      String pass = prop.getProperty("DB_PASS");
      
      conn = DriverManager.getConnection( url, user, pass);
      
      System.out.println(conn);
    
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
  
  public  static DBConnection getInstance(){
    if(instance == null){
      instance = new DBConnection();
    }
    return instance;
  }
  
  public  Connection getDBConnection(){
    return conn;
  }
  
  
}
