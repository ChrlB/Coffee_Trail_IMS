/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Windows;

/**
 *
 * @author user
 */
import com.github.chrlb.coffe_trail_ims.UI.Windows.Dashboard;
import com.github.chrlb.coffe_trail_ims.DAO.UserDAO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JFrame;
import java.sql.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.Services.UserSession;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.*;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;
import org.mindrot.jbcrypt.BCrypt;

public class Login extends JFrame {
  int user_ID;
  UserDAO userDAO;
  Connection conn;
  ResultSet rs ;
  
  ButtonBuilder login_btn;
  JButton show_password_btn;
  
  TextFieldBuilder username_field;
  JPasswordField password_field;
  
  IconBuilder barista_icon,
          hidden_icon,
          logo_icon;
  
  JPanel user_panel;
  LabelBuilder username_label,
               password_label;
  
  ImageIcon scaled_hidden_icon,
            scaled_show_icon;
  
  boolean isPasswordHidden = true;
  
  public Login(){
    
    try{
      
      this.conn = DBConnection.getInstance().getDBConnection();
      userDAO = new UserDAO();
      
      user_panel = new JPanel();
      user_panel.setBackground(new Color(0XB58863));
      user_panel.setLayout(null);
      user_panel.setBounds(220, 110, 830, 300);
      
      
      barista_icon = new IconBuilder(IconsHandler.ICON_USER,-10,40,400,400);
      logo_icon = new IconBuilder(IconsHandler.ICON_LOGO,280,5,480,170);
      
      ImageIcon hidden_icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_HIDDEN));
      ImageIcon show_icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_SHOW));
      
      scaled_hidden_icon = new ImageIcon(hidden_icon.getImage().getScaledInstance(20, 25, Image.SCALE_SMOOTH));
      scaled_show_icon = new ImageIcon(show_icon.getImage().getScaledInstance(20, 25, Image.SCALE_SMOOTH));
       

      show_password_btn = new JButton(null,scaled_hidden_icon);
      show_password_btn.setBounds(498,140,32,50);
      show_password_btn.setFocusable(false);
      show_password_btn.addActionListener((a) -> showHidePassword());
      
      username_field = new TextFieldBuilder (true ,245, 70 ,285,50,15);
      password_field = new JPasswordField (15);
      password_field.setEchoChar('*');
      password_field.setBounds(245, 140, 250, 50);
      password_field.setFont(new Font("Arial", Font.BOLD, 16));
      password_field.setForeground(new Color(0x10232A));
      
      login_btn = new ButtonBuilder("LOGIN",140,210,390,60,17);
      login_btn.addActionListener((a)->  login() );
      
      username_label = new LabelBuilder("Username: ",140,70,200,50,17);
      password_label = new LabelBuilder("Password: ",140,140,200,50,17);
      
      user_panel.add(password_field);
      user_panel.add (username_field);
      
      user_panel.add(login_btn);
      user_panel.add(show_password_btn);
      
      user_panel.add(username_label);
      user_panel.add(password_label);
      
      ImageIcon icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_CUP));
      this.setIconImage(icon.getImage());
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setLayout(null);
      this.setSize(815,520);
      this.setResizable(false);
      this.setTitle("LOGIN");
      this.getContentPane().setBackground(new Color(0x10232A));
      this.setLocationRelativeTo(null);
      
      this.add(logo_icon);
      this.add(barista_icon);
      this.add(user_panel);
      
      this.setVisible(true);
      
      
    }catch(Exception ex){
      System.out.print(ex.getCause());
    }
  }
  
  void showHidePassword(){
    if(isPasswordHidden){
      show_password_btn.setIcon(scaled_show_icon);
      isPasswordHidden = false;
      password_field.setEchoChar((char)0);
    }else{
      show_password_btn.setIcon(scaled_hidden_icon);
      isPasswordHidden = true;
      password_field.setEchoChar('*');
    }
  }
  
  void login(){
    int user_ID;
    
    try{
      String input_username = username_field.getText();
      String input_password = new String(password_field.getPassword());
      
      rs = userDAO.getUserInfoByUsername(input_username);
      
      // if their`s a record 
      if(rs.next()){
        String hashed_password = rs.getString("password");
        
        // hash the inputed password then compare to 
        // hashed password of that user istored in database 
        if (BCrypt.checkpw(input_password, hashed_password)) {
          user_ID = rs.getInt("userID");
          
          //userDAO.recordUserLoginLog(user_ID);
          UserSession.getInstance().createSession(rs);
          
          new Dashboard();
          this.dispose();
          
        }else { 
          JOptionPane.showMessageDialog(null, "Your Password Is Wrong", "LOGIN FAILED", JOptionPane.INFORMATION_MESSAGE);
          //System.out.println("Invalid Password."); 
        }
        
      }else{ 
        JOptionPane.showMessageDialog(null, "Wrong Credentials", "LOGIN FAILED", JOptionPane.INFORMATION_MESSAGE);
        //System.out.println("wrong credentials"); 
      }
      
    }catch (Exception ex){
      ex.printStackTrace();
    }
  }
  {
}
  
}
