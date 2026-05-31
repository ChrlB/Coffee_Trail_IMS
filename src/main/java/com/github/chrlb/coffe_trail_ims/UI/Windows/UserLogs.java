/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Windows;

import com.github.chrlb.coffe_trail_ims.UI.Windows.User;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.Services.UserSession;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.Header;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.TableBuilder;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;

/**
 *
 * @author user
 */
public class UserLogs extends JFrame {
  int user_ID;
  Connection conn;
  Header header;
  
  ResultSet rs;
  String sql;
  PreparedStatement pstmt;

  TableBuilder userlogs_tbl;
  JScrollPane userlogs_tbl_scrollpane;
  
  UserLogs(){
    try{
      this.conn = DBConnection.getInstance().getDBConnection();
      this.user_ID = UserSession.getInstance().getUserID();
      header = new Header();

      sql = """
          SELECT 
            L.logID,
            L.userID,
            U.username,
            L.loginDate,
            L.logoutDate,
            TIME_FORMAT(
              TIMEDIFF(L.logoutDate, L.loginDate), '%H:%i:%s'
            ) AS 'Session Duration'
            
          FROM tbl_userlogs AS L

          INNER JOIN tbl_users AS U 
            ON L.userID = U.userID

          ORDER BY logID DESC;
        """;
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();

      userlogs_tbl = new TableBuilder(rs);

      userlogs_tbl_scrollpane = new JScrollPane(userlogs_tbl);
      userlogs_tbl_scrollpane.setBounds(30,160,1200,360);


      this.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent e) {
        new User();
          dispose();
        }
      });
      
      ImageIcon icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_CUP));
      this.setIconImage(icon.getImage());
      this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      this.setTitle("USER LOGS");
      this.setLayout(null);
      this.setResizable(false);
      this.setSize(1270,580);
      this.getContentPane().setBackground(new Color(0xD3C3B9));
      this.setLocationRelativeTo(null);
      
      this.add(header);
      this.add(userlogs_tbl_scrollpane);
      
      
      this.setVisible(true);
    }catch(Exception ex){
      System.out.println(ex);
    }
  }
  
  
}
