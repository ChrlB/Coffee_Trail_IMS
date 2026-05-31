/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.github.chrlb.coffe_trail_ims;

import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.UI.Windows.Login;

/**
 *
 * @author user
 */
public class Coffe_Trail_IMS {

  public static void main(String[] args) {
    try{

      DBConnection.getInstance();
      new Login();

    }catch(Exception ex){
      ex.printStackTrace();
    }
    //new Dashboard();
  }
}
