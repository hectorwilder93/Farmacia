
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {
    
    private final  String database_name = "pharmacy_database";
    private final  String user = "root";
    private final  String password = "";
    private final  String url = "jdbc:mysql://localhost:3306/" + database_name + "?useSSL=false&serverTimezone=UTC";
    Connection conn = null;
    
    public Connection getConnection(){
        try{
            //obtener valor del driver a traves de Jar connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            //obtener la conexion
            conn= DriverManager.getConnection(url,user,password);
        }catch(ClassNotFoundException e){
            System.err.println("Ha ocurrido un ClassNotFoundException " + e.getMessage());
        
        }catch(SQLException e){
            System.err.println("Ha ocurrido un SQLException " + e.getMessage());
        }
        return conn;    
    }   
}
