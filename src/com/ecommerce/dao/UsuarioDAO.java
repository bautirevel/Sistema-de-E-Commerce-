package com.ecommerce.dao;
import com.ecommerce.model.Usuario;
public interface UsuarioDAO {
    void insertar(Usuario usuario);
    void listarTodos();
    void eliminar(int id);
    Usuario login(String email, String password);
}