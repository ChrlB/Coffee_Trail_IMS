/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Forms;

import java.awt.Color;
import java.sql.*;
import javax.swing.*;
import com.github.chrlb.coffe_trail_ims.DAO.ProductDAO;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.Services.UserSession;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.ButtonBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.ComboBoxBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.LabelBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.SpinnerBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.TextFieldBuilder;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;
import com.github.chrlb.coffe_trail_ims.UI.Windows.Product;

/**
 *
 * @author user
 */
public class NewProduct extends JFrame {
  Product parent;
  Connection conn;
  ProductDAO productDAO;
  
  ResultSet rs;
  
  LabelBuilder   product_name_field_label, 
                 category_name_field_label,
                 unit_price_field_label,
                 stock_quantity_spinner_label,
                 lowStockThreshold_field_label,
                 lowStockThreshold2_field_label;
  
  SpinnerBuilder stock_quantity_spinner,
                 lowStockThreshold_spinner;
  
  TextFieldBuilder product_name_field, 
                   unit_price_field;
  
  ComboBoxBuilder product_category_combobox;
  
  ButtonBuilder confirm_btn, 
                cancel_btn;
  
  public NewProduct(Product parent,Connection conn){
    try{
      this.parent = parent;
      this.conn = DBConnection.getInstance().getDBConnection();
      
      productDAO = new ProductDAO();
      

      rs = productDAO.getProductCategories(true);
      product_category_combobox = new ComboBoxBuilder(175, 120, 275, 50);
      while(rs.next()){
        product_category_combobox.addItem(rs.getString("categoryName"));
      }
      
      product_name_field = new TextFieldBuilder(true, 175, 50, 275, 50, 15);
      unit_price_field = new TextFieldBuilder(true, 175, 190, 275, 50, 15);
      
      lowStockThreshold_spinner = new SpinnerBuilder(true,5,1000);
      lowStockThreshold_spinner.setBounds(175, 260, 275, 50);
      
      stock_quantity_spinner = new SpinnerBuilder(true,0,1000);
      stock_quantity_spinner.setBounds(175, 330, 275, 50);
      

      product_name_field_label = new LabelBuilder("Product Name: ",30,50,200,50,15);
      category_name_field_label = new LabelBuilder("Category Name: ",30,120,200,50,15);
      unit_price_field_label = new LabelBuilder("Unit Price: ",30,190,200,50,15);
      lowStockThreshold_field_label = new LabelBuilder("Low Stock ",30,250,200,50,15);
      lowStockThreshold2_field_label = new LabelBuilder("Threshold: ",30,270,200,50,15);
      stock_quantity_spinner_label = new LabelBuilder("Stock: ",30,330,200,50,15);
      
      product_name_field_label.setForeground(new Color(0XB58863));
      category_name_field_label.setForeground(new Color(0XB58863));
      unit_price_field_label.setForeground(new Color(0XB58863));
      stock_quantity_spinner_label.setForeground(new Color(0XB58863));
      lowStockThreshold_field_label.setForeground(new Color(0XB58863));
      lowStockThreshold2_field_label.setForeground(new Color(0XB58863));
      
      confirm_btn = new ButtonBuilder("CONFIRM",30, 400, 200, 50,15);
      cancel_btn = new ButtonBuilder("CANCEL",250, 400, 200, 50,15);

      cancel_btn.addActionListener((a) -> {closeWindow();});
      confirm_btn.addActionListener((a) -> {addProduct();});
      
      ImageIcon icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_CUP));
      this.setIconImage(icon.getImage());
      this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      this.setLayout(null);
      this.setTitle("NEW PRODUCT FORM");
      this.setSize(490,510);
      this.getContentPane().setBackground(new Color(0x293A3E));
      this.setLocationRelativeTo(null);
      this.setResizable(false);

      this.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
          closeWindow();
        }
      });
      
      this.add(product_name_field);
      this.add(product_category_combobox);
      this.add(unit_price_field);
      this.add(lowStockThreshold_spinner);
      this.add(stock_quantity_spinner);
      
      this.add (product_name_field_label); 
      this.add (category_name_field_label);
      this.add (unit_price_field_label); 
      this.add(lowStockThreshold_field_label);
      this.add(lowStockThreshold2_field_label);
      this.add (stock_quantity_spinner_label);
      
      this.add(confirm_btn);
      this.add(cancel_btn);
    
      this.setVisible(true);
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
  
   void closeWindow(){
    parent.setEnabled(true);
    dispose();
  }
   
  void addProduct(){
    try{
      String new_product_name = product_name_field.getText().trim();
      int new_stock_quantity = ((Number) stock_quantity_spinner.getValue()).intValue();
      int new_lowstock_threshold = ((Number) lowStockThreshold_spinner.getValue()).intValue();
      String new_product_category = product_category_combobox.getSelectedItem().toString().trim();
      double new_unit_price =  Double.parseDouble(
              (unit_price_field.getText().trim().isEmpty())? 
                  "0": 
                  unit_price_field.getText().trim()
      ); 
      
      if (new_product_name.isEmpty() || new_unit_price <= 0 ){
        JOptionPane.showMessageDialog(null,
                "Unit Price and Product Name cannot be empty or less than 1.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
        return;
      }
      
      
      boolean isProductAlreadyExists = productDAO.isProductAlreadyExists(new_product_name, new_product_category);
      
      
      if(isProductAlreadyExists){
        JOptionPane.showMessageDialog(null,
                "\"" + new_product_name + "\" already exists in the " + new_product_category + " category.\n"
                + "It may be active or archived. Please check your product list.",
              "Product Already Exists", JOptionPane.WARNING_MESSAGE);
        return;
      }
     
      
      int command = JOptionPane.showConfirmDialog(null,
              "Do you want to proceed Adding this product?",
              "NEW PRODUCT CONFIRMATION", JOptionPane.OK_CANCEL_OPTION
      ); if (!(command == JOptionPane.OK_OPTION)) return;
      
      
      
      int rowsAffected = productDAO.addNewProduct(
          new_product_name, 
          new_product_category, 
          new_unit_price, 
          new_stock_quantity, 
          new_lowstock_threshold
      );
      

      if (rowsAffected > 0) {
          JOptionPane.showMessageDialog(null,
                  "Product successfully added.",
                  "Success", JOptionPane.INFORMATION_MESSAGE);
          parent.refreshTable();
          closeWindow();
      } else {
          JOptionPane.showMessageDialog(null,
                  "No Product is added.",
                  "Failed", JOptionPane.WARNING_MESSAGE);
          closeWindow();
      }
      
      
      
    }catch(NumberFormatException num_ex){
      JOptionPane.showMessageDialog(null,
                  "Invalid input. Please enter a valid number for product price.",
                  "Update Failed", JOptionPane.WARNING_MESSAGE);
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
    
    
    
  
}
