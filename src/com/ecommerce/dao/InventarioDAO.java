package com.ecommerce.dao;
import com.ecommerce.exceptions.DatosInvalidosException;
import com.ecommerce.exceptions.StockInsuficienteException;

public interface InventarioDAO {
    void restarStock(String codigoProducto, int cantidad) throws StockInsuficienteException;
    void sumarStock(String codigoProducto, int cantidad);
    void ajustarStock(String codigoProducto, int nuevaCantidad) throws DatosInvalidosException;
    int consultarStock(String codigoProducto);
    void verMovimientos();
}
