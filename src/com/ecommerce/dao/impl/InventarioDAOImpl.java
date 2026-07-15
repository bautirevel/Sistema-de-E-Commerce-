package com.ecommerce.dao.impl;
import com.ecommerce.dao.InventarioDAO;
import com.ecommerce.exceptions.DatosInvalidosException;
import com.ecommerce.exceptions.ProductoNoEncontradoException;
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
    public void restarStock(String codigo, int cantidad) throws StockInsuficienteException, ProductoNoEncontradoException {
        alterarStock(codigo, -cantidad, "EGRESO");
    }

    @Override
    public void sumarStock(String codigo, int cantidad) throws ProductoNoEncontradoException {
        try {
            alterarStock(codigo, cantidad, "INGRESO");
        } catch (StockInsuficienteException e) {
            // no puede ocurrir con una cantidad positiva de ingreso
        }
    }

    @Override
    public int consultarStock(String codigo) {
        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement("SELECT stock FROM productos WHERE codigo = ?")) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("stock");
        } catch (SQLException e) {
            System.out.println("Error consultando stock: " + e.getMessage());
        }
        return -1;
    }

    @Override
    public void ajustarStock(String codigo, int nuevaCantidad) throws DatosInvalidosException, ProductoNoEncontradoException {
        if (nuevaCantidad < 0) {
            throw new DatosInvalidosException("El stock no puede quedar en un valor negativo.");
        }
        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement("UPDATE productos SET stock = ? WHERE codigo = ?")) {
            ps.setInt(1, nuevaCantidad);
            ps.setString(2, codigo);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new ProductoNoEncontradoException("No se encontro el producto con codigo " + codigo);
            }
            registrarMovimiento(codigo, "AJUSTE", nuevaCantidad);
        } catch (SQLException e) {
            System.out.println("Error al ajustar stock: " + e.getMessage());
        }
    }

    private void alterarStock(String codigo, int cantidad, String tipo) throws StockInsuficienteException, ProductoNoEncontradoException {
        try (Connection con = Conexion.getInstancia().conectar();
             PreparedStatement ps = con.prepareStatement("SELECT stock FROM productos WHERE codigo = ?")) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new ProductoNoEncontradoException("No se encontro el producto con codigo " + codigo);
            }
            int stockActual = rs.getInt("stock");
            if (stockActual + cantidad < 0) {
                throw new StockInsuficienteException("Stock insuficiente.");
            }
            try (PreparedStatement psUpdate = con.prepareStatement("UPDATE productos SET stock = stock + ? WHERE codigo = ?")) {
                psUpdate.setInt(1, cantidad);
                psUpdate.setString(2, codigo);
                psUpdate.executeUpdate();
            }
            registrarMovimiento(codigo, tipo, Math.abs(cantidad));
        } catch (SQLException e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());
        }
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

    @Override
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
