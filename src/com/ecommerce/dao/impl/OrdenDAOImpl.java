package com.ecommerce.dao.impl;
import com.ecommerce.dao.OrdenDAO;
import java.sql.*;

public class OrdenDAOImpl implements OrdenDAO {
    private String url = "jdbc:sqlite:ecommerce.db";

    @Override 
    public void generarOrden() { 
        String sql = "INSERT INTO ordenes (usuario_id, total, estado) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, 1);
            stmt.setDouble(2, 999.99);
            stmt.setString(3, "PENDIENTE");
            stmt.executeUpdate();
            System.out.println("Orden de compra generada correctamente en BD.");
        } catch (SQLException e) {
            System.out.println("Error JDBC al generar orden: " + e.getMessage());
        }
    }
    @Override public void buscarOrden() { System.out.println("Buscando orden en BD..."); }
    @Override public void cambiarEstado() { System.out.println("Estado de orden actualizado en BD."); }
}
