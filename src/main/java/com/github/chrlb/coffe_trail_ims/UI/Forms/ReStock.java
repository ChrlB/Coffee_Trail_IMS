/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Forms;

import java.awt.Color;
import javax.swing.*;
import java.sql.*;
import com.github.chrlb.coffe_trail_ims.DAO.ProductDAO;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.ButtonBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.LabelBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.SpinnerBuilder;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;
import com.github.chrlb.coffe_trail_ims.UI.Windows.Product;

/**
 *
 * @author user
 */
public class ReStock extends JFrame{
  int productID;
  Connection conn;
  Product parent;
  
  ProductDAO productDAO;
  
  ButtonBuilder confirm_btn, 
                cancel_btn;
  
  SpinnerBuilder restock_spinner;
  LabelBuilder stock_label;
  
  
  public ReStock(Product parent, int productID){
    this.productID = productID;
    this.conn = DBConnection.getInstance().getDBConnection();
    this.parent = parent;
    
    productDAO = new ProductDAO();
    
    stock_label = new LabelBuilder("Add Stock: ",30,50,100,50,15);
    stock_label.setForeground(new Color(0XB58863));
    
    restock_spinner = new SpinnerBuilder(true,0,1000);
    restock_spinner.setBounds(130,50,320,50);
    
    confirm_btn = new ButtonBuilder("CONFIRM",30, 130, 200, 50,15);
    cancel_btn = new ButtonBuilder("CANCEL",250, 130, 200, 50,15);
    
    cancel_btn.addActionListener((a) -> {closeWindow();});
    confirm_btn.addActionListener((a) -> { addStock();});
    
    ImageIcon icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_CUP));
      this.setIconImage(icon.getImage());
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLayout(null);
    this.setTitle("RESTOCK");
    this.setSize(500,270);
    this.getContentPane().setBackground(new Color(0x293A3E));
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent e) {
        closeWindow();
      }
    });
    this.add(stock_label);
    this.add(restock_spinner);
    this.add(confirm_btn);
    this.add(cancel_btn);
    
    this.setVisible(true);
  }
  
  void closeWindow(){
    parent.setEnabled(true);
    this.dispose();
  }
  
  void addStock(){
    try{
      int stock_to_add = ((Number) restock_spinner.getValue()).intValue();
      System.out.println(restock_spinner.getValue());
      
      if(stock_to_add < 1){
        JOptionPane.showMessageDialog(null,
                "Stock to Add cannot be less than 1.",
                "INVALID INPUT", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      
      
      int rowsAffected = productDAO.restockProductStock(productID, stock_to_add);
              
      if (rowsAffected > 0) {
          JOptionPane.showMessageDialog(null,
                  "Stock successfully added.",
                  "Success", JOptionPane.INFORMATION_MESSAGE);
          parent.refreshTable();
          closeWindow();
      } else {
          JOptionPane.showMessageDialog(null,
                  "No Stock is added.",
                  "Failed", JOptionPane.WARNING_MESSAGE);
          closeWindow();
      }
      
    }catch(Exception ex){
      ex.printStackTrace();
    }
    
    
  }
}
