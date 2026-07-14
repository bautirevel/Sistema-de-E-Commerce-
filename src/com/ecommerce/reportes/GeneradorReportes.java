package com.ecommerce.reportes;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class GeneradorReportes {
    public void mostrarEstadisticas() {
        System.out.println("\n==========================================");
        System.out.println("      REPORTES DE GESTION DA VINCI        ");
        System.out.println("==========================================");
        try (Connection con = Conexion.getInstancia().conectar(); Statement st = con.createStatement()) {
            
            // Cantidad total de usuarios
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM usuarios");
            if (rs.next()) System.out.println("-> Cantidad total de usuarios: " + rs.getInt(1));
            
            // Cantidad de clientes
            rs = st.executeQuery("SELECT COUNT(*) FROM usuarios WHERE rol = 'CLIENTE'");
            if (rs.next()) System.out.println("-> Cantidad de clientes: " + rs.getInt(1));
            
            // Cantidad de productos
            rs = st.executeQuery("SELECT COUNT(*) FROM productos");
            if (rs.next()) System.out.println("-> Cantidad de productos en catalogo: " + rs.getInt(1));
            
            // Productos sin stock
            rs = st.executeQuery("SELECT COUNT(*) FROM productos WHERE stock = 0");
            if (rs.next()) System.out.println("-> Productos sin stock: " + rs.getInt(1));
            
            // Ordenes generadas
            rs = st.executeQuery("SELECT COUNT(*) FROM ordenes");
            if (rs.next()) System.out.println("-> Ordenes generadas: " + rs.getInt(1));
            
            // Recaudacion total
            rs = st.executeQuery("SELECT SUM(total) FROM ordenes");
            if (rs.next()) System.out.println("-> Recaudacion total historica: $" + rs.getDouble(1));

            // Reclamos abiertos
            rs = st.executeQuery("SELECT COUNT(*) FROM reclamos WHERE estado = 'ABIERTO'");
            if (rs.next()) System.out.println("-> Reclamos en estado ABIERTO: " + rs.getInt(1));
            
        } catch (SQLException e) {
            System.out.println("Error ejecutando reportes: " + e.getMessage());
        }
        System.out.println("==========================================\n");
    }
}