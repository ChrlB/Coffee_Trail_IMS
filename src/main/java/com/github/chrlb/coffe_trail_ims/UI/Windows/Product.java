/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Windows;

import com.github.chrlb.coffe_trail_ims.UI.Windows.Dashboard;
import com.github.chrlb.coffe_trail_ims.UI.Windows.Category;
import com.github.chrlb.coffe_trail_ims.UI.Forms.ReStock;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.sql.*;
import com.github.chrlb.coffe_trail_ims.DAO.ProductDAO;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.Services.UserSession;
import com.github.chrlb.coffe_trail_ims.UI.Forms.DeductStock;
import com.github.chrlb.coffe_trail_ims.UI.Forms.NewProduct;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.*;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;

/**
 *
 * @author user
 */
public class Product extends JFrame{
  int user_ID;
  Connection conn;
  ProductDAO productDAO;
  
  Header header;
  JPanel product_form_panel;
  
  ButtonBuilder deduct_btn,
                delete_btn,
                update_btn,
                restock_btn,
                category_window_btn,
                add_product_btn,
                readd_product_btn;
  
  SpinnerBuilder lowStockThreshold_spinner;
  
  TextFieldBuilder product_id_field,
                   product_name_field, 
                   unit_price_field;
  
  ResultSet rs,
            products_rs;
  
  TableBuilder products_tbl;
  JScrollPane product_tbl_scroll_pane;
  
  LabelBuilder     product_id_field_label,
                   product_name_field_label, 
                   category_name_field_label,
                   unit_price_field_label, 
                   lowStockThreshold_field_label,
                   lowStockThreshold2_field_label;
  
  ComboBoxBuilder product_category_combobox,
                  product_combobox;
  Object[] selected_record;
  
  Product(){
    try{
      productDAO = new ProductDAO();
      this.conn = DBConnection.getInstance().getDBConnection();
      this.user_ID = UserSession.getInstance().getUserID();
      
      header = new Header();

      rs = productDAO.getProductCategories(true);
      product_category_combobox = new ComboBoxBuilder(155, 170, 255, 50);
      while(rs.next()){
        product_category_combobox.addItem(rs.getString("categoryName"));
      }
      
      product_combobox = new ComboBoxBuilder("Active",490, 115, 125, 30,14);
      product_combobox.addItem("Archived");
      
      category_window_btn = new ButtonBuilder("PRODUCT CATEGORY", 1040, 25, 200, 50,15);
      add_product_btn = new ButtonBuilder("ADD PRODUCT", 800, 25, 200, 50,15);
      readd_product_btn = new ButtonBuilder("RE-ADD PRODUCT", 640, 115, 175, 30,14);
      readd_product_btn.setEnabled(false);
      
      delete_btn = new ButtonBuilder("DELETE", 30, 370, 185, 50,15);
      update_btn = new ButtonBuilder("UPDATE", 225, 370, 185, 50,15);
      deduct_btn = new ButtonBuilder("DEDUCT", 30, 450, 185, 50,15);
      restock_btn = new ButtonBuilder("RESTOCK", 225, 450, 185, 50,15);
      
      category_window_btn.addActionListener( (a) -> { new Category();dispose();} );
      add_product_btn.addActionListener( (a) -> { new NewProduct(this , conn); this.setEnabled(false);  } );
      readd_product_btn.addActionListener( (a) -> { readdProduct();} );
              
      deduct_btn.addActionListener( (a) -> { deductProduct();} );
      delete_btn.addActionListener( (a) -> { deleteProduct();} );
      update_btn.addActionListener( (a) -> { updateProduct();} );
      restock_btn.addActionListener( (a) -> { restockProduct();} );
      
      product_combobox.addActionListener( (a) -> { refreshTable();});

      product_id_field = new TextFieldBuilder(false, 155, 50, 255, 50, 15); 
      product_name_field = new TextFieldBuilder(true, 155, 110, 255, 50, 15);
      unit_price_field = new TextFieldBuilder(true, 155, 230, 255, 50, 15);
      lowStockThreshold_spinner = new SpinnerBuilder(true,5,300);
      lowStockThreshold_spinner.setBounds( 155, 290, 255, 50);

      product_id_field_label = new LabelBuilder("Product ID: ",30,50,200,50,15);
      product_name_field_label = new LabelBuilder("Product Name: ",30,110,200,50,15);
      category_name_field_label = new LabelBuilder("Category Name: ",30,170,200,50,15);
      unit_price_field_label = new LabelBuilder("Unit Price: ",30,230,200,50,15);
      lowStockThreshold_field_label= new LabelBuilder("Low Stock ",30,280,200,50,15);
      lowStockThreshold2_field_label= new LabelBuilder("Threshold: ",30,300,150,50,15);

      header.add(category_window_btn);   
      header.add(add_product_btn);

      product_form_panel = new JPanel();
      product_form_panel.setBounds(0, 100, 430, 550);
      product_form_panel.setBackground (new Color(0XB58863));
      product_form_panel.setLayout(null);

      product_form_panel.add(product_id_field);
      product_form_panel.add(product_name_field);
      product_form_panel.add(product_category_combobox);
      product_form_panel.add(unit_price_field);
      product_form_panel.add(lowStockThreshold_spinner);

      product_form_panel.add(deduct_btn);
      product_form_panel.add(delete_btn);
      product_form_panel.add(update_btn);
      product_form_panel.add(restock_btn);

      product_form_panel.add (product_id_field_label);
      product_form_panel.add (product_name_field_label); 
      product_form_panel.add (category_name_field_label);
      product_form_panel.add (unit_price_field_label); 
      product_form_panel.add(lowStockThreshold_field_label);
      product_form_panel.add(lowStockThreshold2_field_label);
    
      
      products_rs = productDAO.getProducts(true);
      products_tbl = new TableBuilder(products_rs);
      products_tbl.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseReleased(MouseEvent e) {  
            showSelectedRecord();
        }
      });
      
      
      product_tbl_scroll_pane = new JScrollPane(products_tbl);
      product_tbl_scroll_pane.setBounds(440, 150, 810, 455);

      
      this.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
          new Dashboard(); 
          dispose();
        }
      });

      
      ImageIcon icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_CUP));
      this.setIconImage(icon.getImage());
      this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      this.setTitle("PRODUCT");
      this.setLayout(null);
      this.setResizable(false);
      this.setSize(1290,680);
      this.getContentPane().setBackground(new Color(0xD3C3B9));
      this.setLocationRelativeTo(null);
      
      this.add(header);
      this.add(product_form_panel);
      this.add(product_tbl_scroll_pane);
      this.add(product_combobox);
      this.add(readd_product_btn);
      
      this.setVisible(true);

    } catch(Exception ex) {

    }
  }
  
  public void showSelectedRecord(){
    try{
      int row = products_tbl.getSelectedRow();

      if (row != -1) {
        selected_record = new Object[] {
          products_tbl.getValueAt(row, 0),
          products_tbl.getValueAt(row, 1), 
          products_tbl.getValueAt(row, 2), 
          products_tbl.getValueAt(row, 3),
          products_tbl.getValueAt(row, 6)
        };
      }
     
      
      product_id_field.setText(""+selected_record[0]);
      product_name_field.setText(""+selected_record[1]);
      product_category_combobox.setSelectedItem(selected_record[2]);
      unit_price_field.setText(""+selected_record[3]);
      lowStockThreshold_spinner.setValue((int)selected_record[4]);
      
      
    }catch(Exception ex){
      System.out.print(ex.getCause());
    }
  }
  
  public void refreshTable(){
    try{
      boolean isActive = (product_combobox.getSelectedItem().toString().equals("Active"));
      
      readd_product_btn.setEnabled(!isActive);
      deduct_btn.setEnabled(isActive);
      delete_btn.setEnabled(isActive);
      update_btn.setEnabled(isActive);
      restock_btn.setEnabled(isActive);
      

      products_tbl.refreshTable(productDAO.getProducts(isActive));
      
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
  
  void deductProduct(){
    try{
      int row = products_tbl.getSelectedRow();
      if (row == -1) {
        JOptionPane.showMessageDialog(null,
          "Please select a record first.",
          "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
      } 
      
      
      selected_record = new Object[]{
        products_tbl.getValueAt(row, 0),
        products_tbl.getValueAt(row, 5)

      };
      
      
      new DeductStock(this, (int)selected_record[0],(int)selected_record[1]);
      this.setEnabled(false);
    }catch(Exception ex){
      ex.printStackTrace();
    
    }
  }
          
  void restockProduct(){
    try{
      int row = products_tbl.getSelectedRow();
      if (row == -1) {
        JOptionPane.showMessageDialog(null,
          "Please select a record first.",
          "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
      } 
      
      
      selected_record = new Object[]{
        products_tbl.getValueAt(row, 0)
      };
      
      
      new ReStock(this, (int)selected_record[0]);
      this.setEnabled(false);
    }catch(Exception ex){
      ex.printStackTrace();
    
    }
  }
  
  void readdProduct(){
    try{
      int row = products_tbl.getSelectedRow();
      if (row == -1) {
        JOptionPane.showMessageDialog(null,
          "Please select a record first.",
          "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
      } 
      
      
      selected_record = new Object[]{
        products_tbl.getValueAt(row, 0)
      };
      
      
      int command = JOptionPane.showConfirmDialog(null,
              "Do you want to proceed re-adding this Product?",
              "UPDATE CONFIRMATION", JOptionPane.OK_CANCEL_OPTION
      );if (!(command == JOptionPane.OK_OPTION)) return;
      
     
      int rowsAffected = productDAO.setProductStatus( (int)selected_record[0], true);
              
      
      if (rowsAffected > 0) {
          JOptionPane.showMessageDialog(null,
                  "Product re-added successfully.",
                  "Success", JOptionPane.INFORMATION_MESSAGE);
          refreshTable();
      } else {
          JOptionPane.showMessageDialog(null,
                  "No record was added. The Product may not exist.",
                  "Update Failed", JOptionPane.WARNING_MESSAGE);
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
  
  void updateProduct(){
    try{
      int row = products_tbl.getSelectedRow();
      if (row == -1) {
        JOptionPane.showMessageDialog(null,
          "Please select a record first.",
          "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
      }
      
      
      
      selected_record = new Object[]{
        products_tbl.getValueAt(row, 0),
        products_tbl.getValueAt(row, 1),
        products_tbl.getValueAt(row, 2),
        products_tbl.getValueAt(row, 3),
        products_tbl.getValueAt(row, 6)
      };
      
      
      int command = JOptionPane.showConfirmDialog(null,
              "Do you want to proceed updating this record?",
              "UPDATE CONFIRMATION", JOptionPane.OK_CANCEL_OPTION
      ); if (!(command == JOptionPane.OK_OPTION)) return;
      
      
      String new_product_name = product_name_field.getText().trim(); 
      String new_product_category = product_category_combobox.getSelectedItem().toString().trim();
      int new_lowstock_threshold = (int) lowStockThreshold_spinner.getValue(); 
      double new_unit_price = Double.parseDouble(
              (unit_price_field.getText().trim().isEmpty())? 
                      "0": 
                      unit_price_field.getText().trim()
      ); 
      
      
      if (new_product_name.isEmpty() || new_unit_price <= 0) {
          JOptionPane.showMessageDialog(null,
                  "Unit Price and Product Name cannot be empty or less than 1.",
                  "Validation Error", JOptionPane.WARNING_MESSAGE);
          return;
      }
      
      
      if( 
          new_product_name.equals(selected_record[1]) && 
          new_product_category.equals(selected_record[2]) && 
          new_unit_price == ((Number) selected_record[3]).doubleValue() &&
          new_lowstock_threshold == (int) selected_record[4]
        ){
        JOptionPane.showMessageDialog(null,
                  "No changes to update.",
                  "Message", JOptionPane.INFORMATION_MESSAGE);
        return;
      }else if((productDAO.isProductAlreadyExists(new_product_name, "ALL")) && !new_product_name.equals(selected_record[1])){
        JOptionPane.showMessageDialog(null, 
                "productName is already been used",
                "Warning",JOptionPane.WARNING_MESSAGE);
        return;
      }
      
      
      int rowsAffected = productDAO.updateProductInfo(
              ((int) selected_record[0]),
              new_product_name, 
              new_product_category,
              new_unit_price,
              new_lowstock_threshold
      );
      
              
      if (rowsAffected > 0) {
          JOptionPane.showMessageDialog(null,
                  "Product Record updated successfully.",
                  "Success", JOptionPane.INFORMATION_MESSAGE);
          refreshTable();
      } else {
          JOptionPane.showMessageDialog(null,
                  "No record was updated. The Product may not exist.",
                  "Update Failed", JOptionPane.WARNING_MESSAGE);
      }
      
    }catch(NumberFormatException num_ex){
      JOptionPane.showMessageDialog(null,
                  "Invalid input. Please enter a valid number for product price.",
                  "Update Failed", JOptionPane.WARNING_MESSAGE);
    }catch(Exception ex ){
      ex.printStackTrace();
          
    }
  }
  
  void deleteProduct(){
    try{
      int row = products_tbl.getSelectedRow();
      if (row == -1) {
        JOptionPane.showMessageDialog(null,
          "Please select a record first.",
          "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
      } 
      
      
      selected_record = new Object[]{
        products_tbl.getValueAt(row, 0),
        products_tbl.getValueAt(row, 5)
      };
      
      
      String message = ((int)selected_record[1] > 0)?
              "This product is currently in stock. Are you sure you want to delete it?":
              "Do you want to proceed deleting this Product?" ;
      
      
      Object[] option = {"To Archive","Cancel"};
      int command = JOptionPane.showOptionDialog(null,
                message, "DELETE CONFIRMATION", 
                JOptionPane.OK_CANCEL_OPTION ,
                JOptionPane.WARNING_MESSAGE,
                null,
                option,
                option[1]
      ); if(command != 0) return;
      
      
      int rowsAffected = productDAO.setProductStatus((int)selected_record[0], false);
              
      
      message = "Product successfully Archived.";
      if (rowsAffected > 0) {
        JOptionPane.showMessageDialog(null,
              message, "Success", JOptionPane.INFORMATION_MESSAGE);

        refreshTable();
      } else {
        JOptionPane.showMessageDialog(null,
              "No Product deleted.",
              "Failed", JOptionPane.WARNING_MESSAGE);
      }
      
    }catch(Exception ex){
      ex.printStackTrace();
      JOptionPane.showMessageDialog(null,
              "No Product deleted.",
              "Failed", JOptionPane.WARNING_MESSAGE);
    }
  }
}
