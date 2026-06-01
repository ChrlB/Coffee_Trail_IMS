/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Forms;

import com.github.chrlb.coffe_trail_ims.DAO.CategoryDAO;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.UI.Windows.Category;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.ButtonBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.LabelBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.TextFieldBuilder;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;

/**
 *
 * @author user
 */
public class NewCategory extends JFrame{
  Category parent;
  Connection conn;
  
  CategoryDAO categoryDAO;
  
  TextFieldBuilder category_name_field,
                     description_field,
                     unit_field;

  LabelBuilder  category_name_field_label,
                description_field_label,
                unit_field_label;
  
  ButtonBuilder confirm_btn, 
                cancel_btn;
  
  String sql;
  ResultSet rs;
  PreparedStatement pstmt;
  
  public NewCategory(Category parent){
    this.parent = parent;
    this.conn = DBConnection.getInstance().getDBConnection();
    
    categoryDAO = new CategoryDAO();
    
    category_name_field_label = new LabelBuilder("Category Name: ",30,50,150,50,15);
    description_field_label= new LabelBuilder("Discription: ",30,130,150,50,15);
    unit_field_label= new LabelBuilder("Unit ex:(pcs, cup): ",30,210,150,50,15);
    
    category_name_field_label.setForeground(new Color(0XB58863));
    description_field_label.setForeground(new Color(0XB58863));
    unit_field_label.setForeground(new Color(0XB58863));

    category_name_field = new TextFieldBuilder(true, 180, 50, 270, 50, 15);
    description_field = new TextFieldBuilder(true, 180, 130, 270, 50, 15);
    unit_field = new TextFieldBuilder(true, 180, 210, 270, 50, 15);
    
    confirm_btn = new ButtonBuilder("CONFIRM",30, 290, 200, 50,15);
    cancel_btn = new ButtonBuilder("CANCEL",250, 290, 200, 50,15);

    cancel_btn.addActionListener((a) -> {closeWindow();});
    confirm_btn.addActionListener((a) -> {addCategory();});
    
    ImageIcon icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_CUP));
    this.setIconImage(icon.getImage());
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.setLayout(null);
    this.setTitle("NEW CATEGORY FORM");
    this.setSize(490,470);
    this.getContentPane().setBackground(new Color(0x293A3E));
    this.setLocationRelativeTo(null);
    this.setResizable(false);

    this.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent e) {
        closeWindow();
      }
    });
    
    this.add(category_name_field);
    this.add(description_field);
    this.add(unit_field);

    this.add(category_name_field_label);
    this.add(description_field_label);
    this.add(unit_field_label);
    
    this.add(confirm_btn);
    this.add(cancel_btn);
    
    this.setVisible(true);
  }
  
  void closeWindow(){
    parent.setEnabled(true);
    this.dispose();
  }
  
  void addCategory(){
    try{
      String new_category_name = category_name_field.getText().trim();
      String new_description = description_field.getText().trim();
      String new_unit = unit_field.getText().trim();

      if (new_category_name.isEmpty() ||  new_unit.isEmpty()){
           JOptionPane.showMessageDialog(null,
              "Category Name Unit and LowStockThreshold cannot be empty or 0.",
              "Validation Error", JOptionPane.WARNING_MESSAGE);
       return;
      }

//        sql = """
//            SELECT *
//            FROM tbl_categories
//            WHERE   categoryName = UPPER(?);       
//          """;
//
//        pstmt = conn.prepareStatement(sql);
//        pstmt.setString(1,new_category_name);
//        
//        rs = pstmt.executeQuery();

      boolean isCategoryAlreadyExists = categoryDAO.isCategoryAlreadyExists(new_category_name);

      if(isCategoryAlreadyExists){
          JOptionPane.showMessageDialog(null,
                  "\"" + new_category_name + "\" already exists in the category.\n"
                  + "Please check your category table.",
                "Category Already Exists", JOptionPane.WARNING_MESSAGE);
        return;
      }

      int command = JOptionPane.showConfirmDialog(null,
            "Do you want to proceed Adding this category?",
            "NEW CATEGORY CONFIRMATION", JOptionPane.OK_CANCEL_OPTION
      ); if (!(command == JOptionPane.OK_OPTION)) return;

//      sql = """
//        INSERT INTO tbl_categories(
//            categoryName,
//            description,
//            unit
//          )
//        VALUES(UPPER(?),?,?);
//      """;
//
//      pstmt = conn.prepareStatement(sql);
//      pstmt.setString(1,new_category_name);
//      pstmt.setString(2,new_description);
//      pstmt.setString(3,new_unit);

      int rowsAffected = categoryDAO.addNewCategory(new_category_name, new_description, new_unit);

      if (rowsAffected > 0) {
          JOptionPane.showMessageDialog(null,
                  "Category successfully added.",
                  "Success", JOptionPane.INFORMATION_MESSAGE);
          parent.refreshTable();
          closeWindow();
      } else {
          JOptionPane.showMessageDialog(null,
                  "No Category is added.",
                  "Failed", JOptionPane.WARNING_MESSAGE);
          closeWindow();
      }
    }catch(Exception ex){
        ex.printStackTrace();
    }
    
  }
  
  
}
