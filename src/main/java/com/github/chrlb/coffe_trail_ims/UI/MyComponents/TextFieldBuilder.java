/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.MyComponents;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;

/**
 *
 * @author user
 */
public class TextFieldBuilder extends JTextField{
  public TextFieldBuilder(boolean isEditable,int x, int y,int width,int height,int fontSize){
    this.setBounds(x,y,width,height);
    this.setEditable(isEditable);
    this.setFont(new Font("Arial", Font.BOLD, 16));
    this.setForeground(new Color(0x10232A));
  }
}
