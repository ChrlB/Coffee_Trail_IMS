/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Forms;

import com.github.chrlb.coffe_trail_ims.DAO.UserDAO;
import java.awt.Color;
import java.sql.PreparedStatement;
import javax.swing.*;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.ButtonBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.LabelBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.TextFieldBuilder;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;
import com.github.chrlb.coffe_trail_ims.UI.Windows.User;
/**
 *
 * @author user
 */
public class NewUser extends JFrame{
  User parent;
  Connection conn;
  
  UserDAO userDAO;
  
  TextFieldBuilder username_field,
                   full_name_field,
                   password_field,
                   confirm_password_field;
  
  String sql;
  PreparedStatement pstmt;
  
  LabelBuilder  username_field_label,
                full_name_field_label,
                password_field_label,
                confirm_password_field_label;
  
  ButtonBuilder confirm_btn, 
                cancel_btn;
  
  public NewUser(User parent){
    this.conn = DBConnection.getInstance().getDBConnection();
    this.parent = parent;
    
    userDAO = new UserDAO();
    
    username_field = new TextFieldBuilder(true, 130, 50, 320, 50, 15);
    full_name_field = new TextFieldBuilder(true, 130, 130, 320, 50, 15);
    password_field = new TextFieldBuilder(true, 130, 210, 320, 50, 15);
    confirm_password_field = new TextFieldBuilder(true, 130, 290, 320, 50, 15);
    
    username_field_label = new LabelBuilder("Username: ",30,50,100,50,15);
    full_name_field_label= new LabelBuilder("Full name: ",30,130,100,50,15);
    password_field_label= new LabelBuilder("Password: ",30,210,100,50,15);
    confirm_password_field_label= new LabelBuilder("Confirm: ",30,290,100,50,15);
    
    username_field_label.setForeground(new Color(0XB58863));
    full_name_field_label.setForeground(new Color(0XB58863));
    password_field_label.setForeground(new Color(0XB58863));
    confirm_password_field_label.setForeground(new Color(0XB58863));
    
    confirm_btn = new ButtonBuilder("CONFIRM",30, 370, 200, 50,15);
    cancel_btn = new ButtonBuilder("CANCEL",250, 370, 200, 50,15);
    
    cancel_btn.addActionListener((a) -> {Close();});
    confirm_btn.addActionListener((a) -> {addUser();});
    
    ImageIcon icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_CUP));
      this.setIconImage(icon.getImage());
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLayout(null);
    this.setTitle("NEW USER FORM");
    this.setSize(500,500);
    this.getContentPane().setBackground(new Color(0x293A3E));
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent e) {
        Close();
      }
    });
    
    this.add(username_field);
    this.add(full_name_field);
    this.add(password_field);
    this.add(confirm_password_field);
    
    this.add(username_field_label);
    this.add(full_name_field_label);
    this.add(password_field_label);
    this.add(confirm_password_field_label);
    
    this.add(confirm_btn);
    this.add(cancel_btn);
    
    this.setVisible(true);
  }
  
  void Close(){
    parent.setEnabled(true);
    dispose();
  }
  
  void addUser(){
    
    try{
      String  username = username_field.getText().trim();
      String  full_name = full_name_field.getText().trim();
      String  password = password_field.getText().trim();
      String  confirm_password = confirm_password_field.getText().trim();
      
      if(
          username.isEmpty() ||
          full_name.isEmpty() ||
          password.isEmpty() ||
          confirm_password.isEmpty()
        ){
        JOptionPane.showMessageDialog(null, "input needed");
        return;
        
      }else if( !(password.equals(confirm_password)) ){
        JOptionPane.showMessageDialog(null, "password confirmation not match");
        return;
      }
      
      if( !(userDAO.isUsernameAvailable(username)) ){
        JOptionPane.showMessageDialog(null, 
                "username is already been used",
                "Warning",JOptionPane.WARNING_MESSAGE);
        return;
      }
      
      String hashed_password = BCrypt.hashpw(password, BCrypt.gensalt(12));
      
      int rowsAffected = userDAO.addNewUser(username, hashed_password, full_name);

      if (rowsAffected > 0) {
          JOptionPane.showMessageDialog(null,
                  "New User is successfully added.",
                  "Success", JOptionPane.INFORMATION_MESSAGE);
          parent.refreshTable();
          Close();
      } else {
          JOptionPane.showMessageDialog(null,
                  "No User is added.",
                  "Failed", JOptionPane.WARNING_MESSAGE);
          Close();
      }
    }catch (Exception ex){
      JOptionPane.showMessageDialog(null, "input needed");
    }
  }
}
