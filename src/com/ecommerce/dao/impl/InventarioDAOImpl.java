package com.ecommerce.dao.impl;
import com.ecommerce.dao.InventarioDAO;
import com.ecommerce.exceptions.StockInsuficienteException;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class InventarioDAOImpl implements InventarioDAO {
    private Conexion conexion = new Conexion();
    
    @Override
    public void restarStock(String codigo, int cantidad) throws StockInsuficienteException {
        try (Connection con = conexion.conectar(); 
             PreparedStatement ps = con.prepareStatement("SELECT stock FROM productos WHERE codigo = ?")) {
             
            ps.setString(1, codigo); 
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int stockActual = rs.getInt("stock");
                if (stockActual < cantidad) {
                    throw new StockInsuficienteException("Stock insuficiente para: " + codigo + " (Stock actual: " + stockActual + ")");
                }
                
                PreparedStatement psUpdate = con.prepareStatement("UPDATE productos SET stock = stock - ? WHERE codigo = ?");
                psUpdate.setInt(1, cantidad); 
                psUpdate.setString(2, codigo); 
                int filasAfectadas = psUpdate.executeUpdate();
                
                if (filasAfectadas > 0) {
                    System.out.println("[EXITO]: Stock descontado correctamente.");
                }
            } else {
                System.out.println("[ERROR]: Producto no encontrado en el inventario.");
            }
        } catch (SQLException e) { 
            System.out.println("Error critico de BD en inventario: " + e.getMessage()); 
        }
    }
}