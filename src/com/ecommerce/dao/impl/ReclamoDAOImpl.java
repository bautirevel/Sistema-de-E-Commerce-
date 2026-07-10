package com.ecommerce.dao.impl;
import com.ecommerce.dao.ReclamoDAO;
import com.ecommerce.utils.Conexion;
public class ReclamoDAOImpl implements ReclamoDAO {
    private Conexion conexion = new Conexion();
    @Override public void abrirReclamo() { System.out.println("Reclamo registrado en MySQL."); }
    @Override public void buscarReclamo() {}
    @Override public void actualizarEstado() {}
}