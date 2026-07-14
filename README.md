# Sistema de E-Commerce - Proyecto Final Da Vinci

## Descripción
Plataforma de comercio electrónico desarrollada en Java aplicando Programación Orientada a Objetos, manejo de persistencia con MySQL y Patrones de Diseño. Cumple con la gestión de 4 roles, inventario, carritos de compra y procesamiento de pagos.

## Requisitos Previos
- Java Development Kit (JDK) 8 o superior.
- XAMPP (Apache y MySQL encendidos).
- Librería mysql-connector-java.jar (incluida en la carpeta /lib).

## Instrucciones de Ejecución
1. Encender el módulo **MySQL** desde el Panel de Control de XAMPP.
2. Abrir una terminal en el directorio raíz del proyecto.
3. Compilar el proyecto:
   javac -encoding UTF-8 -cp "lib\mysql-connector-java.jar" -d bin -sourcepath src src\com\ecommerce\main\Main.java
4. Ejecutar el proyecto:
   java -cp "bin;lib\mysql-connector-java.jar" com.ecommerce.main.Main

## Accesos Rápidos (Testing)
El sistema asigna roles automáticamente al registrar los siguientes correos:
- **Administrador:** admin@admin.com
- **Operador de Ventas:** ventas@admin.com
- **Responsable Logística:** logistica@admin.com
- **Cliente:** Cualquier otro correo.