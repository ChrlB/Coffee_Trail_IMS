/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Forms;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.github.chrlb.coffe_trail_ims.DAO.ProductDAO;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.Services.UserSession;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.ButtonBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.LabelBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.SpinnerBuilder;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;
import com.github.chrlb.coffe_trail_ims.UI.Windows.Product;

/**
 *
 * @author user
 */
public class DeductStock extends JFrame{
  int productID;
  int max_deduct;
  Connection conn;
  ProductDAO productDAO;
  Product parent;
  
  ButtonBuilder confirm_btn, 
                cancel_btn;
  
  SpinnerBuilder deduct_spinner;
  LabelBuilder stock_label;
  
  
  public DeductStock(Product parent, int productID,int max_deduct){
    this.productID = productID;
    this.conn = DBConnection.getInstance().getDBConnection();
    this.parent = parent;
    this.max_deduct = max_deduct;
    
    productDAO = new ProductDAO();
    
    
    stock_label = new LabelBuilder("Deduct Stock: ",30,50,150,50,15);
    stock_label.setForeground(new Color(0XB58863));
    
    deduct_spinner = new SpinnerBuilder(true,0,1000 );
    deduct_spinner.setBounds(150,50,300,50);
    
    confirm_btn = new ButtonBuilder("CONFIRM",30, 130, 200, 50,15);
    cancel_btn = new ButtonBuilder("CANCEL",250, 130, 200, 50,15);
    
    cancel_btn.addActionListener((a) -> {closeWindow();});
    confirm_btn.addActionListener((a) -> { deductStock();});
    
    ImageIcon icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_CUP));
    this.setIconImage(icon.getImage());
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLayout(null);
    this.setTitle("DEDUCT STOCK");
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
    this.add(deduct_spinner);
    this.add(confirm_btn);
    this.add(cancel_btn);
    
    this.setVisible(true);
  }
  
  void closeWindow(){
    parent.setEnabled(true);
    this.dispose();
  }
  
  void deductStock(){
    try{
      int stock_to_deduct = (int)deduct_spinner.getValue();
      
      if(stock_to_deduct < 1){
        JOptionPane.showMessageDialog(null,
                "Stock to Deduct cannot be less than 1.",
                "INVALID INPUT", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      
      if(stock_to_deduct > max_deduct){
        JOptionPane.showMessageDialog(null,
                "Stock to Deduct cannot be greater than current stock",
                "INVALID INPUT", JOptionPane.INFORMATION_MESSAGE);
        return;
      }
      
      
      int rowsAffected = productDAO.deductProductStock(productID, stock_to_deduct);
      
      
      if (rowsAffected > 0) {
          JOptionPane.showMessageDialog(null,
                  "Stock successfully Deducted.",
                  "Success", JOptionPane.INFORMATION_MESSAGE);
          parent.refreshTable();
          closeWindow();
      } else {
          JOptionPane.showMessageDialog(null,
                  "No Stock is Deducted.",
                  "Failed", JOptionPane.WARNING_MESSAGE);
          closeWindow();
      }
      
    }catch(Exception ex){
      ex.printStackTrace();
    }
    
    
  }
}
