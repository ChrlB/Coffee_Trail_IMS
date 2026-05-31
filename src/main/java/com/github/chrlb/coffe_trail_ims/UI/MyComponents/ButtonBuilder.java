/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.MyComponents;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

/**
 *
 * @author user
 */
public class ButtonBuilder extends JButton{
  
  public ButtonBuilder(String label, int x, int y, int width, int height,int fontsize){
    
    setBounds(x, y, width, height);
    setFocusable(false);
    setFont(new Font("Arial", Font.PLAIN, fontsize));
    setText(label);
    setForeground(Color.WHITE);
    setBackground(new Color(0x3D4D55));
    
  }
}
