package com.ecommerce.dao.impl;
import com.ecommerce.dao.InventarioDAO;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class InventarioDAOImpl implements InventarioDAO {
    private Conexion conexion = new Conexion();
    @Override
    public void restarStock(String codigoProducto, int cantidad) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("UPDATE productos SET stock = stock - ? WHERE codigo = ?")) {
            ps.setInt(1, cantidad);
            ps.setString(2, codigoProducto);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error actualizando stock: " + e.getMessage());
        }
    }
}