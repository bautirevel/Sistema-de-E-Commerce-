package com.ecommerce.dao.impl;
import com.ecommerce.dao.InventarioDAO;
import com.ecommerce.exceptions.StockInsuficienteException;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class InventarioDAOImpl implements InventarioDAO {
    
    public InventarioDAOImpl() {
        try (Connection con = Conexion.getInstancia().conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS inventario_movimientos (id INT AUTO_INCREMENT PRIMARY KEY, producto_codigo VARCHAR(50), tipo VARCHAR(20), cantidad INT, fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
        } catch (SQLException e) {}
    }

    @Override
    public void restarStock(String codigo, int cantidad) throws StockInsuficienteException {
        alterarStock(codigo, -cantidad, "EGRESO");
    }

    public void sumarStock(String codigo, int cantidad) {
        try { alterarStock(codigo, cantidad, "INGRESO"); } catch(Exception e) {}
    }

    public void ajustarStock(String codigo, int nuevaCantidad) {
        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement("UPDATE productos SET stock = ? WHERE codigo = ?")) {
            ps.setInt(1, nuevaCantidad);
            ps.setString(2, codigo);
            ps.executeUpdate();
            registrarMovimiento(codigo, "AJUSTE", nuevaCantidad);
        } catch (SQLException e) {}
    }

    private void alterarStock(String codigo, int cantidad, String tipo) throws StockInsuficienteException {
        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement("SELECT stock FROM productos WHERE codigo = ?")) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int stockActual = rs.getInt("stock");
                if (stockActual + cantidad < 0) {
                    throw new StockInsuficienteException("Stock insuficiente.");
                }
                PreparedStatement psUpdate = con.prepareStatement("UPDATE productos SET stock = stock + ? WHERE codigo = ?");
                psUpdate.setInt(1, cantidad);
                psUpdate.setString(2, codigo);
                psUpdate.executeUpdate();
                registrarMovimiento(codigo, tipo, Math.abs(cantidad));
            }
        } catch (SQLException e) {}
    }

    private void registrarMovimiento(String codigo, String tipo, int cant) {
        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement("INSERT INTO inventario_movimientos (producto_codigo, tipo, cantidad) VALUES (?, ?, ?)")) {
            ps.setString(1, codigo);
            ps.setString(2, tipo);
            ps.setInt(3, cant);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }

    public void verMovimientos() {
        try (Connection con = Conexion.getInstancia().conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM inventario_movimientos ORDER BY fecha DESC")) {
            System.out.println("--- HISTORIAL DE MOVIMIENTOS DE STOCK ---");
            while (rs.next()) {
                System.out.println("[" + rs.getTimestamp("fecha") + "] Prod: " + rs.getString("producto_codigo") + " | " + rs.getString("tipo") + " | Cantidad: " + rs.getInt("cantidad"));
            }
        } catch (SQLException e) {}
    }
}