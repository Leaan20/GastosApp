package org.GastosApp.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Clase que se conectara con la DB
public class DBConnect {
    private static final String URL = "jdbc:mysql://localhost:3306/gastos_app";
    private static final String USER = "root";
    private static final String PASSWORD = "Leaan-rhcpmysql20";

    // metodo estatico de conexion
    public static Connection getConexion() throws SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER,PASSWORD);
        }catch(ClassNotFoundException exc){
            throw new SQLException("No se encontro el drive de MySQL", exc);
        }
    }



}
