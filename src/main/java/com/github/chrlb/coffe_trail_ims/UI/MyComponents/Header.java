/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.MyComponents;

import java.awt.Color;
import javax.swing.JPanel;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;

/**
 *
 * @author user
 */
public class Header extends JPanel{
  public IconBuilder logo_icon;
  
  public Header(){
    logo_icon = new IconBuilder(IconsHandler.ICON_LOGO,30,5,340,90);
    
    this.setBounds(0,0,1270,100);
    this.setBackground(new Color(0x10232A));
    this.setLayout(null);
    this.add(logo_icon);
  }
  
}
