/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package mu.sic;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ACER
 */
public class admin extends javax.swing.JFrame {

    Connection con;
    Statement stat;
    ResultSet rs;
    String sql;
    DefaultTableModel tableModel; // Table model for JTable
     DefaultTableModel tableModel2; // Table model 2 for JTable
    
    public void insertData() {
    try {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        String password = JOptionPane.showInputDialog(this, "Enter password:");
        String email = JOptionPane.showInputDialog(this, "Enter email:");
        String nohp = JOptionPane.showInputDialog(this, "Enter phone number:");
        int premium = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter premium status (0 or 1):"));

        String insertSql = "INSERT INTO login (username, password, email, nohp, active, premium, premiumRequest) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertStatement = con.prepareStatement(insertSql);
        insertStatement.setString(1, username);
        insertStatement.setString(2, password);
        insertStatement.setString(3, email);
        insertStatement.setString(4, nohp);
        insertStatement.setInt(5, 0);
        insertStatement.setInt(6, premium);
        insertStatement.setInt(7, 0);

        int rowsAffected = insertStatement.executeUpdate();
        System.out.println(rowsAffected + " row(s) inserted.");
        
        insertStatement.close();
        displayDataPremium();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Failed to insert data: " + e.getMessage());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage());
    }
}

     
    
    // Method to display all data from the database table
    public void displayData() {
      try {
          sql = "SELECT * FROM login";
          Statement displayStat = con.createStatement();
          rs = displayStat.executeQuery(sql);

          // Clear the table model
          tableModel.setRowCount(0);

          while (rs.next()) {
              String username = rs.getString("username");
              String password = rs.getString("password");
              String email = rs.getString("email");
              String nohp = rs.getString("nohp");
              int active = rs.getInt("active"); // Retrieve as int
              int premium = rs.getInt("premium"); // Retrieve as int
              int premiumRequest = rs.getInt("premiumRequest"); // Retrieve as int

              // Add the data to the table model
              Object[] rowData = {username, password, email, nohp, active, premium, premiumRequest};
              tableModel.addRow(rowData);
          }

          rs.close();
          displayStat.close();
      } catch (SQLException e) {
          JOptionPane.showMessageDialog(this, "Failed to fetch data: " + e.getMessage());
      }
  }
    
     // Method to display all data from the database table
    public void displayDataPremium() {
      try {
          sql = "SELECT * FROM login";
          Statement displayStat = con.createStatement();
          rs = displayStat.executeQuery(sql);

          // Clear the table model
          tableModel2.setRowCount(0);

          while (rs.next()) {
              String username = rs.getString("username");
              int premium = rs.getInt("premium"); // Retrieve as int
              int premiumRequest = rs.getInt("premiumRequest"); // Retrieve as int

              // Add the data to the table model
              Object[] rowData = {username,premium, premiumRequest};
              tableModel2.addRow(rowData);
          }

          rs.close();
          displayStat.close();
      } catch (SQLException e) {
          JOptionPane.showMessageDialog(this, "Failed to fetch data: " + e.getMessage());
      }
  }
    
    public void deleteData() {
        PreparedStatement selectStatement = null;

        try {
            // Get the right row to delete
            String userget = JOptionPane.showInputDialog(this, "Enter Username:");
            String sql = "SELECT * FROM login WHERE username = ?";
            selectStatement = con.prepareStatement(sql);
            selectStatement.setString(1, userget);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                sql = "DELETE FROM login WHERE username = ?";
                PreparedStatement deleteStatement = con.prepareStatement(sql);
                deleteStatement.setString(1, username);
                int rowsAffected = deleteStatement.executeUpdate();
                displayDataPremium();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Data deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "No matching data found.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "No matching data found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to delete data: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage());
        } finally {
            try {
                if (selectStatement != null) {
                    selectStatement.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
}
    
    public void updateData() {
        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;

        try {
            // Get the Right Row to update
            String userget = JOptionPane.showInputDialog(this, "Enter Username:");
            sql = "SELECT * FROM login WHERE username = ?";
            selectStatement = con.prepareStatement(sql);
            selectStatement.setString(1, userget);
            rs = selectStatement.executeQuery();

            if (rs.next()) {
                // Change the Data
                String username = JOptionPane.showInputDialog(this, "Enter new Username:", rs.getString("username"));
                String password = JOptionPane.showInputDialog(this, "Enter new password:", rs.getString("password"));
                String email = JOptionPane.showInputDialog(this, "Enter new email:", rs.getString("email"));
                String nohp = JOptionPane.showInputDialog(this, "Enter new phone number:", rs.getString("nohp"));
                int premium = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter new Premium status (0 or 1):", rs.getInt("premium")));
                int premiumRequest = rs.getInt("premiumRequest");

                if (premium == 1) {
                    premiumRequest = 0;
                }

                sql = "UPDATE login SET username = ?, password = ?, email = ?, nohp = ?, premium = ?, premiumRequest = ? WHERE username = ?";
                updateStatement = con.prepareStatement(sql);
                updateStatement.setString(1, username);
                updateStatement.setString(2, password);
                updateStatement.setString(3, email);
                updateStatement.setString(4, nohp);
                updateStatement.setInt(5, premium);
                updateStatement.setInt(6, premiumRequest);
                updateStatement.setString(7, userget);

                int rowsAffected = updateStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) updated.");
                displayDataPremium();
            } else {
                    JOptionPane.showMessageDialog(this, "No user found with the given username.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to update data: " + e.getMessage());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage());
            } finally {
                try {
                    if (selectStatement != null) {
                        selectStatement.close();
                    }
                    if (updateStatement != null) {
                        updateStatement.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
            }
}
     
    public admin() {
       setResizable(false); // Disable resizing of the JFrame
       setTitle("Admin Page");

       // Set the icon image for the JFrame
       ImageIcon icon = new ImageIcon(getClass().getResource("/image/IconApp.png"));
       setIconImage(icon.getImage());

       // Database setup
       initComponents();
       koneksi DB = new koneksi();
       DB.config();
       con = DB.con;
       stat = DB.stm;
       
       
        // Create the table model 1
        tableModel = new DefaultTableModel();
        tableModel.addColumn("username");
        tableModel.addColumn("password");
        tableModel.addColumn("email");
        tableModel.addColumn("nohp");
        tableModel.addColumn("active");
        tableModel.addColumn("premium");
        tableModel.addColumn("premiumRequest");

        // Set the table model to the JTable
        jTable1.setModel(tableModel);
        
        // Create the table model 1
        tableModel2 = new DefaultTableModel();
        tableModel2.addColumn("username");
        tableModel2.addColumn("premium");
        tableModel2.addColumn("premiumRequest");

        // Set the table model to the JTable
        jTable2.setModel(tableModel2);
        
        displayData();
        displayDataPremium();
}
  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        table = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        insert = new javax.swing.JButton();
        update = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        premiumActivation = new javax.swing.JButton();
        logout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        insert.setText("Insert Data");
        insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                insertActionPerformed(evt);
            }
        });

        update.setText("Update Data");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });

        delete.setText("Delete Data");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        premiumActivation.setText("Premium Activation");
        premiumActivation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                premiumActivationActionPerformed(evt);
            }
        });

        logout.setText("Logout");
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tableLayout = new javax.swing.GroupLayout(table);
        table.setLayout(tableLayout);
        tableLayout.setHorizontalGroup(
            tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tableLayout.createSequentialGroup()
                .addGroup(tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(premiumActivation, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(insert, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(update, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );
        tableLayout.setVerticalGroup(
            tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tableLayout.createSequentialGroup()
                .addGroup(tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tableLayout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(insert)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(update)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delete))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(tableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tableLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))
                    .addGroup(tableLayout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(premiumActivation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(logout)
                        .addGap(83, 83, 83))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(table, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(630, 630, 630))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(table, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(876, 668));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void insertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_insertActionPerformed
        insertData();
        displayData();
    }//GEN-LAST:event_insertActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        updateData();
        displayData();    
    }//GEN-LAST:event_updateActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        deleteData();
        displayData();    
    }//GEN-LAST:event_deleteActionPerformed

    private void premiumActivationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_premiumActivationActionPerformed
       String userCheck = JOptionPane.showInputDialog(this, "Input the username:");
        sql = "SELECT premiumRequest FROM login WHERE username = ?";
        int premiumRequest = 0;

        try {
            PreparedStatement selectStatement = con.prepareStatement(sql);
            selectStatement.setString(1, userCheck);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                premiumRequest = resultSet.getInt("premiumRequest");
            }

            resultSet.close();
            selectStatement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch data: " + e.getMessage());
        }
        
       if(premiumRequest == 1){
            int option = JOptionPane.showOptionDialog(
                this,
                "Do you want to change " + userCheck + " account to Premium?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Yes", "No"},
                "Yes"
             );

            if (option == JOptionPane.YES_OPTION) {
                // Update premiumRequest to 0 and premium to 1
                sql = "UPDATE login SET premiumRequest = 0, premium = 1 WHERE username = ?";
                 JOptionPane.showMessageDialog(null, "Change Succes, now " + userCheck + " is Premium" );
            } else if (option == JOptionPane.NO_OPTION) {
                // Update premiumRequest to 0
                sql = "UPDATE login SET premiumRequest = 0 WHERE username = ?";
                 JOptionPane.showMessageDialog(null, userCheck + " Premium Request set to 0");
            } else {
                // User closed the dialog or clicked outside the options
                // Handle the cancellation or alternative action
                JOptionPane.showMessageDialog(null, "Do Nothing");
                return; // Exit the method
            }
       }else{
            JOptionPane.showMessageDialog(null, "Request is NULL");
       }

        try {
            PreparedStatement updateStatement = con.prepareStatement(sql);
            updateStatement.setString(1, userCheck);
            int rowsAffected = updateStatement.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated.");

            updateStatement.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to update data: " + e.getMessage());
        }
        
        displayDataPremium();
    }//GEN-LAST:event_premiumActivationActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        JOptionPane.showMessageDialog(null,"Bye Admin");
        
        // Create a new instance of the login screen
        login frame = new login();
        frame.setVisible(true);
        this.setVisible(false);
        
        JOptionPane.showMessageDialog(null, "Logout Berhasil");
    }//GEN-LAST:event_logoutActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton delete;
    private javax.swing.JButton insert;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton logout;
    private javax.swing.JButton premiumActivation;
    private javax.swing.JPanel table;
    private javax.swing.JButton update;
    // End of variables declaration//GEN-END:variables
}
