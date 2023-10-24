/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectdictionary;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 *
 * @author hahah
 */
public class database {
    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/dictionary", "root", "");
            return connect;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
