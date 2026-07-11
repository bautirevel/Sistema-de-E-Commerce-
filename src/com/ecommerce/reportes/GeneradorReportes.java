package com.ecommerce.reportes;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class GeneradorReportes {
    private Conexion conexion = new Conexion();
    
    public void mostrarEstadisticas() {
        try (Connection con = conexion.conectar();
             Statement st = con.createStatement()) {
            System.out.println("\n--- REPORTES DE GESTION ---");
            
            ResultSet rsUsers = st.executeQuery("SELECT COUNT(*) AS total FROM usuarios");
            if(rsUsers.next()) System.out.println("Total Usuarios: " + rsUsers.getInt("total"));
            
            ResultSet rsProds = st.executeQuery("SELECT COUNT(*) AS total FROM productos");
            if(rsProds.next()) System.out.println("Total Productos en BD: " + rsProds.getInt("total"));
            
            ResultSet rsOrdenes = st.executeQuery("SELECT COUNT(*) AS total, SUM(total) AS plata FROM ordenes");
            if(rsOrdenes.next()) {
                System.out.println("Ordenes Generadas: " + rsOrdenes.getInt("total"));
                System.out.println("Recaudacion Total: $" + rsOrdenes.getDouble("plata"));
            }
        } catch (SQLException e) { System.out.println("Error en reportes: " + e.getMessage()); }
    }
}