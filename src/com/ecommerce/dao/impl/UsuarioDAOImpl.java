package com.ecommerce.dao.impl;

import com.ecommerce.dao.UsuarioDAO;
import com.ecommerce.model.Usuario;
import com.ecommerce.utils.Conexion;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements UsuarioDAO {
    //conexión a la bd
    private Conexion conexion;

    //constructor que da inicio a la conexión
    public UsuarioDAOImpl() {
        this.conexion = new Conexion();
    }

    @Override
    public void insertar(Usuario usuario) {
    }

    @Override
    public Usuario buscarPorId(int id) {
        return null;
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>();
    }

    @Override
    public void actualizar(Usuario usuario) {
    }

    @Override
    public void eliminar(int id) {
    }
}