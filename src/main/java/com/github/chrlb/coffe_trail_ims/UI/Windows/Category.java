/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Windows;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.Services.UserSession;
import com.github.chrlb.coffe_trail_ims.UI.Forms.NewCategory;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.ButtonBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.ComboBoxBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.Header;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.LabelBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.TableBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.TextFieldBuilder;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;

/**
 *
 * @author juanv
 */
public class Category extends JFrame{
    int user_ID;
    Connection conn;
    Header header;
    JPanel category_form_panel;
    
    TextFieldBuilder category_name_field,
                     description_field,
                     unit_field;
    
    LabelBuilder  category_name_field_label,
                  description_field_label,
                  unit_field_label;
    
    ButtonBuilder new_btn, 
                  delete_btn,
                  update_btn,
                  readd_category_btn;
    
    TableBuilder category_tbl;
    JScrollPane category_tbl_scrollpane;
    
    ResultSet rs;
    String sql;
    PreparedStatement pstmt;
    
    ComboBoxBuilder category_combobox;
    
    Object[] selected_record;
    
    public Category(){
      try{
        this.conn = DBConnection.getInstance().getDBConnection();
        this.user_ID = UserSession.getInstance().getUserID();
        header = new Header();
        
        
        
        category_name_field_label = new LabelBuilder("Category Name: ",30,50,150,50,15);
        description_field_label= new LabelBuilder("Discription: ",30,130,150,50,15);
        unit_field_label= new LabelBuilder("Unit Field: ",30,210,150,50,15);
        
        category_name_field = new TextFieldBuilder(true, 180, 50, 270, 50, 15);
        description_field = new TextFieldBuilder(true, 180, 130, 270, 50, 15);
        unit_field = new TextFieldBuilder(true, 180, 210, 270, 50, 15);
        
        readd_category_btn = new ButtonBuilder("RE-ADD CATEGORY", 650, 115, 175, 30,14);
        readd_category_btn.setEnabled(false);
        
        category_combobox = new ComboBoxBuilder("Active",500, 115, 125, 30,14);
        category_combobox.addItem("Archived");
        
        new_btn = new ButtonBuilder("NEW CATEGORY",1050, 30, 200, 50,15);
        update_btn = new ButtonBuilder("UPDATE",250, 320, 200, 50,15);
        delete_btn = new ButtonBuilder("DELETE",30, 320, 200, 50,15);
        
        new_btn.addActionListener((a) -> { new NewCategory(this);this.setEnabled(false);} );
        update_btn.addActionListener((a) -> { updateCategory();} );
        delete_btn.addActionListener((a) -> { deleteCategory();} );
        
        readd_category_btn.addActionListener((a) -> { readdCategory();} );
        category_combobox.addActionListener((a) -> { refreshTable();} );
                
        header.add(new_btn);
        
        category_form_panel = new JPanel();
        category_form_panel.setLayout(null);
        category_form_panel.setBounds(0,100, 480, 450);
        category_form_panel.setBackground(new Color(0XB58863));
        
        
        category_form_panel.add(category_name_field);
        category_form_panel.add(description_field);
        category_form_panel.add(unit_field);
        
        category_form_panel.add(category_name_field_label);
        category_form_panel.add(description_field_label);
        category_form_panel.add(unit_field_label);
       
        category_form_panel.add(delete_btn);
        category_form_panel.add(update_btn);
        
        
        sql = """
            SELECT 
                categoryName,
                description,
                unit
            FROM tbl_categories
            WHERE isActive = 1;
             """;
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        
        category_tbl = new TableBuilder(rs);
        category_tbl.addMouseListener(new MouseAdapter() {
         @Override
          public void mouseReleased(MouseEvent e) {  
              showSelectedRecord();
          }
        });
        
        category_tbl_scrollpane = new JScrollPane(category_tbl);
        category_tbl_scrollpane.setBounds(500,150,725,350);

        
        
        this.addWindowListener(new java.awt.event.WindowAdapter() {
          @Override
          public void windowClosing(java.awt.event.WindowEvent e) {
            new Product(); 
            dispose();
          }
        });
        
      
        ImageIcon icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_CUP));
        this.setIconImage(icon.getImage());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("PRODUCT CATEGORIES");
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(1270,580);
        this.getContentPane().setBackground(new Color(0xD3C3B9));
        this.setLocationRelativeTo(null);
        
        this.add(header);
        this.add(category_form_panel);
        this.add(category_tbl_scrollpane);
        this.add(readd_category_btn);
        this.add(category_combobox);
        
        this.setVisible(true);
        
      }catch(Exception ex){
        System.out.println(ex);
      }
    }
    
    public void refreshTable(){
      try{
        int isActive = (category_combobox.getSelectedItem().toString().equals("Active"))? 1:0;
      
        readd_category_btn.setEnabled((isActive != 1));
        delete_btn.setEnabled((isActive == 1));
        update_btn.setEnabled((isActive == 1));
        
        sql = """
          SELECT 
              categoryName,
              description,
              unit
          FROM tbl_categories
          WHERE isActive = ?;
        """;

        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, isActive);
        category_tbl.refreshTable(pstmt.executeQuery());
        
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
    
    void showSelectedRecord(){
      try{
        int row = category_tbl.getSelectedRow();

        if (row != -1) {

          selected_record = new Object[] {
            category_tbl.getValueAt(row, 0),
            category_tbl.getValueAt(row, 1), 
            category_tbl.getValueAt(row, 2)
          };
        }
        
        category_name_field.setText(""+selected_record[0]);
        description_field.setText((selected_record[1] == null)? "" : String.valueOf(selected_record[1]));
        unit_field.setText(""+selected_record[2]);
        
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
    
    void readdCategory(){
      try{
        int row = category_tbl.getSelectedRow();
        if (row == -1) {
          JOptionPane.showMessageDialog(null,
            "Please select a record first.",
            "No Selection", JOptionPane.WARNING_MESSAGE);
          return;
        } 

        selected_record = new Object[]{
          category_tbl.getValueAt(row, 0)
        };

        int command = JOptionPane.showConfirmDialog(null,
                "Do you want to proceed re-adding this Category?",
                "UPDATE CONFIRMATION", JOptionPane.OK_CANCEL_OPTION
        );
        if (!(command == JOptionPane.OK_OPTION)) return;

        sql = """
              UPDATE tbl_categories
              SET
                isActive = 1
              WHERE categoryName = ?;
              """;

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, ""+selected_record[0]);

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null,
                    "Category added successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();

        } else {
            JOptionPane.showMessageDialog(null,
                    "No record was added. The Category may not exist.",
                    "Update Failed", JOptionPane.WARNING_MESSAGE);
        }
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
  
    
    void updateCategory(){
      try{
        int row = category_tbl.getSelectedRow();
        
        if (row == -1) {
          JOptionPane.showMessageDialog(null,
            "Please select a record first.",
            "No Selection", JOptionPane.WARNING_MESSAGE);
          return;
        } 
      
        selected_record = new Object[]{
          category_tbl.getValueAt(row, 0),
          category_tbl.getValueAt(row, 1),
          category_tbl.getValueAt(row, 2)
        };

        int command = JOptionPane.showConfirmDialog(null,
                "Do you want to proceed updating this Category?",
                "UPDATE CONFIRMATION", JOptionPane.OK_CANCEL_OPTION
        );
        if (!(command == JOptionPane.OK_OPTION)) return;

        category_name_field.setText(category_name_field.getText().trim().toUpperCase());

        String new_category_name = category_name_field.getText().trim(); 
        String new_description = description_field.getText().trim(); 
        String new_unit = unit_field.getText().trim(); 

        if(new_description.isEmpty()) new_description = null;

        if (new_category_name.isEmpty() ||  new_unit.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Category Name and Unit cannot be empty.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        boolean is_category_name_not_changed = new_category_name.equals(selected_record[0]);

        if( 
            is_category_name_not_changed && 
            Objects.equals(new_description, selected_record[1]) && 
            new_unit.equals(selected_record[2]) 
          ){
          JOptionPane.showMessageDialog(null,
                    "No changes to update.",
                    "Message", JOptionPane.INFORMATION_MESSAGE);
          return;
        }
        
        if( !is_category_name_not_changed ){
          sql = """
            SELECT *
            FROM tbl_categories
            WHERE  categoryName = UPPER(?);
          """;

          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1,new_category_name);

          rs = pstmt.executeQuery();

          if(rs.next()){
            JOptionPane.showMessageDialog(null,
                    "\"" + new_category_name + "\" already exists in the category. Please check your Category list.",
                  "Category Already Exists", JOptionPane.WARNING_MESSAGE);
            return;
          }
        }
        
        sql = """
            UPDATE tbl_categories
            SET categoryName = UPPER(?),
                description = LOWER(?),
                unit = LOWER(?)
            WHERE categoryName = UPPER(?);
        """;

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,new_category_name);
        pstmt.setString(2,new_description);
        pstmt.setString(3,new_unit);
        pstmt.setString(4,selected_record[0].toString());

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null,
                    "Category successfully Updated.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(null,
                    "No Category is Updated.",
                    "Failed", JOptionPane.WARNING_MESSAGE);
        }
      
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
    
    void deleteCategory(){
      try{
        int row = category_tbl.getSelectedRow();
        
        if (row == -1) {
          JOptionPane.showMessageDialog(null,
            "Please select a record first.",
            "No Selection", JOptionPane.WARNING_MESSAGE);
          return;
        } 
      
        selected_record = new Object[]{
          category_tbl.getValueAt(row, 0),
          category_tbl.getValueAt(row, 1),
          category_tbl.getValueAt(row, 2)
        };
        
        category_name_field.setText(selected_record[0].toString());
        
        sql = """
           SELECT *
           FROM tbl_products
           WHERE categoryName = ?;
        """;
        
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,selected_record[0].toString());
        
        rs = pstmt.executeQuery();
        
        if(rs.next()){
          JOptionPane.showMessageDialog(null,
                "Cannot delete this category because it has existing products associated with it.\n"+
                        "Please remove or reassign all products (active and archived) before deleting this category.",
                "Deletion Not Allowed", 
                JOptionPane.WARNING_MESSAGE
          );
          return;
        }
        

        int command = JOptionPane.showConfirmDialog(null,
                "Do you want to proceed deleting this Category('" + selected_record[0] + "')?",
                "DELETE CONFIRMATION", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (!(command == JOptionPane.OK_OPTION)) return;
        
        
        sql = """
           UPDATE tbl_categories
              SET isActive = 0
           WHERE categoryName = ?;
        """;
        
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,selected_record[0].toString());
        
        int rowsAffected = pstmt.executeUpdate();
        
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null,
                    "Category successfully Archived.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(null,
                    "No Category is Deleted.",
                    "Failed", JOptionPane.WARNING_MESSAGE);
        }
        
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
    
    
}
