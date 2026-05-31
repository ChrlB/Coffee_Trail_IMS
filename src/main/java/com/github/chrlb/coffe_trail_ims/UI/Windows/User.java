/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Windows;

import com.github.chrlb.coffe_trail_ims.UI.Windows.Dashboard;
import com.github.chrlb.coffe_trail_ims.UI.Forms.NewUser;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.sql.*;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.Services.UserSession;
import com.github.chrlb.coffe_trail_ims.UI.Forms.ChangePassword;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.*;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;


/**
 *
 * @author user
 */
public class User extends JFrame{
    int user_ID;
    Connection conn;
    Header header;
    
    ButtonBuilder new_btn, 
                  delete_btn,
                  change_password_btn,
                  update_btn,
                  user_logs_btn,
                  readd_user_btn;
    
    JPanel user_form_panel;
    TextFieldBuilder user_id_field,
                     username_field,
                     full_name_field;
    ResultSet rs;
    String sql;
    PreparedStatement pstmt;
    
    TableBuilder users_tbl;
    JScrollPane users_tbl_scrollpane;
    
    LabelBuilder  user_id_field_label,
                  username_field_label,
                  full_name_field_label;
    
    ComboBoxBuilder user_combobox;
    
    Object[] selected_record;
    
    User(){
      try{
        this.conn = DBConnection.getInstance().getDBConnection();
        this.user_ID = UserSession.getInstance().getUserID();
        header = new Header();
        
        readd_user_btn = new ButtonBuilder("RE-ADD USER", 650, 115, 175, 30,14);
        readd_user_btn.setEnabled(false);
        
        user_combobox = new ComboBoxBuilder("Active",500, 115, 125, 30,14);
        user_combobox.addItem("Archived");
        
        user_id_field_label = new LabelBuilder("User ID: ",30,50,100,50,15);
        username_field_label= new LabelBuilder("Username: ",30,130,100,50,15);
        full_name_field_label= new LabelBuilder("Fullname: ",30,210,100,50,15);
        
        
        user_id_field = new TextFieldBuilder(false, 130, 50, 320, 50, 15);
        username_field = new TextFieldBuilder(true, 130, 130, 320, 50, 15);
        full_name_field = new TextFieldBuilder(true, 130, 210, 320, 50, 15);
        
        user_logs_btn = new ButtonBuilder("USER LOGS",1050, 30, 200, 50,15);
        
        new_btn = new ButtonBuilder("NEW",30, 290, 200, 50,15);
        update_btn = new ButtonBuilder("UPDATE",250, 290, 200, 50,15);
        change_password_btn = new ButtonBuilder("CHANGE PASSWORD",30, 370, 200, 50,15);
        delete_btn = new ButtonBuilder("DELETE",250, 370, 200, 50,15);
        
        new_btn.addActionListener((a) -> {
          this.setEnabled(false);
          new NewUser(this);
        });
        delete_btn.addActionListener((a) -> { deleteRecord();});
        change_password_btn.addActionListener((a) -> {  changePassword();});
        update_btn.addActionListener((a) -> {     updateRecord();});
        user_logs_btn.addActionListener((a) -> {  new UserLogs(); dispose();});
        
        readd_user_btn.addActionListener((a) -> {readdUser();});
        user_combobox.addActionListener((a) -> {refreshTable();} );
        
        header.add(user_logs_btn);
        
        user_form_panel = new JPanel();
        user_form_panel.setLayout(null);
        user_form_panel.setBounds(0,100, 480, 450);
        user_form_panel.setBackground(new Color(0XB58863));


        user_form_panel.add(new_btn);
        user_form_panel.add(delete_btn);
        user_form_panel.add(change_password_btn);
        user_form_panel.add(update_btn);
        
        user_form_panel.add(user_id_field_label);
        user_form_panel.add(username_field_label);
        user_form_panel.add(full_name_field_label);
        
        user_form_panel.add(user_id_field);
        user_form_panel.add(username_field);
        user_form_panel.add(full_name_field);



        sql = """
              SELECT 
                userID,
                username,
                password,
                fullname,
                DATE_FORMAT(dateCreated,"%Y-%d-%m") as dateCreated
              FROM tbl_users
              WHERE isActive = 1;
              """;
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        
        users_tbl = new TableBuilder(rs);
        users_tbl.addMouseListener(new MouseAdapter() {
         @Override
          public void mouseReleased(MouseEvent e) {  
              showSelectedRecord();
          }
        });
        
        users_tbl_scrollpane = new JScrollPane(users_tbl);
        users_tbl_scrollpane.setBounds(500,150,725,350);



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
        this.setTitle("USER");
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(1270,580);
        this.getContentPane().setBackground(new Color(0xD3C3B9));
        this.setLocationRelativeTo(null);
        
        this.add(header);
        this.add(user_form_panel);
        this.add(users_tbl_scrollpane);
        
        this.add(readd_user_btn);
        this.add(user_combobox);
        
        this.setVisible(true);
        
      }catch(Exception ex){
        System.out.println(ex);
      }
    }
    
    void showSelectedRecord(){
      try{
        int row = users_tbl.getSelectedRow();

        if (row != -1) {

          selected_record = new Object[] {
            users_tbl.getValueAt(row, 0),
            users_tbl.getValueAt(row, 1), 
            users_tbl.getValueAt(row, 3)  
          };
        }
        user_id_field.setText(""+selected_record[0]);
        username_field.setText(""+selected_record[1]);
        full_name_field.setText(""+selected_record[2]);
      }catch(Exception ex){
        System.out.print(ex.getCause());
      }
    };
    
    void readdUser(){
      try{
        int row = users_tbl.getSelectedRow();
        if (row == -1) {
          JOptionPane.showMessageDialog(null,
            "Please select a record first.",
            "No Selection", JOptionPane.WARNING_MESSAGE);
          return;
        } 

        selected_record = new Object[]{
          users_tbl.getValueAt(row, 0)
        };

        int command = JOptionPane.showConfirmDialog(null,
                "Do you want to proceed re-adding this user?",
                "UPDATE CONFIRMATION", JOptionPane.OK_CANCEL_OPTION
        );
        if (!(command == JOptionPane.OK_OPTION)) return;

        sql = """
              UPDATE tbl_users
              SET
                isActive = 1
              WHERE userID = ?;
              """;

        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, (int)selected_record[0]);

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null,
                    "User added successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();

        } else {
            JOptionPane.showMessageDialog(null,
                    "No record was added. The User may not exist.",
                    "Update Failed", JOptionPane.WARNING_MESSAGE);
        }
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
  
    
    void updateRecord(){
      int CANCEL = 2;
      try {
        int row = users_tbl.getSelectedRow();
        if (row == -1) {
          JOptionPane.showMessageDialog(null,
            "Please select a record first.",
            "No Selection", JOptionPane.WARNING_MESSAGE);
          return;
        } 
        
        selected_record = new Object[]{
          users_tbl.getValueAt(row, 0),
          users_tbl.getValueAt(row, 1),
          users_tbl.getValueAt(row, 2)
        };
          
        int command = JOptionPane.showConfirmDialog(null,
                "Do you want to proceed updating this record?",
                "UPDATE CONFIRMATION", JOptionPane.OK_CANCEL_OPTION
        );
        if (command == CANCEL) return;

        String user_id = user_id_field.getText().trim();
        String new_username = username_field.getText().trim();
        String new_fullname = full_name_field.getText().trim();


        if (new_username.isEmpty() || new_fullname.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Username and Full Name cannot be empty.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if( 
            new_username.equals(selected_record[1]) &&
            new_fullname.equals(selected_record[2]) 
          ){
          JOptionPane.showMessageDialog(null,
                    "No changes to update.",
                    "Message", JOptionPane.INFORMATION_MESSAGE);
          return;
        }else  if(!(isUsernameAvailable(new_username)) ) return;
        
        sql = """
              UPDATE tbl_users
              SET username = ?,
                  fullname = ?
              WHERE userID = ?
              """;

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, new_username);
        pstmt.setString(2, new_fullname);
        pstmt.setInt(3, (int) selected_record[0]);

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null,
                    "Record updated successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
            
        } else {
            JOptionPane.showMessageDialog(null,
                    "No record was updated. The user may not exist.",
                    "Update Failed", JOptionPane.WARNING_MESSAGE);
        }
      }catch(Exception ex){
        JOptionPane.showMessageDialog(null,
            "Error updating record: " + ex.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
      }
    }
    
    void deleteRecord(){
      int CANCEL = 2;
      try{
        int row = users_tbl.getSelectedRow();
        if (row == -1) {
          JOptionPane.showMessageDialog(null,
            "Please select a record first.",
            "No Selection", JOptionPane.WARNING_MESSAGE);
          return;
        } 
        
        selected_record = new Object[]{
          users_tbl.getValueAt(row, 0)
        };
        
        if( ((int)selected_record[0]) == 1){
          JOptionPane.showMessageDialog(null,
                    "Cannot delete admin user.",
                    "Failed", JOptionPane.WARNING_MESSAGE);
          return ;
        }
        
        int command = JOptionPane.showConfirmDialog(null,
                "Do you want to proceed deleting this record?",
                "DELETE CONFIRMATION", JOptionPane.OK_CANCEL_OPTION 
        );
        if (command == CANCEL) return;
        
        sql = """
              UPDATE tbl_users
                SET isActive = 0
              WHERE userID = ?;
              """;
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, (int) selected_record[0]);
        
        int rowsAffected = pstmt.executeUpdate();
        
        if (rowsAffected > 0) {
          JOptionPane.showMessageDialog(null,
                ("User "+ selected_record[0] +" successfully archived."),
                "Success", JOptionPane.INFORMATION_MESSAGE);
          
          refreshTable();
        } else {
          JOptionPane.showMessageDialog(null,
                "No User is deleted.",
                "Failed", JOptionPane.WARNING_MESSAGE);
        }
        
        
      }catch(Exception ex){
        ex.printStackTrace();
      }
    
    }
    
    void changePassword(){
      try{
        int row = users_tbl.getSelectedRow();
        if (row == -1) {
          JOptionPane.showMessageDialog(null,
            "Please select a record first.",
            "No Selection", JOptionPane.WARNING_MESSAGE);
          return;
        } 
        
        selected_record = new Object[]{
          users_tbl.getValueAt(row, 0)
        };
        
        new ChangePassword(this);
        this.setEnabled(false);
        
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
    
    public boolean isUsernameAvailable(String new_username){
      try{
        sql = """
              SELECT * FROM tbl_users 
              WHERE username = ?
              """;
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,new_username);
        rs = pstmt.executeQuery();

        if(rs.next()){
          JOptionPane.showMessageDialog(null, 
                  "username is already been used",
                  "Warning",JOptionPane.WARNING_MESSAGE);
          return false;
        }
        return true;
      }catch(Exception ex){
        return false;
      }
    }
    
    public void refreshTable(){
      try{
        int isActive = (user_combobox.getSelectedItem().toString().equals("Active"))? 1:0;
      
        readd_user_btn.setEnabled((isActive != 1));
        delete_btn.setEnabled((isActive == 1));
        update_btn.setEnabled((isActive == 1));
        change_password_btn.setEnabled((isActive == 1));
                
        sql = """
              SELECT 
                userID,
                username,
                password,
                fullname,
                DATE_FORMAT(dateCreated,"%Y-%d-%m") as dateCreated
              FROM tbl_users
              WHERE isActive = ?;
              """;
        
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, isActive);
        users_tbl.refreshTable(pstmt.executeQuery());
        
      }catch(Exception ex){
        
      }
    }

}
