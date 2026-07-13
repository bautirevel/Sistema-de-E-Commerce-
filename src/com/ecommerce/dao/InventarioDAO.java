package com.ecommerce.dao;
import com.ecommerce.exceptions.StockInsuficienteException;
public interface InventarioDAO {
    void restarStock(String codigoProducto, int cantidad) throws StockInsuficienteException;
}