/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.MyComponents;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author user
 */
//public class IconBuilder extends JLabel {
//  public IconBuilder(String path, int x, int y) {
//    super(new ImageIcon(IconBuilder.class.getResource(path)));
//    //setBounds(11,11,11,11);
//    setLocation(x,y);
//    setVisible(true);
//  }
//}
public class IconBuilder extends JLabel {
  
  public IconBuilder(String path, int x, int y,int width, int height) {

    URL url = IconBuilder.class.getResource(path);
    if (url == null) {
      System.err.println("Image not found: " + path);

    } else {
      ImageIcon icon = new ImageIcon(url);
      Image scaledImg = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
      setIcon(new ImageIcon(scaledImg));
      setBounds(x, y, width, height);
    }
  }
}
