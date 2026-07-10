package com.ecommerce.dao.impl;
import com.ecommerce.dao.OrdenDAO;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class OrdenDAOImpl implements OrdenDAO {
    private Conexion conexion = new Conexion();
    @Override
    public void generarOrden(double total, String estado) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("INSERT INTO ordenes (total, estado) VALUES (?, ?)")) {
            ps.setDouble(1, total);
            ps.setString(2, estado);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar orden en BD: " + e.getMessage());
        }
    }
}