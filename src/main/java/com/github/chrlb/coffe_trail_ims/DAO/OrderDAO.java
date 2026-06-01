/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.DAO;
import java.sql.Connection;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author user
 */
public class OrderDAO {
  Connection conn;
  String sql;
  PreparedStatement pstmt;
  
  public OrderDAO(){
    this.conn = DBConnection.getInstance().getDBConnection();
  }
  
  public ResultSet getOrdersOrderID(){
    try{
      sql = """
        SELECT orderID 
        FROM tbl_orders 
        ORDER BY orderID DESC; 
      """;
      
      pstmt = conn.prepareStatement(sql);
      return pstmt.executeQuery();
    }catch(SQLException ex){
      ex.printStackTrace();
      return null;
    }
  }
  
  public ResultSet getOrdersCustomerName(){
    try{
      sql = """
        SELECT  DISTINCT customerName AS customerName,
                orderDate 
        FROM tbl_orders 
        ORDER BY orderDate DESC; 
      """;
      
      pstmt = conn.prepareStatement(sql);
      return pstmt.executeQuery();
    }catch(SQLException ex){
      ex.printStackTrace();
      return null;
    }
  }
}
