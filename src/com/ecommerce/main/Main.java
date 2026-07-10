package com.ecommerce.main;
import com.ecommerce.dao.ProductoDAO;
import com.ecommerce.dao.impl.ProductoDAOImpl;
import com.ecommerce.exceptions.DatosInvalidosException;
import com.ecommerce.exceptions.ProductoDuplicadoException;
import com.ecommerce.model.EstadoProducto;
import com.ecommerce.model.Producto;
import com.ecommerce.model.ProductoFisico;
import com.ecommerce.utils.Validador;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        ProductoDAO productoDAO = new ProductoDAOImpl();

        while (!salir) {
            System.out.println("\n=== SISTEMA E-COMMERCE ===");
            System.out.println("1. Ingresar Producto Fisico a mano");
            System.out.println("2. Ver Productos en BD");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            if (opcion == 1) {
            try {
            System.out.print("Ingrese el codigo: ");
            String codigo = scanner.nextLine();
            Validador.validarCadena(codigo, "Codigo");
            Validador.validarProductoDuplicado(codigo, productoDAO);

            System.out.print("Ingrese el nombre: ");
            String nombre = scanner.nextLine();
            Validador.validarCadena(nombre, "Nombre");

            System.out.print("Ingrese el precio: ");
            double precio = scanner.nextDouble();
            Validador.validarPrecio(precio);

            System.out.print("Ingrese el stock: ");
            int stock = scanner.nextInt();
            Validador.validarStock(stock);
            scanner.nextLine();

            Producto nuevoProducto = new ProductoFisico(codigo, nombre, "Sin desc", precio, "General", stock, 1.5, EstadoProducto.ACTIVO, 500.0);
            productoDAO.insertar(nuevoProducto);
            System.out.println("¡Exito!");

    } catch (DatosInvalidosException | ProductoDuplicadoException e) {
        System.out.println("Error: " + e.getMessage());
    }
} else if (opcion == 2) {
                productoDAO.listarTodos();
            } else if (opcion == 3) {
                salir = true;
                System.out.println("Saliendo...");
            } else {
                System.out.println("Opcion invalida.");
            }
        }
        scanner.close();
    }
}