package com.ecommerce.dao.impl;
import com.ecommerce.dao.CategoriaDAO;
import com.ecommerce.utils.Conexion;
import java.sql.*;

public class CategoriaDAOImpl implements CategoriaDAO {
    private Conexion conexion = Conexion.getInstancia();
    public CategoriaDAOImpl() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS categorias (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(50), descripcion VARCHAR(100), estado VARCHAR(20) DEFAULT 'ACTIVO')");
        } catch (SQLException e) {}
        // Si la tabla ya existia de una version anterior sin la columna 'estado', la agregamos.
        try (Connection con = conexion.conectar(); Statement st = con.createStatement()) {
            st.execute("ALTER TABLE categorias ADD COLUMN estado VARCHAR(20) DEFAULT 'ACTIVO'");
        } catch (SQLException e) { /* la columna ya existe, se ignora */ }
    }
    @Override
    public void insertar(String nombre, String descripcion) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)")) {
            ps.setString(1, nombre); ps.setString(2, descripcion); ps.executeUpdate(); System.out.println("Categoria creada.");
        } catch (SQLException e) {}
    }
    @Override
    public void listarTodos() {
        try (Connection con = conexion.conectar(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT * FROM categorias")) {
            while(rs.next()) System.out.println(rs.getInt("id") + ". " + rs.getString("nombre") + " - " + rs.getString("descripcion") + " [" + rs.getString("estado") + "]");
        } catch (SQLException e) {}
    }
    @Override
    public void actualizar(int id, String nombre, String descripcion) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("UPDATE categorias SET nombre=?, descripcion=? WHERE id=?")) {
            ps.setString(1, nombre); ps.setString(2, descripcion); ps.setInt(3, id);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Categoria actualizada." : "[ERROR]: No se encontro la categoria.");
        } catch (SQLException e) { System.out.println("Error al actualizar categoria: " + e.getMessage()); }
    }
    @Override
    public void eliminar(int id) {
        // Baja logica: no se borra el registro (podria tener productos asociados), se marca como INACTIVO.
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("UPDATE categorias SET estado='INACTIVO' WHERE id=?")) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Categoria dada de baja." : "[ERROR]: No se encontro la categoria.");
        } catch (SQLException e) { System.out.println("Error al dar de baja la categoria: " + e.getMessage()); }
    }
    @Override
    public void asociarProducto(String codigoProducto, String nombreCategoria) {
        try (Connection con = conexion.conectar(); PreparedStatement ps = con.prepareStatement("UPDATE productos SET categoria=? WHERE codigo=?")) {
            ps.setString(1, nombreCategoria); ps.setString(2, codigoProducto);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Producto asociado a la categoria " + nombreCategoria + "." : "[ERROR]: No se encontro el producto.");
        } catch (SQLException e) { System.out.println("Error al asociar producto: " + e.getMessage()); }
    }
}