package com.ecommerce.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/ecommerce";
    private static final String USER = "root";
    private static final String PASS = "";

    public Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            // Si la base de datos no existe, la crea al vuelo
            try {
                Connection conBase = DriverManager.getConnection("jdbc:mysql://localhost:3306/", USER, PASS);
                conBase.createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS ecommerce");
                return DriverManager.getConnection(URL, USER, PASS);
            } catch (SQLException ex) {
                System.out.println("[ERROR CRITICO DB]: Verifica que XAMPP (MySQL) este encendido.");
                return null;
            }
        }
    }
}