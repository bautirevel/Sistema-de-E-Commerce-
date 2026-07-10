package com.ecommerce.dao.impl;
import com.ecommerce.dao.InventarioDAO;
import java.sql.*;

public class InventarioDAOImpl implements InventarioDAO {
    private String url = "jdbc:sqlite:ecommerce.db";

    @Override 
    public void actualizarStock() { 
        String sql = "UPDATE inventario SET cantidad = ? WHERE producto_id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, 50); // Cantidad dummy
            stmt.setInt(2, 1);  // ID dummy
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("Stock actualizado correctamente en BD.");
            }
        } catch (SQLException e) {
            System.out.println("Error JDBC: " + e.getMessage());
        }
    }
    @Override public void consultarStock() { System.out.println("Consultando stock actual en BD..."); }
}
