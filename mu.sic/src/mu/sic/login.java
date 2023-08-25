package mu.sic;

import java.sql.*;
import javax.swing.*;

public class login extends javax.swing.JFrame {
    
    Connection con;
    Statement stat;
    ResultSet rs;
    String sql;
    String username;

    public login() {
        setResizable(false); // Disable resizing of the JFrame
        setTitle("Login Page");
        
        // Set the icon image for the JFrame
        ImageIcon icon = new ImageIcon(getClass().getResource("/image/IconApp.png"));
        setIconImage(icon.getImage());
       
    
        initComponents();
        koneksi DB = new koneksi();
        DB.config();
        con = DB.con;
        stat = DB.stm;
       
    }
    
    public void bersih(){
       usernameJF.setText("");
       passwordJF.setText("");
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        userLabel = new javax.swing.JLabel();
        passwordJF = new javax.swing.JPasswordField();
        usernameJF = new javax.swing.JTextField();
        passLabel = new javax.swing.JLabel();
        signup = new javax.swing.JButton();
        login = new javax.swing.JButton();
        Logo = new javax.swing.JLabel();
        Banner = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 500));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 3, 0, 3, new java.awt.Color(255, 204, 153)));
        jPanel1.setMinimumSize(new java.awt.Dimension(600, 400));
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 200));
        jPanel1.setLayout(null);

        userLabel.setText("Username :");
        jPanel1.add(userLabel);
        userLabel.setBounds(10, 170, 70, 16);

        passwordJF.setBackground(new java.awt.Color(153, 153, 153));
        passwordJF.setForeground(new java.awt.Color(255, 255, 255));
        passwordJF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordJFActionPerformed(evt);
            }
        });
        jPanel1.add(passwordJF);
        passwordJF.setBounds(80, 200, 130, 22);

        usernameJF.setBackground(new java.awt.Color(153, 153, 153));
        usernameJF.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.add(usernameJF);
        usernameJF.setBounds(80, 170, 130, 22);

        passLabel.setText("Password  :");
        jPanel1.add(passLabel);
        passLabel.setBounds(10, 200, 80, 16);

        signup.setBackground(new java.awt.Color(0, 0, 0));
        signup.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        signup.setForeground(new java.awt.Color(255, 255, 255));
        signup.setText("Sign up");
        signup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signupActionPerformed(evt);
            }
        });
        jPanel1.add(signup);
        signup.setBounds(60, 230, 72, 23);

        login.setBackground(new java.awt.Color(0, 0, 0));
        login.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        login.setForeground(new java.awt.Color(255, 255, 255));
        login.setText("Login");
        login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginActionPerformed(evt);
            }
        });
        jPanel1.add(login);
        login.setBounds(140, 230, 72, 23);

        Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo.png"))); // NOI18N
        Logo.setText("jLabel2");
        jPanel1.add(Logo);
        Logo.setBounds(40, 110, 160, 50);

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 220, 380));

        Banner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/loginBanner.gif"))); // NOI18N
        jPanel2.add(Banner, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 380, 380));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setSize(new java.awt.Dimension(616, 418));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginActionPerformed
         username = usernameJF.getText();
         String password = String.valueOf(passwordJF.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Data tidak boleh ada yang kosong");
            return;
        }

        // Admin Site
        if (username.equals("admin") && password.equals("123")) {
            JOptionPane.showMessageDialog(null, "Welcome Admin");
            // Make admin frame opened in center
            admin frame = new admin();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);

            // Make this frame (login) dissapear
            this.setVisible(false);
            return;
        }

            try {
          sql = "SELECT * FROM login";
          rs = stat.executeQuery(sql);
          boolean check = false;

          while (rs.next()) {
              if (username.equals(rs.getString("username")) && password.equals(rs.getString("password"))) {
                  // Update Active to 1
                  sql = "UPDATE login SET active = 1 WHERE username = ?";
                  PreparedStatement updateStatement = con.prepareStatement(sql);
                  updateStatement.setString(1, username);
                  
                  // Execute the update query
                  int rowsAffected = updateStatement.executeUpdate();
                  updateStatement.close();

                  JOptionPane.showMessageDialog(null, "Login Berhasil");

                  // Make main frame opened in center
                  main frame = new main();
                  frame.setUsername(username);
                  frame.prep();
                  frame.setVisible(true);
                  frame.setLocationRelativeTo(null);

                  // Make this frame (login) disappear
                  this.setVisible(false);

                  check = true;
              }
          }

          if (!check) {
              JOptionPane.showMessageDialog(null, "Login Gagal");
              bersih();
          }

      } catch (SQLException e) {
          JOptionPane.showMessageDialog(this, e.getMessage());
          bersih();
      }
    }//GEN-LAST:event_loginActionPerformed

    private void signupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signupActionPerformed
        // Make signup frame opened in center
        signup frame = new signup();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        // Make this frame (login) dissapear
        this.setVisible(false);
    }//GEN-LAST:event_signupActionPerformed

    private void passwordJFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordJFActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordJFActionPerformed
    
    
    
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
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                login frame = new login();
                frame.setLocationRelativeTo(null); // Center the JFrame on the screen
                frame.setVisible(true);
            }
        });
        
         
         
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Banner;
    private javax.swing.JLabel Logo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton login;
    private javax.swing.JLabel passLabel;
    private javax.swing.JPasswordField passwordJF;
    private javax.swing.JButton signup;
    private javax.swing.JLabel userLabel;
    private javax.swing.JTextField usernameJF;
    // End of variables declaration//GEN-END:variables
}
