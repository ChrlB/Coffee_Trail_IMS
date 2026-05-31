/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Forms;

import java.awt.Color;
import javax.swing.*;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.ButtonBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.LabelBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.TextFieldBuilder;
import com.github.chrlb.coffe_trail_ims.UI.Windows.User;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.Services.UserSession;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;
/**
 *
 * @author user
 */
public class ChangePassword extends JFrame{
  Connection conn;
  User user_window;
  int userID;
  
  TextFieldBuilder password_field,
                   confirm_password_field;
  
  ButtonBuilder confirm_btn, 
                cancel_btn;
  
  LabelBuilder  password_field_label,
                confirm_password_field_label;
  
  
  String sql;
  PreparedStatement pstmt;
  
  public ChangePassword(User user_window){
    this.conn = DBConnection.getInstance().getDBConnection();
    this.userID = UserSession.getInstance().getUserID();
    this.user_window = user_window;
    
    
    password_field = new TextFieldBuilder(true, 180, 50, 320, 50, 15);
    confirm_password_field = new TextFieldBuilder(true, 180, 130, 320, 50, 15);
    
    password_field_label= new LabelBuilder("New Password: ",30,50,200,50,15);
    confirm_password_field_label= new LabelBuilder("Confirm: ",30,130,200,50,15);
    
    password_field_label.setForeground(new Color(0XB58863));
    confirm_password_field_label.setForeground(new Color(0XB58863));
    
    confirm_btn = new ButtonBuilder("CONFIRM",30, 210, 225, 50,15);
    cancel_btn = new ButtonBuilder("CANCEL",275, 210, 225, 50,15);
    
    cancel_btn.addActionListener((a) -> {close();});
    confirm_btn.addActionListener((a) -> {changeUserPassword();});
    
    ImageIcon icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_CUP));
    this.setIconImage(icon.getImage());
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLayout(null);
    this.setTitle("CHANGE PASSWORD FORM");
    this.setSize(550,330);
    this.getContentPane().setBackground(new Color(0x293A3E));
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent e) {
        close();
      }
    });
    
    this.add(password_field);
    this.add(confirm_password_field);
    
    this.add(password_field_label);
    this.add(confirm_password_field_label);
    
    this.add(confirm_btn);
    this.add(cancel_btn);
    this.setVisible(true);
  }
  
  void close(){
    user_window.setEnabled(true);
    dispose();
  }
  
  void changeUserPassword(){
    try{
      
      String  password = password_field.getText().trim();
      String  confirm_password = confirm_password_field.getText().trim();
      
      if( password.isEmpty() || confirm_password.isEmpty() ){
        JOptionPane.showMessageDialog(null, "input needed");
        return;
        
      }else if( !(password.equals(confirm_password)) ){
        JOptionPane.showMessageDialog(null, "password confirmation not match");
        return;
      }
      
      String hashed_password = BCrypt.hashpw(password, BCrypt.gensalt(12));
      
      
      sql = """
            UPDATE tbl_users
            SET password = ?
            WHERE userID = ?;
            """;
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,hashed_password);
      pstmt.setInt(2,userID);
      
      int rowsAffected = pstmt.executeUpdate();
      
      if (rowsAffected > 0) {
          JOptionPane.showMessageDialog(null,
                  "User Password successfully Change.",
                  "Success", JOptionPane.INFORMATION_MESSAGE);
          user_window.refreshTable();
          close();
      } else {
          JOptionPane.showMessageDialog(null,
                  "Changing Password Unsuccessfull.",
                  "Failed", JOptionPane.WARNING_MESSAGE);
          close();
      }
    }catch(Exception ex){
      ex.printStackTrace();
    }
  }
  
}
