package com.ecommerce.dao.impl;
import com.ecommerce.dao.UsuarioDAO;
import com.ecommerce.model.Usuario;
import com.ecommerce.utils.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {
    private Conexion conexion = new Conexion();

    @Override
    public void insertar(Usuario usuario) {
        Connection con = conexion.conectar();
        if (con == null) return;
        try {
            String sql = "INSERT INTO usuarios (nombre, apellido, email) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getEmail());
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en BD: " + e.getMessage());
        } finally {
            conexion.desconectar();
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        Connection con = conexion.conectar();
        if (con == null) return new ArrayList<>();
        try {
            String sql = "SELECT * FROM usuarios";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n--- LISTA DE USUARIOS EN BD ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | " + rs.getString("nombre") + " " + rs.getString("apellido") + " | " + rs.getString("email"));
            }
            System.out.println("-------------------------------\n");
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en BD: " + e.getMessage());
        } finally {
            conexion.desconectar();
        }
        return new ArrayList<>();
    }

    @Override public Usuario buscarPorId(int id) { return null; }
    @Override public void actualizar(Usuario usuario) {}
    @Override public void eliminar(int id) {}
    @Override public void activar(int id) {}
    @Override public void desactivar(int id) {}
}