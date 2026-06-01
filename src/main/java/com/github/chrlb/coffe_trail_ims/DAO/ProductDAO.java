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
public class ProductDAO {
  String sql;
  Connection conn;
  PreparedStatement pstmt;
  
  public ProductDAO(){
    this.conn = DBConnection.getInstance().getDBConnection();
  }
  
  public ResultSet getProducts(boolean isActive){
    try{
      sql = """
        SELECT 
            p.productID as ID,
            p.productName,
            p.categoryName as category,
            p.unitPrice,
            c.unit,
            p.stockQuantity as stock,
            p.lowStockthreshold as "lowStock threshold",
            DATE_FORMAT(p.dateCreated,"%Y-%d-%m") as dateCreated
        FROM tbl_products as p
        inner join tbl_categories as c 
            on  p.categoryName = c.categoryName
        WHERE p.isActive = ?;
      """;
      
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, ((isActive)? 1:0) );
      
      return pstmt.executeQuery();
    }catch(SQLException ex){
      ex.printStackTrace();
      return null;
    }
  }
  
  public ResultSet getLowStockProducts(){
    try{
      sql = """
        SELECT 
            p.productID,
            p.productName as name,
            IF(p.stockQuantity = 0,'OUT OF STOCK',p.stockQuantity) as stocks 
        FROM tbl_products as p 

        WHERE p.stockQuantity <= p.lowStockthreshold
            AND P.isActive = 1;
       """;

      pstmt = conn.prepareStatement(sql);
      return pstmt.executeQuery();
    }catch(SQLException ex){
      ex.printStackTrace();
      return null;
    }
  }
  
  public ResultSet getBestSellerProducts(){
    try{
      sql = """
        SELECT 
          P.productID,
          P.productName       AS name,
          P.unitPrice         AS price,
          P.categoryName      AS category,
          S.total_sale
        FROM tbl_products AS P
        
        INNER JOIN (
        
          SELECT
              productID,
              SUM(quantity) AS total_sale
          FROM tbl_sales 
            
          GROUP BY productID
        ) AS S ON P.productID = S.productID
        
        WHERE S.total_sale = (
          SELECT MAX(sales.total_sale)
          FROM tbl_products AS product

          INNER JOIN (
            SELECT
              productID,
              SUM(quantity) AS total_sale
            FROM tbl_sales
            GROUP BY productID
          ) AS sales ON product.productID = sales.productID

          WHERE product.categoryName = P.categoryName
           AND P.isActive = 1
        )
            
        ORDER BY total_sale DESC;
      """;

      pstmt = conn.prepareStatement(sql);
      return pstmt.executeQuery();
    }catch(SQLException ex){
      ex.printStackTrace();
      return null;
    }
  }
  
  public ResultSet getProductCategories(boolean isActive){
    try{
      sql = """
          SELECT 
            categoryName
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
  
  public ResultSet getProductNames(){
    try{
      sql = """
        SELECT  productName FROM tbl_products; 
      """;
      
      pstmt = conn.prepareStatement(sql);
      return pstmt.executeQuery();
    }catch(SQLException ex){
      ex.printStackTrace();
      return null;
    }
  }
  
  public int setProductStatus(int productID, boolean isActive){
   try{
     sql = """
        UPDATE tbl_products
        SET
          isActive = ?
        WHERE productID = ?;
      """;
     
     pstmt = conn.prepareStatement(sql);
     pstmt.setInt(1, ((isActive)? 1:0) );
     pstmt.setInt(2, productID );
     
     return pstmt.executeUpdate();
   } catch(SQLException ex){
     ex.printStackTrace();
     return 0;
   }
  }
  
  public int updateProductInfo( int productID, String product_name, String category, double unit_price, int low_stock_threshold){
    try{
      sql = """
        UPDATE tbl_products
        SET
          productName = ?,
          categoryName = ?,
          unitPrice = ?,
          lowStockThreshold = ?
        WHERE productID = ?;
      """;

      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, product_name);
      pstmt.setString(2, category);
      pstmt.setDouble(3, unit_price);
      pstmt.setInt(4, low_stock_threshold);
      pstmt.setInt(5, productID);
      
      return pstmt.executeUpdate();
    }catch(SQLException ex){
      ex.printStackTrace();
      return 0;
    }
  }

  public int deductProductStock(int productID, int amount){
    try{
      sql = """
        UPDATE tbl_products
        SET stockQuantity = stockQuantity - ?
        WHERE productID = ?;
      """;
      
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, amount);
      pstmt.setInt(2, productID);
      
      return pstmt.executeUpdate();
    }catch(SQLException ex){
      ex.printStackTrace();
      return 0;
    }
  }
  
  public int restockProductStock(int productID, int amount){
    try{
      sql = """
        UPDATE tbl_products
        SET stockQuantity = stockQuantity + ?
        WHERE productID = ?;
      """;  
      
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, amount);
      pstmt.setInt(2, productID);
      
      return pstmt.executeUpdate();
    }catch(SQLException ex){
      ex.printStackTrace();
      return 0;
    }
  }
  
  public boolean isProductAlreadyExists(String productName, String category) throws SQLException{
    sql = """
      SELECT *
      FROM tbl_products
      WHERE   productName = LOWER(?) OR ? = 'ALL'
          AND categoryName = ? OR ? = 'ALL';
    """;

    pstmt = conn.prepareStatement(sql);
    pstmt.setString(1,productName);
    pstmt.setString(2,productName);
    pstmt.setString(3,category);
    pstmt.setString(4,category);

    ResultSet rs = pstmt.executeQuery();
    return (rs.next());
  }
  
  public int addNewProduct(String productName,String category, double price, int stockQuantity, int lowStockThreshold){
    try{
      sql = """
        INSERT INTO tbl_products(
            productName,
            categoryName,
            unitPrice,
            stockQuantity,
            lowStockThreshold
          )
        VALUES(LOWER(?),?,?,?,?);
      """;

      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,productName);
      pstmt.setString(2,category);
      pstmt.setDouble(3,price);
      pstmt.setInt(4,stockQuantity);
      pstmt.setInt(5,lowStockThreshold);
      
      return pstmt.executeUpdate();
    }catch(SQLException ex ){
      ex.printStackTrace();
      return 0;
    }
  }
}
