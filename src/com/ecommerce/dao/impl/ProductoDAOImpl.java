package com.ecommerce.dao.impl;
import com.ecommerce.dao.ProductoDAO;
import com.ecommerce.model.Producto;
import com.ecommerce.utils.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {
    private Conexion conexion = new Conexion();

    @Override
    public void insertar(Producto producto) {
        Connection con = conexion.conectar();
        if (con == null) return;
        try {
            String sql = "INSERT INTO productos (codigo, nombre, precio) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, producto.getCodigoUnico());
            ps.setString(2, producto.getNombre());
            ps.setDouble(3, producto.getPrecioBase());
            
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en BD: " + e.getMessage());
        } finally {
            conexion.desconectar();
        }
    }

    @Override public Producto buscarPorCodigo(String codigo) { return null; }

    @Override
    public List<Producto> listarTodos() {
        Connection con = conexion.conectar();
        if (con == null) return new ArrayList<>();
        try {
            String sql = "SELECT * FROM productos";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n--- TU BASE DE DATOS REAL ---");
            while (rs.next()) {
                System.out.println("Cod: " + rs.getString("codigo") + " | " + rs.getString("nombre") + " | $" + rs.getDouble("precio"));
            }
            System.out.println("-----------------------------\n");
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en BD: " + e.getMessage());
        } finally {
            conexion.desconectar();
        }
        return new ArrayList<>();
    }

    @Override public void actualizar(Producto producto) {}
    @Override public void eliminar(String codigo) {}
}