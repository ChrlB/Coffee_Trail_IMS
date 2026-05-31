/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.github.chrlb.coffe_trail_ims.UI.Windows.Dashboard;
import javax.swing.JFrame;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.Services.UserSession;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.*;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;

/**
 *
 * @author user
 */
public class Order extends JFrame{
  Connection conn;
  int user_ID, total_order_price ;
  ArrayList<Object[]> pendingItems ;
  
  
  
  Header header ;
  JPanel order_form_panel;
  
  TableBuilder product_tbl;
  JScrollPane product_tbl_scrollpane,
              order_list_scrollpane;
  
  ComboBoxBuilder product_category_combobox;
  SpinnerBuilder  quantity_spinner;
  
  TextFieldBuilder customer_name_field,
                   productID_field,
                   productName_field;
  JTextArea order_list;
  
  LabelBuilder  order_list_label,
                total_order_price_label,
                customer_name_field_label,
                productID_field_label,
                productName_field_label,
                quantity_spinner_label,
                product_category_combobox_label;
  
  ButtonBuilder add_btn,
                cancel_btn,
                confirm_btn;
  
  PreparedStatement pstmt,
                    insert_sales_pstmt,
                    deduct_stock_pstmt;
  
  ResultSet rs,
            product_rs;
  String sql;
  
  Object[] selected_record;
  
  public Order(){
    try{
      this.conn = DBConnection.getInstance().getDBConnection();
      this.user_ID = UserSession.getInstance().getUserID();
      
      total_order_price = 0;
      pendingItems = new ArrayList<>();

      header = new Header();

      order_form_panel = new JPanel();
      order_form_panel.setLayout(null);
      order_form_panel.setBounds(0,100,700,550);
      order_form_panel.setBackground(new Color(0XB58863));
      
      sql = """
            INSERT INTO  tbl_sales(
                  orderID, 
                  productID,
                  salePrice,
                  quantity) 
            VALUES (?, ?, ?, ?)
            """;
      insert_sales_pstmt = conn.prepareStatement(sql);
      
      sql = """
            UPDATE  tbl_products
            SET stockQuantity = stockQuantity - ?
            WHERE productID = ?;
            """;
      deduct_stock_pstmt = conn.prepareStatement(sql);
      
      add_btn = new ButtonBuilder("ADD",325,340,150,45,15);
      add_btn.addActionListener((a) -> addItemToOrder());
      
      cancel_btn= new ButtonBuilder("CANCEL",500,340,150,45,15);
      cancel_btn.addActionListener((a) -> cancelOrder());
      
      confirm_btn= new ButtonBuilder("CONFIRM ORDER",325,405,325,45,15);
      confirm_btn.addActionListener((a) -> confirmOrder());
     
      sql = """
        SELECT 
          DISTINCT categoryName
        FROM tbl_products 
        WHERE stockQuantity > 0; 
      """;

      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      
      product_category_combobox = new ComboBoxBuilder("ALL",500,270,150,40,15);
      while(rs.next()){
        product_category_combobox.addItem(rs.getString("categoryName"));
      }
      product_category_combobox.addActionListener((e) -> updateTable());
      
      
      quantity_spinner = new SpinnerBuilder(0);
      quantity_spinner.setBounds(325,270,150,40);
      
      product_category_combobox_label = new LabelBuilder(" Product Category:",500,240,150,30, 15);
      quantity_spinner_label = new LabelBuilder(" Quantity:",325,240,150,30, 15);
      
      sql = """
        SELECT productID ,
               productName as name,
               categoryName as category,
               unitPrice as price,
               stockQuantity as stock
        FROM tbl_products
        WHERE stockQuantity > 0;
        """;

      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      
     
      product_tbl = new TableBuilder(rs);
      product_tbl.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {  
            showSelectedRecord();
        }
      });
      
      product_tbl_scrollpane = new JScrollPane(product_tbl);
      product_tbl_scrollpane.setBounds(0,0,700,220);
      
      
      productID_field = new TextFieldBuilder(false,50,270,250,40,15);
      productName_field = new TextFieldBuilder(false,50,340,250,40,15);
      customer_name_field = new TextFieldBuilder(true,50,410,250,40,15);
      
      productID_field_label = new LabelBuilder(" ProductID:",50,240,250,30,15);
      productName_field_label = new LabelBuilder(" Product name:",50,310,250,30,15);
      customer_name_field_label = new LabelBuilder(" Customer name:",50,380,250,30,15);
      
      
      
      order_list_label = new LabelBuilder(" ORDER LIST:",725, 125, 500, 50,25);
      order_list_label.setOpaque(true); 
      order_list_label.setForeground(Color.WHITE);
      order_list_label.setBackground(new Color(0x3D4D55));
      
      order_list = new JTextArea();
      order_list.setText("");
      order_list.setEditable(false);
      order_list.setFont(new Font("Arial", Font.BOLD, 20));
      order_list.setForeground(new Color(0x3D4D55));
      
      order_list_scrollpane = new JScrollPane(order_list);
      order_list_scrollpane.setBounds(725, 175, 500, 320);
      
      total_order_price_label = new LabelBuilder(" TOTAL: ₱0",725, 500, 500, 50,25);
      total_order_price_label.setOpaque(true);
      total_order_price_label.setBackground(Color.WHITE);
      
      
      ImageIcon icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_CUP));
      this.setIconImage(icon.getImage());
      this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      this.setTitle("ORDER");
      this.setLayout(null);
      this.setResizable(false);
      this.setSize(1270,650);
      this.getContentPane().setBackground(new Color(0xD3C3B9));
      this.setLocationRelativeTo(null);

      this.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
          new Dashboard(); 
          dispose();
        }
      });
      
      order_form_panel.add(product_category_combobox);
      order_form_panel.add(quantity_spinner);
      order_form_panel.add(product_tbl_scrollpane);
      
      order_form_panel.add(productID_field);
      order_form_panel.add(productName_field);
      order_form_panel.add(customer_name_field);
      
      order_form_panel.add(customer_name_field_label);
      order_form_panel.add(productID_field_label);
      order_form_panel.add(productName_field_label);
      order_form_panel.add(quantity_spinner_label);
      order_form_panel.add(product_category_combobox_label);
      
      order_form_panel.add(add_btn);
      order_form_panel.add(cancel_btn);
      order_form_panel.add(confirm_btn);

      this.add(header);
      this.add(order_form_panel);
      
      this.add(order_list_label);
      this.add(order_list_scrollpane);
      this.add(total_order_price_label);
      
      this.setVisible(true);
    }catch(Exception ex){
      System.out.println(ex);
    }
  }
  
  private void updateTable(){
    try {
      String selected_category = product_category_combobox.getSelectedItem().toString();
      
      if(selected_category.equals("ALL")){
        sql = """
        SELECT productID ,
               productName as name,
               categoryName as category,
               unitPrice as price,
               stockQuantity as stock
        FROM tbl_products
        WHERE stockQuantity > 0 ;
        """;
        pstmt = conn.prepareStatement(sql);
      }else{
        sql = """
        SELECT productID ,
               productName as name,
               categoryName as category,
               unitPrice as price,
               stockQuantity as stock
        FROM tbl_products
        WHERE stockQuantity > 0 and categoryName = ?;
        """;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, selected_category);
      }
      
      rs = pstmt.executeQuery();
      product_tbl.refreshTable(rs);
    } catch (SQLException ex) {
      Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
    }
    
  }
  
  public void showSelectedRecord(){
    try{
      int row = product_tbl.getSelectedRow();

      if (row != -1) {

        selected_record = new Object[] {
          product_tbl.getValueAt(row, 0)
//          product_tbl.getValueAt(row, 1), 
//          product_tbl.getValueAt(row, 2), 
//          product_tbl.getValueAt(row, 3)  
        };
      }
      
      sql = """
        SELECT 
            tbl_products.productID as productID,
            tbl_products.productName as productName,
            tbl_products.categoryName as categoryName,
            tbl_products.unitPrice as unitPrice,
            tbl_products.stockQuantity as stockQuantity,
            tbl_categories.unit as unit
        FROM tbl_products
        INNER JOIN tbl_categories 
            ON tbl_products.categoryName = tbl_categories.categoryName
        WHERE tbl_products.stockQuantity > 0 and tbl_products.productID = ?;
      """;
      
      //System.out.println("ID "+(int)selected_record[0]);
      pstmt = conn.prepareStatement(sql);
      pstmt.setInt(1, (int)selected_record[0]);
      product_rs = pstmt.executeQuery();
      
      
      
      if(product_rs.next()){
        int availableStock = product_rs.getInt("stockQuantity");
        for (Object[] item : pendingItems) {
          if((int)selected_record[0] == (int) item[0]){
            availableStock -= (int)item[2];
            break;
          }
        }
        
        quantity_spinner.setMax(availableStock);
        
        productID_field.setText(""+product_rs.getInt("productID"));
        
        productName_field.setText(product_rs.getString("productName"));
      }
      
      
    }catch(Exception ex){
      System.out.print(ex.getCause());
    }
  }
  
  public void addItemToOrder(){
    try{
      int productID = product_rs.getInt("productID");
      int quantity = (int)(quantity_spinner.getValue()); 
      double salePrice = product_rs.getDouble("unitPrice");
      boolean isItemAlreadyAdded = false;
      
      if(quantity < 1){
        JOptionPane.showMessageDialog(null,
                "Quantity cannot be 0.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
        return;
      }
      
      for (Object[] item : pendingItems) {
        if(productID == (int) item[0]){
          item[2] = ((int)item[2] + quantity);
          isItemAlreadyAdded = true;
          break;
        }
      }
      
      if(!isItemAlreadyAdded) {
        System.out.print("added");
        pendingItems.add(new Object[]{productID,salePrice, quantity});
      }
      
      String productName = product_rs.getString("productName");
      String unit = product_rs.getString("unit");
      int unit_price = product_rs.getInt("unitPrice");
      
      int total_item_price = (unit_price * quantity);
      total_order_price += total_item_price;
      
      order_list.setText(
        order_list.getText() + "\n [" +
        pendingItems.size() + "]  " +
        quantity +" "+ unit + "  " +
        productName + " - ₱" + total_item_price
      );
      
      total_order_price_label.setText(" TOTAL: ₱"+total_order_price);
      showSelectedRecord();
    }catch(Exception ex){
      System.out.print(ex.getCause());
      JOptionPane.showMessageDialog(null, "No items added!");
    }
  }
  
  public void resetOrder(){
    try{
      total_order_price = 0;
      pendingItems.clear();
      insert_sales_pstmt.clearBatch();
      deduct_stock_pstmt.clearBatch();
      order_list.setText("");
      total_order_price_label.setText(" TOTAL: ₱"+total_order_price);
    }catch(Exception ex){
      ex.printStackTrace(); 
    }
  }
  
  public void cancelOrder(){
    try{
      resetOrder();
      showSelectedRecord();
      JOptionPane.showMessageDialog(null, "order canceled!");
    }catch(Exception ex){
      ex.printStackTrace(); 
    }
  }
  
  public void confirmOrder(){
    
    try {
      if (pendingItems.isEmpty()) {
        JOptionPane.showMessageDialog(null, "No items added!","Message",JOptionPane.WARNING_MESSAGE);
        return;
      }else if(customer_name_field.getText().trim().equals("")){
        JOptionPane.showMessageDialog(null, "customer name is required!","Message",JOptionPane.WARNING_MESSAGE);
        return;
      }


      // Insert order, get generated key
      sql = "INSERT INTO tbl_orders(userID,customerName) VALUES (?,?)";

      pstmt = conn.prepareStatement(
        sql,
        Statement.RETURN_GENERATED_KEYS
      );
      pstmt.setInt(1, user_ID);
      pstmt.setString(2,customer_name_field.getText().trim());
      pstmt.executeUpdate();
      rs = pstmt.getGeneratedKeys();

      if (rs.next()) {
        int orderID = rs.getInt(1);

        // Build batch
        for (Object[] item : pendingItems) {
          insert_sales_pstmt.setInt(1, orderID);   // orderID
          insert_sales_pstmt.setInt(2, (int)item[0]);   // productID
          insert_sales_pstmt.setDouble(3, (double)item[1]);
          insert_sales_pstmt.setInt(4, (int)item[2]);   // quantity
          insert_sales_pstmt.addBatch();
          
          deduct_stock_pstmt.setInt(1, (int)item[2]);
          deduct_stock_pstmt.setInt(2, (int)item[0]);
          deduct_stock_pstmt.addBatch();
        }
      }
      int[] rowsAffected = deduct_stock_pstmt.executeBatch();
      insert_sales_pstmt.executeBatch();
      
      JOptionPane.showMessageDialog(null, "Order confirmed!");
      
      customer_name_field.setText("");
      updateTable();
      resetOrder();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
  }
}
