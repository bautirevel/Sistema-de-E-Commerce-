package com.ecommerce.main;
import java.util.Scanner;
import com.ecommerce.dao.ProductoDAO;
import com.ecommerce.dao.impl.ProductoDAOImpl;
import com.ecommerce.model.Producto;
import com.ecommerce.model.ProductoFisico;
import com.ecommerce.model.EstadoProducto;

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
                System.out.print("Ingrese el codigo (ej. PF-01): ");
                String codigo = scanner.nextLine();
                System.out.print("Ingrese el nombre: ");
                String nombre = scanner.nextLine();
                System.out.print("Ingrese el precio base: ");
                double precio = scanner.nextDouble();
                scanner.nextLine();

                // Instanciamos TU clase hija (ProductoFisico) y usamos tu enum EstadoProducto
                Producto nuevoProducto = new ProductoFisico(
                    codigo, 
                    nombre, 
                    "Sin descripcion", 
                    precio, 
                    "General", 
                    10,                     // stock
                    1.5,                    // peso
                    EstadoProducto.ACTIVO,  // estado
                    500.0                   // costo de envio
                );
                
                System.out.println("Procesando tu objeto...");
                productoDAO.insertar(nuevoProducto);
                System.out.println("¡Exito!");

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