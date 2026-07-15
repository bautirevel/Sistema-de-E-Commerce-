package com.ecommerce.dao.impl;

import com.ecommerce.dao.UsuarioDAO;
import com.ecommerce.model.Usuario;
import com.ecommerce.model.Rol;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class UsuarioDAOImpl implements UsuarioDAO {
    private Conexion conexion = Conexion.getInstancia();

    public UsuarioDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS usuarios (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(50), apellido VARCHAR(50), email VARCHAR(100) UNIQUE, contrasena VARCHAR(50), fecha_alta TIMESTAMP DEFAULT CURRENT_TIMESTAMP, rol VARCHAR(30), estado BOOLEAN DEFAULT TRUE)");
        } catch (SQLException e) { System.out.println("Error creando tabla: " + e.getMessage()); }
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("ALTER TABLE usuarios ADD COLUMN fecha_alta TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        } catch (SQLException e) { /* la columna ya existe, se ignora */ }
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
                return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("email"), rs.getString("contrasena"), rs.getTimestamp("fecha_alta"), rs.getBoolean("estado"), Rol.valueOf(rs.getString("rol")));
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

    @Override
    public void actualizar(Usuario usuario) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("UPDATE usuarios SET nombre=?, apellido=?, email=?, rol=? WHERE id=?")) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getRol().name());
            ps.setInt(5, usuario.getId());
            int filas = ps.executeUpdate();
            if (filas > 0) System.out.println("Usuario actualizado correctamente.");
            else System.out.println("[ERROR]: No se encontro el usuario a modificar.");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("[ERROR]: El email ya se encuentra registrado por otro usuario.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public Usuario buscarPorId(int id) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM usuarios WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearUsuario(rs);
        } catch (SQLException e) {
            System.out.println("Error buscando usuario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM usuarios WHERE email = ?")) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearUsuario(rs);
        } catch (SQLException e) {
            System.out.println("Error buscando usuario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void activar(int id) {
        cambiarEstado(id, true);
    }

    @Override
    public void desactivar(int id) {
        cambiarEstado(id, false);
    }

    private void cambiarEstado(int id, boolean estado) {
        try (Connection con = conexion.conectar();
             PreparedStatement ps = con.prepareStatement("UPDATE usuarios SET estado=? WHERE id=?")) {
            ps.setBoolean(1, estado);
            ps.setInt(2, id);
            int filas = ps.executeUpdate();
            if (filas > 0) System.out.println("Usuario " + (estado ? "activado." : "desactivado."));
            else System.out.println("[ERROR]: No se encontro el usuario indicado.");
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado del usuario: " + e.getMessage());
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"),
                rs.getString("email"), rs.getString("contrasena"), rs.getTimestamp("fecha_alta"),
                rs.getBoolean("estado"), Rol.valueOf(rs.getString("rol")));
    }
}