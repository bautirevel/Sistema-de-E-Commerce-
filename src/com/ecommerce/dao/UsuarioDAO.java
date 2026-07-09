package com.ecommerce.dao;

import com.ecommerce.model.Usuario;
import java.util.List;

public interface UsuarioDAO {
    void insertar(Usuario usuario);
    Usuario buscarPorId(int id);
    List<Usuario> listarTodos();
    void actualizar(Usuario usuario);
    void eliminar(int id);
}