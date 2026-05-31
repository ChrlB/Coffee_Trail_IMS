/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.MyComponents;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author user
 */
public class SpinnerBuilder extends JSpinner {
  int min = 0;
  int max = 1;
  boolean isEditable = false;
  public SpinnerBuilder() {
    this.min = 1;
    setModel(new SpinnerNumberModel(min, min, max, 1));
    applyStyle(isEditable);
  }
  
  public SpinnerBuilder(int min) {
    setModel(new SpinnerNumberModel(min, min, max, 1));
    this.min = min;
    applyStyle(isEditable);
  }
  
  public SpinnerBuilder(boolean isEditable,int min, int max) {
    this.min = min;
    this.max = max;
    this.isEditable = isEditable;
    setModel(new SpinnerNumberModel(min, min, max, 1));
    applyStyle(isEditable);
  }

  public void setMax(int max) {
    this.max = max;
    setModel(new SpinnerNumberModel(min, min, max, 1));
    applyStyle(isEditable);
  }

  private void applyStyle(boolean isEditable) {
    JComponent editor = this.getEditor();
    if (editor instanceof JSpinner.DefaultEditor) {
        JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
        textField.setEditable(isEditable);

        if (isEditable)  textField.addKeyListener(new KeyAdapter() {
          @Override
          public void keyTyped(KeyEvent e) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c)) {
              e.consume(); // block non-numeric input
            }
          }

          @Override
          public void keyReleased(KeyEvent e) {
            try {
              commitEdit(); 
            } catch (ParseException ex) {
              // invalid input, ignore
            }
          }
        });
        
      }

      this.setFont(new Font("Arial", Font.BOLD, 17));
      this.setForeground(new Color(0x3D4D55));
    }
//    JComponent editor = this.getEditor();
//    if (editor instanceof JSpinner.DefaultEditor) {
//      ((JSpinner.DefaultEditor) editor)
//          .getTextField()
//          .setEditable(isEditable);
//    }
//    this.setFont(new Font("Arial", Font.BOLD, 14));
//    this.setForeground(new Color(0x3D4D55));
 // }
}
