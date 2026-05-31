/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.MyComponents;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
/**
 *
 * @author user
 */
public class TableBuilder extends JTable{
  public ResultSet rs;
  int selected_row;
  DefaultTableModel tbl_model;
  
  int column_count;
  
  public TableBuilder(ResultSet rs){
    try{
      this.rs = rs;
      DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

      
      this.setDefaultRenderer(Object.class, centerRenderer);
      this.setModel(buildModel(rs));
      
      this.setRowHeight(35);
      this.setFont(new Font("Arial", Font.BOLD, 14));
      this.setForeground(new Color(0x3D4D55));
      //this.setEditable(false);
      this.setDragEnabled(false);
      this.getTableHeader().setForeground(Color.WHITE);
      this.getTableHeader().setBackground(new Color(0x3D4D55));
      this.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
      this.getTableHeader().setReorderingAllowed(false);
      //setBounds(x,y,width, height);
      this.setVisible(true);
      
      
      
    }catch (Exception ex){
      System.out.print(ex.getCause());
    }
  }
  
  @Override
  public boolean isCellEditable(int row, int column) {
      return false; 
  }
  
  public void refreshTable(ResultSet new_rs){
    try{
      setModel(buildModel(new_rs));
    }catch(Exception ex){
      System.out.print(ex.getCause());
    }
  }
  
  private DefaultTableModel buildModel(ResultSet rs){
    try{
      ResultSetMetaData rs_metadata = rs.getMetaData();
      column_count = rs_metadata.getColumnCount();
      
      String[] column_names = new String[column_count];
      
      for(int column = 0; column < column_count; ){
        column_names[column] = rs_metadata.getColumnLabel(++column);
      }
      
      tbl_model = new DefaultTableModel(column_names,0);
      
      while(rs.next()){
        Object[] record = new Object[column_count];
        
        for(int column = 0; column < column_count;){
         record[column] = rs.getObject(++column);
        }
        tbl_model.addRow(record);
      }
      
      return tbl_model;
      
    }catch(Exception ex){
      System.out.print(ex.getCause());
    }
    return tbl_model;
  }
  
  
  
  public Object[] getRecord(){
    Object[] record = new Object[column_count];
    try{
      selected_row = this.getSelectedRow();
      
      if (selected_row != -1) {
        for(int column = 0; column < column_count; column++){
          //System.out.println(this.getValueAt(selected_row, column));
          record[column] = this.getValueAt(selected_row, column);
        }
      }
    }catch(Exception ex){
      System.out.print(ex.getCause());
    }
    return record;
  }
}
