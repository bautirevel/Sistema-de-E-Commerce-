package com.ecommerce.dao;
import com.ecommerce.exceptions.DatosInvalidosException;
import com.ecommerce.exceptions.ProductoNoEncontradoException;
import com.ecommerce.exceptions.StockInsuficienteException;

public interface InventarioDAO {
    void restarStock(String codigoProducto, int cantidad) throws StockInsuficienteException, ProductoNoEncontradoException;
    void sumarStock(String codigoProducto, int cantidad) throws ProductoNoEncontradoException;
    void ajustarStock(String codigoProducto, int nuevaCantidad) throws DatosInvalidosException, ProductoNoEncontradoException;
    int consultarStock(String codigoProducto);
    void verMovimientos();
}
