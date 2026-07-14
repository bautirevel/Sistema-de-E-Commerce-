package com.ecommerce.dao;
import com.ecommerce.model.Usuario;
public interface UsuarioDAO {
    void insertar(Usuario usuario);
    void listarTodos();
    void eliminar(int id);
    Usuario login(String email, String password);
    void actualizar(Usuario usuario);
    Usuario buscarPorId(int id);
    Usuario buscarPorEmail(String email);
    void activar(int id);
    void desactivar(int id);
}