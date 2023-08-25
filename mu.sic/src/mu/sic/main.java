
package mu.sic;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.Random;
import javax.sound.sampled.*;
/**
 *
 * @author ACER
 */
public class main extends javax.swing.JFrame {

    Connection con;
    Statement stat;
    ResultSet rs;
    String sql;
    
    String username; // Username
    Clip clip; // for setMusicPlayer
    ImageIcon icon; // for changing Image Icon
    
    // for setMusicPlayer and setPlayList
    String[] src =  new String[10];
    String[] title = new String[10];
    String[] banner = new String[10];
   
    int index; // for prev and next song
    
    // Define the color options using RGB values
    Color[] colorOptions;
   
    boolean check1 = false; // For menu1 Data Check
    boolean check2 = false; // For menu2 Data Check
    boolean check3 = false; // For menu3 Data Check
    boolean check4 = false; // For menu4 Data Check
    boolean check5 = false; // For menu5 Data Check
    boolean check6 = false; // For menu6 Data Check
    
    int rows = 3; // Number of rows in the grid
    int columns = 2; // Number of columns in the grid
    int horizontalGap = 0; // Horizontal spacing between components in pixels
    int verticalGap = 0; // Vertical spacing between components in pixels
    JPanel music = new JPanel(new GridLayout(rows, columns, horizontalGap, verticalGap)); // Create a new JPanel with GridLayout and spacing
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public void prep(){
        UserName.setText(username);
        premiumTab.setVisible(false);
        premiumDone.setVisible(false);
        Template.setVisible(true);
        requestDone.setVisible(false);
        musicPlayer.setVisible(true);
        musicList.setVisible(false);
        checkPremium();
    }
   
    public void checkPremium(){
        // Check Premium 
        try {
            sql = "SELECT premium FROM login WHERE username = '" + username + "'";
            rs = stat.executeQuery(sql);
            
            // Check jika user Premium
             if (rs.next()) {
                
                if (rs.getInt("premium") == 1) {
                   premium.setForeground(new java.awt.Color(0, 153, 0));
                }else{
                   premium.setForeground(new java.awt.Color(255, 0, 51)); 
                }
             }

            // Close the result set and statement
            rs.close();
            stat.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    public void setRandomBackgroundColor() {  
        Random random = new Random();
        int index = random.nextInt(colorOptions.length);
        Color randomColor = colorOptions[index];

        musicPlayer.setBackground(randomColor);
    }
    
public void setMusicPlayer(String src,String title,String banner) {
    // Stop the clip if clip still running 
    if (clip != null && clip.isRunning()) {
            clip.stop();
    }
    
    // Make Music Player Visible
    setRandomBackgroundColor();
    musicPlayer.setVisible(true);
    musicList.setVisible(false);
    // Set Song Title
    songTitle.setText(title);
    
    // Set Song Banner
    icon = new ImageIcon(getClass().getResource("/musicBanner/" + banner));
    songBanner.setIcon(icon);
    
    // Run Music Player Code Here
    try {
        File soundFile = new File("src/audio/" + src);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);

        clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        // Get the gain control from the clip
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        // Calculate the desired volume level in decibels (dB)
        // Increase the max volume (6.0206db)
        float volume = 6.0206f;

        // Set the volume level
        gainControl.setValue(volume);

        clip.start();
        icon = new ImageIcon(getClass().getResource("/image/pause.png"));
        start.setIcon(icon);
        icon = new ImageIcon(getClass().getResource("/image/disk.gif"));
        musicIcon.setIcon(icon);
        
        // When Song reach end
        clip.addLineListener(new LineListener() {
            public void update(LineEvent event) {
                if (event.getType() == LineEvent.Type.STOP) {
                    // i will update the icon logo
                    icon = new ImageIcon(getClass().getResource("/image/play.png"));
                    start.setIcon(icon);
                    icon = new ImageIcon(getClass().getResource("/image/disk.png"));
                    musicIcon.setIcon(icon);
                }
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}
   
public void setMenu(boolean check, String databaseName) {
    musicList.setVisible(true);
    musicPlayer.setVisible(false);
    
    if (!check) {
        int i = 0;
        try {
            sql = "SELECT * FROM " + databaseName;
            rs = stat.executeQuery(sql);
            
            // remove all components
            music.removeAll();
            while (rs.next() && i < (rows * columns)) {
                // Make a playlist Button
                javax.swing.JButton playListButton = new javax.swing.JButton();
                playListButton.setBackground(new java.awt.Color(102, 102, 102));
                playListButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/ListPlay.png"))); // NOI18N
                playListButton.setBorder(null);
                playListButton.setBorderPainted(false);
                playListButton.setContentAreaFilled(false);
                playListButton.setPreferredSize(new java.awt.Dimension(75, 30));
                final int currentIndex = i; // Declare i as final
                playListButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        playListButtonActionPerformed(evt, currentIndex); // Pass the currentIndex
                    }
                });
                playListButton.setMaximumSize(playListButton.getPreferredSize());
                music.add(playListButton); // Add the playlist button to the panel

                
                // Make a Label Button
                javax.swing.JLabel label = new javax.swing.JLabel();
                label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
                label.setForeground(new java.awt.Color(0, 0, 0));
                label.setText(rs.getString("title"));
                music.add(label);

                src[i] = rs.getString("source");
                title[i] = rs.getString("title");
                banner[i] = rs.getString("banner");
                i++;
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            // Close the result set and statement in the finally block
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // Handle the exception or log it
                }
            }
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    // Handle the exception or log it
                }
            }
        }

        musicList.setLayout(new BorderLayout());
        musicList.add(music, BorderLayout.NORTH);
        musicList.revalidate(); // Revalidate the musicList panel to ensure the changes are visible
    }
    
} 
    public main() 
    {
        
        setResizable(false); // Disable resizing of the JFrame
        setTitle("MU.SIC");
        
        // Set the icon image for the JFrame
        ImageIcon icon = new ImageIcon(getClass().getResource("/image/IconApp.png"));
        setIconImage(icon.getImage());
        
        initComponents();
        koneksi DB = new koneksi();
        DB.config();
        con = DB.con;
        stat = DB.stm;
        
        // Set Color for Random Background Color    
        colorOptions = new Color[]{
               new Color(204, 204, 255),     // Purple
               new Color(204, 255, 204),     // Green
               new Color(255, 255, 204)      // Yellow
       };
        
    // Add window closing listener
    addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            try {
                // Update Active to 0
                String updateSql = "UPDATE login SET active = 0 WHERE username = ?";
                PreparedStatement updateStatement = con.prepareStatement(updateSql);
                updateStatement.setString(1, username);

                // Execute the update query
                int rowsAffected = updateStatement.executeUpdate();

            } catch (SQLException ex) {
                // Handle any exceptions that occur during the update
                ex.printStackTrace();
            }  
            super.windowClosing(e);
        }  
    });
    
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Template = new javax.swing.JPanel();
        menubar = new javax.swing.JPanel();
        userPanel = new javax.swing.JPanel();
        userIcon = new javax.swing.JLabel();
        UserName = new javax.swing.JLabel();
        menu1 = new javax.swing.JButton();
        menu2 = new javax.swing.JButton();
        menu3 = new javax.swing.JButton();
        menu4 = new javax.swing.JButton();
        menu5 = new javax.swing.JButton();
        menu6 = new javax.swing.JButton();
        premium = new javax.swing.JButton();
        musicLogo = new javax.swing.JPanel();
        musicIcon = new javax.swing.JButton();
        musicList = new javax.swing.JPanel();
        top = new javax.swing.JPanel();
        logout = new javax.swing.JButton();
        musicPlayer = new javax.swing.JPanel();
        songBanner = new javax.swing.JLabel();
        songTitle = new javax.swing.JLabel();
        prev = new javax.swing.JButton();
        next = new javax.swing.JButton();
        start = new javax.swing.JButton();
        premiumTab = new javax.swing.JPanel();
        back = new javax.swing.JButton();
        buyPremium = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        premiumDone = new javax.swing.JLabel();
        requestDone = new javax.swing.JLabel();
        PremiumList = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Template.setBackground(new java.awt.Color(0, 0, 0));
        Template.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menubar.setBackground(new java.awt.Color(0, 0, 0));
        menubar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        userPanel.setBackground(new java.awt.Color(255, 255, 255));
        userPanel.setForeground(new java.awt.Color(255, 255, 255));

        userIcon.setForeground(new java.awt.Color(255, 255, 255));
        userIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/userLogo .png"))); // NOI18N
        userIcon.setText("jLabel1");

        UserName.setBackground(new java.awt.Color(255, 255, 255));
        UserName.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        UserName.setText("User");

        javax.swing.GroupLayout userPanelLayout = new javax.swing.GroupLayout(userPanel);
        userPanel.setLayout(userPanelLayout);
        userPanelLayout.setHorizontalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(userIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(UserName, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        userPanelLayout.setVerticalGroup(
            userPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userPanelLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(userIcon))
            .addGroup(userPanelLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(UserName, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        menubar.add(userPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 60));

        menu1.setBackground(new java.awt.Color(0, 0, 0));
        menu1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        menu1.setForeground(new java.awt.Color(255, 255, 255));
        menu1.setText("Jazz");
        menu1.setBorder(null);
        menu1.setBorderPainted(false);
        menu1.setContentAreaFilled(false);
        menu1.setFocusPainted(false);
        menu1.setHideActionText(true);
        menu1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        menu1.setInheritsPopupMenu(true);
        menu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu1ActionPerformed(evt);
            }
        });
        menubar.add(menu1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 170, 60));

        menu2.setBackground(new java.awt.Color(0, 0, 0));
        menu2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        menu2.setForeground(new java.awt.Color(255, 255, 255));
        menu2.setText("Country");
        menu2.setBorder(null);
        menu2.setBorderPainted(false);
        menu2.setContentAreaFilled(false);
        menu2.setFocusPainted(false);
        menu2.setHideActionText(true);
        menu2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        menu2.setInheritsPopupMenu(true);
        menu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu2ActionPerformed(evt);
            }
        });
        menubar.add(menu2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 170, 60));

        menu3.setBackground(new java.awt.Color(0, 0, 0));
        menu3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        menu3.setForeground(new java.awt.Color(255, 255, 255));
        menu3.setText("EDM");
        menu3.setBorder(null);
        menu3.setBorderPainted(false);
        menu3.setContentAreaFilled(false);
        menu3.setFocusPainted(false);
        menu3.setHideActionText(true);
        menu3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        menu3.setInheritsPopupMenu(true);
        menu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu3ActionPerformed(evt);
            }
        });
        menubar.add(menu3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 170, 60));

        menu4.setBackground(new java.awt.Color(0, 0, 0));
        menu4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        menu4.setForeground(new java.awt.Color(255, 255, 255));
        menu4.setText("Rock");
        menu4.setBorder(null);
        menu4.setBorderPainted(false);
        menu4.setContentAreaFilled(false);
        menu4.setFocusPainted(false);
        menu4.setHideActionText(true);
        menu4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        menu4.setInheritsPopupMenu(true);
        menu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu4ActionPerformed(evt);
            }
        });
        menubar.add(menu4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 170, 60));

        menu5.setBackground(new java.awt.Color(0, 0, 0));
        menu5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        menu5.setForeground(new java.awt.Color(255, 255, 255));
        menu5.setText("Pop");
        menu5.setBorder(null);
        menu5.setBorderPainted(false);
        menu5.setContentAreaFilled(false);
        menu5.setFocusPainted(false);
        menu5.setHideActionText(true);
        menu5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        menu5.setInheritsPopupMenu(true);
        menu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu5ActionPerformed(evt);
            }
        });
        menubar.add(menu5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 170, 60));

        menu6.setBackground(new java.awt.Color(0, 0, 0));
        menu6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        menu6.setForeground(new java.awt.Color(255, 255, 255));
        menu6.setText("Gospel");
        menu6.setBorder(null);
        menu6.setBorderPainted(false);
        menu6.setContentAreaFilled(false);
        menu6.setFocusPainted(false);
        menu6.setHideActionText(true);
        menu6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        menu6.setInheritsPopupMenu(true);
        menu6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menu6ActionPerformed(evt);
            }
        });
        menubar.add(menu6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 370, 170, 60));

        premium.setBackground(new java.awt.Color(0, 0, 0));
        premium.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        premium.setForeground(new java.awt.Color(255, 0, 51));
        premium.setText("Premium");
        premium.setBorder(null);
        premium.setBorderPainted(false);
        premium.setContentAreaFilled(false);
        premium.setFocusPainted(false);
        premium.setHideActionText(true);
        premium.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        premium.setInheritsPopupMenu(true);
        premium.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                premiumActionPerformed(evt);
            }
        });
        menubar.add(premium, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 170, 60));

        musicLogo.setBackground(new java.awt.Color(0, 0, 0));

        musicIcon.setBackground(new java.awt.Color(0, 0, 0));
        musicIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/disk.png"))); // NOI18N
        musicIcon.setBorder(null);
        musicIcon.setBorderPainted(false);
        musicIcon.setContentAreaFilled(false);
        musicIcon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                musicIconActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout musicLogoLayout = new javax.swing.GroupLayout(musicLogo);
        musicLogo.setLayout(musicLogoLayout);
        musicLogoLayout.setHorizontalGroup(
            musicLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(musicLogoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(musicIcon)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        musicLogoLayout.setVerticalGroup(
            musicLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(musicLogoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(musicIcon)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menubar.add(musicLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 170, 130));

        Template.add(menubar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 630));

        musicList.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout musicListLayout = new javax.swing.GroupLayout(musicList);
        musicList.setLayout(musicListLayout);
        musicListLayout.setHorizontalGroup(
            musicListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 690, Short.MAX_VALUE)
        );
        musicListLayout.setVerticalGroup(
            musicListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );

        Template.add(musicList, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 690, 540));

        top.setBackground(new java.awt.Color(255, 255, 255));

        logout.setBackground(new java.awt.Color(0, 0, 0));
        logout.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setText("Logout");
        logout.setBorderPainted(false);
        logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout topLayout = new javax.swing.GroupLayout(top);
        top.setLayout(topLayout);
        topLayout.setHorizontalGroup(
            topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topLayout.createSequentialGroup()
                .addContainerGap(587, Short.MAX_VALUE)
                .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        topLayout.setVerticalGroup(
            topLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(logout, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Template.add(top, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 690, 60));

        musicPlayer.setBackground(new java.awt.Color(255, 255, 204));
        musicPlayer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        songBanner.setForeground(new java.awt.Color(255, 255, 255));
        songBanner.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        musicPlayer.add(songBanner, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, 570, 320));

        songTitle.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        songTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        songTitle.setText("Select Song First");
        musicPlayer.add(songTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 690, -1));

        prev.setForeground(new java.awt.Color(255, 255, 255));
        prev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/prev.png"))); // NOI18N
        prev.setBorder(null);
        prev.setBorderPainted(false);
        prev.setContentAreaFilled(false);
        prev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevActionPerformed(evt);
            }
        });
        musicPlayer.add(prev, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 460, -1, -1));

        next.setForeground(new java.awt.Color(255, 255, 255));
        next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/next.png"))); // NOI18N
        next.setBorder(null);
        next.setBorderPainted(false);
        next.setContentAreaFilled(false);
        next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextActionPerformed(evt);
            }
        });
        musicPlayer.add(next, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 460, -1, 71));

        start.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/play.png"))); // NOI18N
        start.setBorder(null);
        start.setBorderPainted(false);
        start.setContentAreaFilled(false);
        start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionPerformed(evt);
            }
        });
        musicPlayer.add(start, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 460, -1, -1));

        Template.add(musicPlayer, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 690, 540));

        premiumTab.setBackground(new java.awt.Color(255, 255, 255));
        premiumTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        back.setBackground(new java.awt.Color(0, 0, 0));
        back.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        back.setText("X");
        back.setBorder(null);
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });
        premiumTab.add(back, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 0, -1, -1));

        buyPremium.setBackground(new java.awt.Color(0, 0, 0));
        buyPremium.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        buyPremium.setForeground(new java.awt.Color(255, 255, 255));
        buyPremium.setText("BUY PREMIUM");
        buyPremium.setBorder(null);
        buyPremium.setBorderPainted(false);
        buyPremium.setFocusPainted(false);
        buyPremium.setHideActionText(true);
        buyPremium.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buyPremium.setInheritsPopupMenu(true);
        buyPremium.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buyPremiumActionPerformed(evt);
            }
        });
        premiumTab.add(buyPremium, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 400, 230, 60));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo.png"))); // NOI18N
        premiumTab.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, 441, 101));

        premiumDone.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        premiumDone.setForeground(new java.awt.Color(51, 153, 0));
        premiumDone.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        premiumDone.setText("Akun Premium!!");
        premiumTab.add(premiumDone, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 250, 790, -1));

        requestDone.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        requestDone.setText("Request anda masih dalam proses oleh Admin");
        premiumTab.add(requestDone, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 250, -1, -1));

        PremiumList.setBackground(new java.awt.Color(255, 204, 102));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Dengarkan secara offline");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Buatlah playlist sendiri");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 153, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Hanya 1$/Bulan");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Bebas iklan");

        javax.swing.GroupLayout PremiumListLayout = new javax.swing.GroupLayout(PremiumList);
        PremiumList.setLayout(PremiumListLayout);
        PremiumListLayout.setHorizontalGroup(
            PremiumListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PremiumListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PremiumListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE))
                .addContainerGap())
        );
        PremiumListLayout.setVerticalGroup(
            PremiumListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PremiumListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        premiumTab.add(PremiumList, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 220, 380, 160));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Template, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(premiumTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Template, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(premiumTab, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(876, 668));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu1ActionPerformed

    try {
        stat = con.createStatement();
        setMenu(check1, "jazz");
        check1 = true;
        check2 = false;
        check3 = false;
        check4 = false;
        check5 = false;
        check6 = false;
        menu1.setForeground(new java.awt.Color(255, 255, 0));
        menu2.setForeground(new java.awt.Color(255, 255, 255));
        menu3.setForeground(new java.awt.Color(255, 255, 255));
        menu4.setForeground(new java.awt.Color(255, 255, 255));
        menu5.setForeground(new java.awt.Color(255, 255, 255));
        menu6.setForeground(new java.awt.Color(255, 255, 255));

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }



    }//GEN-LAST:event_menu1ActionPerformed

private void playListButtonActionPerformed(java.awt.event.ActionEvent evt, int i) {
    setMusicPlayer(src[i],title[i],banner[i]);
    index = i+1;
}

    private void menu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu2ActionPerformed
        try {
            stat = con.createStatement();
            setMenu(check2, "country");
            check1 = false;
            check2 = true;
            check3 = false;
            check4 = false;
            check5 = false;
            check6 = false;
            menu1.setForeground(new java.awt.Color(255, 255, 255));
            menu2.setForeground(new java.awt.Color(255, 255, 0));
            menu3.setForeground(new java.awt.Color(255, 255, 255));
            menu4.setForeground(new java.awt.Color(255, 255, 255));
            menu5.setForeground(new java.awt.Color(255, 255, 255));
            menu6.setForeground(new java.awt.Color(255, 255, 255));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_menu2ActionPerformed

    private void menu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu3ActionPerformed
    menu1.setForeground(new java.awt.Color(255, 255, 255));
    menu2.setForeground(new java.awt.Color(255, 255, 255));
    menu3.setForeground(new java.awt.Color(255, 255, 0));
    menu4.setForeground(new java.awt.Color(255, 255, 255));
    menu5.setForeground(new java.awt.Color(255, 255, 255));
    menu6.setForeground(new java.awt.Color(255, 255, 255));

         try {
            stat = con.createStatement();
            setMenu(check3, "edm");
            check1 = false;
            check2 = false;
            check3 = true;
            check4 = false;
            check5 = false;
            check6 = false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_menu3ActionPerformed

    private void menu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu4ActionPerformed
        menu1.setForeground(new java.awt.Color(255, 255, 255));
        menu2.setForeground(new java.awt.Color(255, 255, 255));
        menu3.setForeground(new java.awt.Color(255, 255, 255));
        menu4.setForeground(new java.awt.Color(255, 255, 0));
        menu5.setForeground(new java.awt.Color(255, 255, 255));
        menu6.setForeground(new java.awt.Color(255, 255, 255));

         try {
            stat = con.createStatement();
            setMenu(check4, "rock");
            check1 = false;
            check2 = false;
            check3 = false;
            check4 = true;
            check5 = false;
            check6 = false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_menu4ActionPerformed

    private void menu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu5ActionPerformed
        menu1.setForeground(new java.awt.Color(255, 255, 255));
        menu2.setForeground(new java.awt.Color(255, 255, 255));
        menu3.setForeground(new java.awt.Color(255, 255, 255));
        menu4.setForeground(new java.awt.Color(255, 255, 255));
        menu5.setForeground(new java.awt.Color(255, 255, 0));
        menu6.setForeground(new java.awt.Color(255, 255, 255));

         try {
            stat = con.createStatement();
            setMenu(check5, "pop");
            check1 = false;
            check2 = false;
            check3 = false;
            check4 = false;
            check5 = true;
            check6 = false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_menu5ActionPerformed

    private void menu6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menu6ActionPerformed
        menu1.setForeground(new java.awt.Color(255, 255, 255));
        menu2.setForeground(new java.awt.Color(255, 255, 255));
        menu3.setForeground(new java.awt.Color(255, 255, 255));
        menu4.setForeground(new java.awt.Color(255, 255, 255));
        menu5.setForeground(new java.awt.Color(255, 255, 255));
        menu6.setForeground(new java.awt.Color(255, 255, 0));

         try {
            stat = con.createStatement();
            setMenu(check6, "gospel");
            check1 = false;
            check2 = false;
            check3 = false;
            check4 = false;
            check5 = false;
            check6 = true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_menu6ActionPerformed

    private void prevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevActionPerformed
        if(index == 1){
            setMusicPlayer(src[2],title[2],banner[2]);
            index = 3;
        }else if(index == 2){
            setMusicPlayer(src[0],title[0],banner[0]);
            index--;
        }else{
            setMusicPlayer(src[1],title[1],banner[1]);
            index--;
        }
        
    }//GEN-LAST:event_prevActionPerformed

    private void musicIconActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_musicIconActionPerformed
        setRandomBackgroundColor();
        musicPlayer.setVisible(true);
        musicList.setVisible(false);
    }//GEN-LAST:event_musicIconActionPerformed

    private void startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startActionPerformed

       if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();  // Stop the song if it is currently playing
                icon = new ImageIcon(getClass().getResource("/image/play.png"));
                start.setIcon(icon);
                icon = new ImageIcon(getClass().getResource("/image/disk.png"));
                musicIcon.setIcon(icon);
            } else if (clip.getMicrosecondPosition() >= clip.getMicrosecondLength()) {
                // Replay the song if it has reached the end
                clip.setMicrosecondPosition(0);  // Reset the position to the beginning
                clip.start();
                icon = new ImageIcon(getClass().getResource("/image/pause.png"));
                start.setIcon(icon);
                icon = new ImageIcon(getClass().getResource("/image/disk.gif"));
                musicIcon.setIcon(icon);
            } else {
                clip.start(); // Resume playing the song if it is paused
                icon = new ImageIcon(getClass().getResource("/image/pause.png"));
                start.setIcon(icon);
                icon = new ImageIcon(getClass().getResource("/image/disk.gif"));
                musicIcon.setIcon(icon);
            }
        }
    }//GEN-LAST:event_startActionPerformed
   
    private void nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextActionPerformed
        if(index == 1){
            setMusicPlayer(src[1],title[1],banner[1]);
            index++;
        }else if(index == 2){
            setMusicPlayer(src[2],title[2],banner[2]);
            index++;
        }else{
            setMusicPlayer(src[0],title[0],banner[0]);
            index = 1;
        }
    }//GEN-LAST:event_nextActionPerformed

    private void logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutActionPerformed
        // Stop the clip if clip still running 
        if (clip != null && clip.isRunning()) {
                clip.stop();
        }
        // Create a new instance of the login screen
        login frame = new login();
        frame.setVisible(true);
        this.setVisible(false);
        
        // Change Activity Database to 0
        try {
            // Update Active to 0
            String updateSql = "UPDATE login SET active = 0 WHERE username = ?";
            PreparedStatement updateStatement = con.prepareStatement(updateSql);
            updateStatement.setString(1, username);
            
            // Execute the update query
            int rowsAffected = updateStatement.executeUpdate();
            
        } catch (SQLException ex) {
            // Handle any exceptions that occur during the update
            ex.printStackTrace();
        }  
        
        JOptionPane.showMessageDialog(null, "Logout Berhasil");

    }//GEN-LAST:event_logoutActionPerformed

    private void premiumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_premiumActionPerformed
        premiumTab.setVisible(true);
        Template.setVisible(false);
        try {
            sql = "SELECT * FROM login WHERE username = '" + username + "'";
            Statement stat1 = con.createStatement();
            rs = stat1.executeQuery(sql);

            // Check if the user has made a previous request
            if (rs.next()) {
                if (rs.getInt("premiumRequest") == 1) {
                    requestDone.setVisible(true);
                    PremiumList.setVisible(false);
                    buyPremium.setVisible(false);
                }

                if (rs.getInt("premium") == 1) {
                    premiumTab.setVisible(true);
                    premiumDone.setVisible(true);
                    PremiumList.setVisible(false);
                    buyPremium.setVisible(false);
                }
            }

            // Close the first result set and statement
            rs.close();
            stat1.close();

            // Create a new statement object for the second query
            Statement stat2 = con.createStatement();
            sql = "SELECT premiumRequest FROM login WHERE username = '" + username + "'";
            rs = stat2.executeQuery(sql);

            // ... continue with the rest of your code ...

            // Close the second result set and statement
            rs.close();
            stat2.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_premiumActionPerformed

    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        premiumTab.setVisible(false);
        Template.setVisible(true);
    }//GEN-LAST:event_backActionPerformed

    private void buyPremiumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buyPremiumActionPerformed
       JOptionPane.showMessageDialog(null,"Permintaan anda sedang diproses oleh Admin");
       
        // Change PremiumReq Database to 1
        try {
            // Update Active to 1
            String updateSql = "UPDATE login SET premiumRequest = 1 WHERE username = ?";
            PreparedStatement updateStatement = con.prepareStatement(updateSql);
            updateStatement.setString(1, username);
            
            // Execute the update query
            int rowsAffected = updateStatement.executeUpdate();
            
        } catch (SQLException ex) {
            // Handle any exceptions that occur during the update
            ex.printStackTrace();
        }  
        
        requestDone.setVisible(true);
        PremiumList.setVisible(false);
        buyPremium.setVisible(false);
    }//GEN-LAST:event_buyPremiumActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) 
    {
        
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
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new main().setVisible(true);
                
            }
        });
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PremiumList;
    private javax.swing.JPanel Template;
    private javax.swing.JLabel UserName;
    private javax.swing.JButton back;
    private javax.swing.JButton buyPremium;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JButton logout;
    private javax.swing.JButton menu1;
    private javax.swing.JButton menu2;
    private javax.swing.JButton menu3;
    private javax.swing.JButton menu4;
    private javax.swing.JButton menu5;
    private javax.swing.JButton menu6;
    private javax.swing.JPanel menubar;
    private javax.swing.JButton musicIcon;
    private javax.swing.JPanel musicList;
    private javax.swing.JPanel musicLogo;
    private javax.swing.JPanel musicPlayer;
    private javax.swing.JButton next;
    private javax.swing.JButton premium;
    private javax.swing.JLabel premiumDone;
    private javax.swing.JPanel premiumTab;
    private javax.swing.JButton prev;
    private javax.swing.JLabel requestDone;
    private javax.swing.JLabel songBanner;
    private javax.swing.JLabel songTitle;
    private javax.swing.JButton start;
    private javax.swing.JPanel top;
    private javax.swing.JLabel userIcon;
    private javax.swing.JPanel userPanel;
    // End of variables declaration//GEN-END:variables
}
