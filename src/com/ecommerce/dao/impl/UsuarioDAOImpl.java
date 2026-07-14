package com.ecommerce.dao.impl;

import com.ecommerce.dao.UsuarioDAO;
import com.ecommerce.model.Usuario;
import com.ecommerce.model.Rol;
import com.ecommerce.utils.Conexion;
import java.sql.*;
import java.util.Date;

public class UsuarioDAOImpl implements UsuarioDAO {
    private Conexion conexion = Conexion.getInstancia();

    public UsuarioDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS usuarios (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(50), apellido VARCHAR(50), email VARCHAR(100) UNIQUE, contrasena VARCHAR(50), rol VARCHAR(30), estado BOOLEAN DEFAULT TRUE)");
        } catch (SQLException e) { System.out.println("Error creando tabla: " + e.getMessage()); }
    }

    @Override
    public void insertar(Usuario usuario) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("INSERT INTO usuarios (nombre, apellido, email, contrasena, rol) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getContrasena());
            ps.setString(5, usuario.getRol().name());
            ps.executeUpdate();
            System.out.println("Â¡Usuario registrado en la base de datos correctamente!");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("[ERROR]: El email ya se encuentra registrado.");
        } catch (SQLException e) { 
            System.out.println("Error al insertar usuario: " + e.getMessage()); 
        }
    }

    @Override
    public Usuario login(String email, String pass) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM usuarios WHERE email = ? AND contrasena = ? AND estado = true")) {
            ps.setString(1, email);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("email"), rs.getString("contrasena"), new Date(), rs.getBoolean("estado"), Rol.valueOf(rs.getString("rol")));
            }
        } catch (SQLException e) { 
            System.out.println("Error de BD en el login: " + e.getMessage()); 
        }
        return null;
    }

    @Override
    public void listarTodos() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM usuarios")) {
            while(rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | " + rs.getString("nombre") + " " + rs.getString("apellido") + " | Rol: " + rs.getString("rol"));
            }
        } catch (SQLException e) {}
    }

    @Override
    public void eliminar(int id) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("DELETE FROM usuarios WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
}