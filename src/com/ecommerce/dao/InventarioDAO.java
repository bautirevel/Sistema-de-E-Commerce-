package com.ecommerce.dao;
public interface InventarioDAO {
    void restarStock(String codigoProducto, int cantidad);
}