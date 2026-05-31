/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.MyComponents;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JComboBox;

/**
 *
 * @author user
 */
public class ComboBoxBuilder extends JComboBox{
  public ComboBoxBuilder(Object firstItem,  int x,int y, int width,int height, int fontSize){
    this.addItem(firstItem);
    this.setSelectedIndex(0);
    this.setBounds(x, y, width, height);
    this.setFont(new Font("Arial", Font.BOLD, fontSize));
    this.setForeground(new Color(0x3D4D55));
  }
  
  public ComboBoxBuilder(int x,int y, int width,int height){
    this.setBounds(x, y, width, height);
    this.setFont(new Font("Arial", Font.BOLD, 15));
    this.setForeground(new Color(0x3D4D55));
  }
}
