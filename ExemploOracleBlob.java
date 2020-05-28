/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testedbfiap;

import java.io.ByteArrayOutputStream;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author calixto
 */
public class ExemploOracleBlob {

    /**
     * @param args the command line arguments
     */
    
    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream(); 
        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer)) { 
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
}

    
    public static void main(String[] args) {
        try {
            // TODO code application logic here

            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

            String url = "yoururl";
            String user = "youruser";
            String password = "yourpasswd";

            Connection conn = DriverManager.getConnection(url, user, password);
            conn.setSchema("yourschema");

            if (!conn.isClosed()) {
                System.out.println("Connected!");
            }
            try {

                Statement stmt = conn.createStatement();
                
                String query = "INSERT INTO testeblob(name, rawdata) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(query);
            
                pstmt.setString(1,"imagem");
   
                FileInputStream fin;
                fin = new FileInputStream("/Users/calixto/Documents/mari_rafa_1.jpg");
                pstmt.setBinaryStream(2, fin);
                pstmt.execute();
                
                System.out.println("Inserido");
                
                //recebendo figura
                FileOutputStream fos = new FileOutputStream("saida2TDSJ.jpg");
                InputStream is = null;
                
                ResultSet rs = stmt.executeQuery("Select rawdata from testeblob");
                while(rs.next()) {
                   
                   is = rs.getBinaryStream("rawdata");                 
                   
                   System.out.println();
                }
                
                fos.write(getBytesFromInputStream(is));
                fos.flush();
                fos.close();
                
                System.out.println("Gravado");

                
            } catch (FileNotFoundException ex) {
                System.out.println("Arquivo não encontrado");
            } catch (IOException ex) {
                Logger.getLogger(TesteDBFiap.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            

            conn.close();

        } catch (SQLException ex) {
            Logger.getLogger(TesteDBFiap.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
