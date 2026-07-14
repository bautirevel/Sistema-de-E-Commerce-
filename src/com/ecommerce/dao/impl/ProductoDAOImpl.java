package com.ecommerce.dao.impl;
import com.ecommerce.dao.ProductoDAO;
import com.ecommerce.model.*;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class ProductoDAOImpl implements ProductoDAO {
    private Conexion conexion = Conexion.getInstancia();
    public ProductoDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS productos (codigo VARCHAR(20) PRIMARY KEY, nombre VARCHAR(50), precio DOUBLE, stock INT, categoria VARCHAR(30))");
        } catch (SQLException e) {}
    }
    @Override
    public void insertar(Producto producto) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("INSERT INTO productos VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, producto.getCodigo()); ps.setString(2, producto.getNombre());
            ps.setDouble(3, producto.getPrecio()); ps.setInt(4, producto.getStock()); ps.setString(5, producto.getCategoria());
            ps.executeUpdate(); System.out.println("Producto guardado en BD.");
        } catch (SQLException e) { System.out.println("Error: " + e.getMessage()); }
    }
    @Override
    public void listarTodos() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM productos")) {
            System.out.println("--- CATALOGO DE PRODUCTOS ---");
            while(rs.next()) System.out.println("[" + rs.getString("codigo") + "] " + rs.getString("nombre") + " - $" + rs.getDouble("precio") + " (Stock: " + rs.getInt("stock") + ")");
        } catch (SQLException e) {}
    }
    @Override
    public void eliminar(String codigo) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("DELETE FROM productos WHERE codigo=?")) {
            ps.setString(1, codigo); ps.executeUpdate(); System.out.println("Producto eliminado.");
        } catch (SQLException e) {}
    }
    @Override
    public Producto buscarPorCodigo(String codigo) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("SELECT * FROM productos WHERE codigo=?")) {
            ps.setString(1, codigo); ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Instanciamos como ProductoFisico por defecto para el carrito
                return new ProductoFisico(rs.getString("codigo"), rs.getString("nombre"), rs.getDouble("precio"), rs.getInt("stock"));
            }
        } catch (SQLException e) {}
        return null;
    }
}