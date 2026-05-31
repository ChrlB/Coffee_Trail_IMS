/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.chrlb.coffe_trail_ims.UI.Windows;

import com.github.chrlb.coffe_trail_ims.UI.Windows.Dashboard;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.github.chrlb.coffe_trail_ims.Services.DBConnection;
import com.github.chrlb.coffe_trail_ims.Services.UserSession;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.ComboBoxBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.Header;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.LabelBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.TableBuilder;
import com.github.chrlb.coffe_trail_ims.UI.MyComponents.TextFieldBuilder;
import com.github.chrlb.coffe_trail_ims.UI.Icons.IconsHandler;

/**
 *
 * @author user
 */
public class Sales extends JFrame{
    int user_ID;
    Connection conn;
    Header header;
    
    TableBuilder sales_tbl;
    JScrollPane sales_tbl_scrollpane;
    
    ResultSet rs;
    String sql;
    PreparedStatement pstmt;
    
    TextFieldBuilder total_orders_field,
                    total_sales_field,
                    total_quantity_field;
        
    LabelBuilder  total_orders_field_label,
              total_sales_field_label,
              total_quantity_field_label,
              order_combobox_label,
              customer_combobox_label,
              user_combobox_label,
              product_category_combobox_label,
              product_combobox_label;
    
    JPanel footer_panel;
    
    ComboBoxBuilder order_combobox,
                    customer_combobox,
                    user_combobox,
                    product_category_combobox,
                    product_combobox;
    
   public Sales(){
      try{  
        this.conn = DBConnection.getInstance().getDBConnection();
        this.user_ID = UserSession.getInstance().getUserID();
        header = new Header();
        
        footer_panel = new JPanel();
        footer_panel.setBackground(new Color(0XB58863));
        footer_panel.setLayout(null);
        footer_panel.setBounds(0, 580, 1270, 120);
        
        total_orders_field = new TextFieldBuilder(false, 170, 590, 250, 40, 17);
        total_sales_field = new TextFieldBuilder(false, 560, 590, 260, 40, 17);
        total_quantity_field = new TextFieldBuilder(false, 980, 590, 250, 40, 17);
        
        total_orders_field_label = new LabelBuilder("TOTAL ORDERS: ",30,590,150,50,15);
        total_sales_field_label = new LabelBuilder("TOTAL SALES: ",430,590,150,50,15);
        total_quantity_field_label = new LabelBuilder("TOTAL QUANTITY: ",830,590,150,50,15);
        
        total_orders_field_label.setForeground(new Color(0x3D4D55));
        total_sales_field_label.setForeground(new Color(0x3D4D55));
        total_quantity_field_label.setForeground(new Color(0x3D4D55));
        
        order_combobox_label = new LabelBuilder("Order Filter:",30,102,150,30, 13);
        customer_combobox_label = new LabelBuilder("Customer Filter:",200,102,150,30, 13);
        user_combobox_label = new LabelBuilder("User Filter:",370,102,150,30, 13);
        product_category_combobox_label = new LabelBuilder("Product Category Filter:",540,102,200,30, 13);
        product_combobox_label = new LabelBuilder("Product Filter:",710,102,150,30, 13);
        
        order_combobox = new ComboBoxBuilder("ALL",30,125,150,30,13);
        customer_combobox = new ComboBoxBuilder("ALL",200,125,150,30,13);
        user_combobox = new ComboBoxBuilder("ALL",370,125,150,30,13);
        product_category_combobox = new ComboBoxBuilder("ALL",540,125,150,30,13);
        product_combobox = new ComboBoxBuilder("ALL",710,125,150,30,13);

        order_combobox.addActionListener((a) -> { refreshTable();} );
        customer_combobox.addActionListener((a) -> { refreshTable();} );
        user_combobox.addActionListener((a) -> { refreshTable();} );
        product_category_combobox.addActionListener((a) -> { refreshTable();} );
        product_combobox.addActionListener((a) -> { refreshTable();} );
        
        //  order_combobox
        sql = "SELECT orderID FROM tbl_orders ORDER BY orderID DESC; ";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        while(rs.next()){ order_combobox.addItem(rs.getString("orderID")); }
        
        
        //  customer_combobox
        sql = "SELECT  DISTINCT customerName AS customerName, orderDate FROM tbl_orders ORDER BY orderDate DESC; ";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        while(rs.next()){ customer_combobox.addItem(rs.getString("customerName")); }
        
        
        //  user_combobox
        sql = "SELECT  username FROM tbl_users; ";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        while(rs.next()){ user_combobox.addItem(rs.getString("username")); }
        
        
        //  product_category_combobox
        sql = "SELECT categoryName FROM tbl_categories;";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        while(rs.next()){ product_category_combobox.addItem(rs.getString("categoryName")); }
        
        
        // product_combobox
        sql = "SELECT  productName FROM tbl_products; ";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        while(rs.next()){ product_combobox.addItem(rs.getString("productName")); }
        
        
        sql = """
          SELECT 
              S.saleID,
              S.orderID,
              U.username      AS user,
              O.customerName,
              S.productID,
              P.productName,
              P.categoryName  AS category,
              S.salePrice,
              S.quantity,
              DATE_FORMAT(O.orderDate,"%Y-%d-%m") as orderDate
          FROM tbl_sales AS S
              
          INNER JOIN tbl_products AS P
              ON S.productID = P.productID
              
          INNER JOIN tbl_orders AS O
              ON S.orderID = O.orderID
              
          INNER JOIN tbl_users AS U
              ON O.userID = U.userID
              
          ORDER BY S.orderID DESC;
        """;
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        
        sales_tbl = new TableBuilder(rs);
        
        sales_tbl_scrollpane = new JScrollPane(sales_tbl);
        sales_tbl_scrollpane.setBounds(30,160,1200,410);
        
        sql = """
              SELECT 
                 SUM(salePrice * quantity) AS TOTAL_SALE_PRICE, 
                 SUM(quantity) AS TOTAL_QUANTITY,
                 COUNT(DISTINCT orderID) AS TOTAL_ORDERS
              FROM tbl_sales AS S
            
              """;
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        rs.next();
        total_sales_field.setText(rs.getString("TOTAL_SALE_PRICE"));
        total_quantity_field.setText(rs.getString("TOTAL_QUANTITY"));
        total_orders_field.setText(rs.getString("TOTAL_ORDERS"));
          
        this.addWindowListener(new java.awt.event.WindowAdapter() {
          @Override
          public void windowClosing(java.awt.event.WindowEvent e) {
            new Dashboard(); 
            dispose();
          }
        });
        
        ImageIcon icon = new ImageIcon(getClass().getResource(IconsHandler.ICON_CUP));
        this.setIconImage(icon.getImage());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("SALES");
        this.setLayout(null);
        this.setResizable(false);
        this.setSize(1270,700);
        this.getContentPane().setBackground(new Color(0xD3C3B9));
        this.setLocationRelativeTo(null);
        
        this.add (header);
        this.add(sales_tbl_scrollpane);
        
        this.add(order_combobox);
        this.add(customer_combobox);
        this.add(user_combobox);
        this.add(product_category_combobox);
        this.add(product_combobox);
        
        this.add(order_combobox_label);
        this.add(customer_combobox_label);
        this.add(user_combobox_label);
        this.add(product_category_combobox_label);
        this.add(product_combobox_label);
        
        this.add(total_orders_field);
        this.add(total_sales_field);
        this.add(total_quantity_field);
        
        this.add(total_orders_field_label);
        this.add(total_sales_field_label);
        this.add(total_quantity_field_label);
        
        this.add(footer_panel);
        
        
        this.setVisible(true);
        
      }catch(Exception ex){
        System.out.println(ex);
      }
    }
    
    void refreshTable(){
      try{
        String selected_order_text = order_combobox.getSelectedItem().toString();
        int selected_order = (selected_order_text.equals("ALL"))?  0 : Integer.parseInt(selected_order_text );
        String selected_customer = customer_combobox.getSelectedItem().toString();
        String selected_user = user_combobox.getSelectedItem().toString();
        String selected_category = product_category_combobox.getSelectedItem().toString();
        String selected_product = product_combobox.getSelectedItem().toString();
        
        sql = """
          SELECT 
              S.saleID,
              S.orderID,
              U.username      AS user,
              O.customerName,
              S.productID,
              P.productName,
              P.categoryName  AS category,
              S.salePrice,
              S.quantity,
              DATE_FORMAT(O.orderDate,"%Y-%d-%m") as orderDate
          FROM tbl_sales AS S
              
          INNER JOIN tbl_products AS P
              ON S.productID = P.productID
              
          INNER JOIN tbl_orders AS O
              ON S.orderID = O.orderID
              
          INNER JOIN tbl_users AS U
              ON O.userID = U.userID
              
          WHERE  
              (? = 0            OR S.orderID      = ?)
              AND (? = 'ALL'    OR U.username     = ?)
              AND (? = 'ALL'    OR O.customerName = ?)
              AND (? = 'ALL'    OR P.categoryName = ?)
              AND (? = 'ALL'    OR P.productName  = ?)
          ORDER BY S.orderID DESC;
        """;
        
        
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, selected_order);          pstmt.setInt(2, selected_order);
        pstmt.setString(3, selected_user); pstmt.setString(4, selected_user);
        pstmt.setString(5, selected_customer);     pstmt.setString(6, selected_customer);
        pstmt.setString(7, selected_category); pstmt.setString(8, selected_category);
        pstmt.setString(9, selected_product);  pstmt.setString(10, selected_product);
        
        rs = pstmt.executeQuery();
        sales_tbl.refreshTable(rs);
        
        sql = """
            SELECT 
                COUNT(DISTINCT S.orderID) AS TOTAL_ORDERS,
                SUM(S.salePrice * S.quantity) AS TOTAL_SALE_PRICE,
                SUM(S.quantity) AS TOTAL_QUANTITY
            FROM tbl_sales AS S
              
            INNER JOIN tbl_products AS P 
                ON S.productID = P.productID
                    
            INNER JOIN tbl_orders   AS O 
                ON S.orderID   = O.orderID
              
            INNER JOIN tbl_users    AS U 
                ON O.userID     = U.userID
              
            WHERE
                (? = 0         OR S.orderID      = ?)
                AND (? = 'ALL' OR U.username     = ?)
                AND (? = 'ALL' OR O.customerName = ?)
                AND (? = 'ALL' OR P.categoryName = ?)
                AND (? = 'ALL' OR P.productName  = ?)
        """;

        // same bindings
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, selected_order);        pstmt.setInt(2, selected_order);
        pstmt.setString(3, selected_user);      pstmt.setString(4, selected_user);
        pstmt.setString(5, selected_customer);  pstmt.setString(6, selected_customer);
        pstmt.setString(7, selected_category);  pstmt.setString(8, selected_category);
        pstmt.setString(9, selected_product);   pstmt.setString(10, selected_product);

        rs = pstmt.executeQuery();
        if (rs.next()) {
            total_sales_field.setText(rs.getString("TOTAL_SALE_PRICE"));
            total_quantity_field.setText(rs.getString("TOTAL_QUANTITY"));
            total_orders_field.setText(rs.getString("TOTAL_ORDERS"));
        }
        
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
   
   
  
}
