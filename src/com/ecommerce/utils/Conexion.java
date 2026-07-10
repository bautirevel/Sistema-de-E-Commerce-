package com.ecommerce.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/ecommerce";
    private static final String USER = "root";
    private static final String PASS = "";
    private Connection con;

    public Connection conectar() {
        try {
            con = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Error de conexion: " + e.getMessage());
        }
        return con;
    }

    public void desconectar() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al desconectar: " + e.getMessage());
        }
    }
}