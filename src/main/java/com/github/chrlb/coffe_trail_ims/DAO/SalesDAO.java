/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.DAO;

import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author user
 */
public class SalesDAO {
  Connection conn;
  String sql;
  PreparedStatement pstmt;
  
  SalesDAO(){
    conn = DBConnection.getInstance().getDBConnection();
  }
  
  
}
