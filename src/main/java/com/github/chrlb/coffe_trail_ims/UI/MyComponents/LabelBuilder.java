/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.MyComponents;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;

/**
 *
 * @author user
 */
public class LabelBuilder extends JLabel{
  public LabelBuilder(String label,int x, int y, int width, int height, int font_size){
    this.setText(label);
    this.setBounds(x,y,width,height);
    this.setForeground(new Color(0x3D4D55));
    this.setFont(new Font("Arial", Font.BOLD, font_size));
  }
}
