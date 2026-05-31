/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.DAO;
import java.sql.Connection;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
/**
 *
 * @author user
 */
public class OrderDAO {
  Connection conn;
  
  public OrderDAO(){
    this.conn = DBConnection.getInstance().getDBConnection();
  }
  
  
}
