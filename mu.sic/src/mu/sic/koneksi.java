package mu.sic;
import java.sql.*;
import javax.swing.JOptionPane;

public class koneksi {
   public Connection con;
   public Statement stm;
   
   public void config(){
       try{
           Class.forName("com.mysql.cj.jdbc.Driver");
           con = (Connection) DriverManager.getConnection ("jdbc:mysql://localhost/music","root","");
           stm = (Statement) con.createStatement();
       }catch(Exception e){
         JOptionPane.showMessageDialog(null, "Koneksi Gagal " + e.getMessage());
       }
   }
   
}
