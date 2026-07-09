package com.ecommerce.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {
    private Connection con;

    public Connection conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            //ruta a la bd
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecommerce", "root", "");
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (Exception e) {
            System.out.println("Error al conectarse a la base de datos: " + e.getMessage());
        }
        return con;
    }

    public void desconectar() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (Exception e) {
            System.out.println("Error al cerrar la conexión.");
        }
    }
}
