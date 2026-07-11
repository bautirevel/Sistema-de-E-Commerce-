package com.ecommerce.dao;
public interface EnvioDAO {
    void registrarEnvio(int idOrden, String direccion);
    void rastrearEnvio(int idEnvio);
}