package com.ecommerce.dao.impl;

import com.ecommerce.dao.ProductoDAO;
import com.ecommerce.model.Producto;
import com.ecommerce.utils.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {
    private Conexion conexion;

    public ProductoDAOImpl() {
        this.conexion = new Conexion();
    }

    @Override
    public void insertar(Producto producto) {
        Connection con = conexion.conectar();
        try {
            String sql = "INSERT INTO productos (codigo, nombre, precio) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "COD-TMP");
            ps.setString(2, "Producto Temporal");
            ps.setDouble(3, 0.0);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            conexion.desconectar();
        }
    }

    @Override
    public Producto buscarPorCodigo(String codigo) {
        return null;
    }

    @Override
    public List<Producto> listarTodos() {
        return new ArrayList<>();
    }

    @Override
    public void actualizar(Producto producto) {
    }

    @Override
    public void eliminar(String codigo) {
        Connection con = conexion.conectar();
        try {
            String sql = "DELETE FROM productos WHERE codigo = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            conexion.desconectar();
        }
    }
}
