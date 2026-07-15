package com.ecommerce.dao;

import com.ecommerce.model.Envio;
import com.ecommerce.model.EstadoEnvio;
import com.ecommerce.exceptions.EnvioNoEncontradoException;
import java.util.List;

public interface EnvioDAO {
    Envio guardar(Envio envio);
    Envio buscarPorId(int id) throws EnvioNoEncontradoException;
    List<Envio> obtenerTodos();
    void actualizarEstado(int id, EstadoEnvio nuevoEstado) throws EnvioNoEncontradoException;
    int contarPorEstado(EstadoEnvio estado);
}
