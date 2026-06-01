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
public class CategoryDAO {
  Connection conn;
  String sql;
  PreparedStatement pstmt;
  
  public CategoryDAO(){
    this.conn = DBConnection.getInstance().getDBConnection();
  }
  
  public boolean isCategoryAlreadyExists(String categoryName) throws SQLException {
    sql = """
      SELECT *
      FROM tbl_categories
      WHERE  categoryName = UPPER(?);
    """;
    
    pstmt = conn.prepareStatement(sql);
    pstmt.setString(1,categoryName);

    ResultSet rs = pstmt.executeQuery();
    
    return(rs.next());
  }
  
  public ResultSet getCategories(boolean isActive){
    try{
      sql = """
        SELECT 
            categoryName,
            description,
            unit
        FROM tbl_categories
        WHERE isActive = ?;
      """;
      
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, ((isActive)? 1:0) );
      return pstmt.executeQuery();
    }catch(SQLException ex){
      ex.printStackTrace();
      return null;
    }
  }
  
  public ResultSet getCategoryNames(){
    try{
      sql = """
        SELECT categoryName FROM tbl_categories;
      """;
      
      pstmt = conn.prepareStatement(sql);
      return pstmt.executeQuery();
    }catch(SQLException ex){
      ex.printStackTrace();
      return null;
    }
  }
  
  public int setCategoryStatus(String categoryName, boolean isActive){
    try{
      sql = """
        UPDATE tbl_categories
        SET
          isActive = ?
        WHERE categoryName = ?;
      """;
      
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, ((isActive)? 1:0) );
      pstmt.setString(2, categoryName);
      return pstmt.executeUpdate();
    }catch(SQLException ex){
      ex.printStackTrace();
      return 0;
    }
  }
  
  public int updateCategoryInfo(String new_category_name, String description, String unit, String old_category_name){
    try{
      sql = """
            UPDATE tbl_categories
            SET categoryName = UPPER(?),
                description = LOWER(?),
                unit = LOWER(?)
            WHERE categoryName = UPPER(?);
        """;

      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,new_category_name);
      pstmt.setString(2,description);
      pstmt.setString(3,unit);
      pstmt.setString(4,old_category_name);
      
      return pstmt.executeUpdate();
      
    }catch(SQLException ex){
      ex.printStackTrace();
      return 0;
    }
  }
  
  public int addNewCategory(String new_category_name, String new_description, String new_unit){
    try{
      sql = """
        INSERT INTO tbl_categories(
            categoryName,
            description,
            unit
          )
        VALUES(UPPER(?),?,?);
      """;

      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,new_category_name);
      pstmt.setString(2,new_description);
      pstmt.setString(3,new_unit);
      
      return pstmt.executeUpdate();
    }catch(SQLException ex){
      ex.printStackTrace();
      return 0;
    }
    
  }
}
