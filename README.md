# Sistema de E-Commerce - Final Programación Avanzada

## Descripción
Plataforma de E-Commerce por consola desarrollada en Java para la gestión del ciclo de vida completo de compras online. El sistema persistirá la información requerida y utiliza múltiples patrones de diseño arquitectónicos.

## Requisitos
- Java JDK 11 o superior
- Conector JDBC para SQLite

## Cómo levantar el proyecto
1. Clonar el repositorio.
2. Añadir el driver JDBC de SQLite al Build Path del proyecto.
3. Compilar y ejecutar la clase `Main.java` ubicada en el paquete principal.
4. Interactuar con el sistema a través de las 13 opciones del menú por consola.

## Desarrolladores
- Bauti Revel
- Theo
2. Para tu archivo Fundamentacion_Diseno.txt: Fijate en el explorador de Visual Studio Code si ese archivo también está vacío. Si lo está, abrilo, copiá y pegá esto adentro y guardalo:
FUNDAMENTACIÓN DE PATRONES DE DISEÑO UTILIZADOS

De acuerdo con los requerimientos del examen final integrador, se aplicaron los siguientes patrones arquitectónicos:

1. Patrón DAO (Data Access Object):
Se utilizó de manera obligatoria para toda la capa de persistencia. Esto permite separar completamente la lógica de negocio de las consultas SQL, encapsulando el acceso a la base de datos SQLite y mejorando la escalabilidad del sistema.

2. Patrón Factory Method / Abstract Factory:
Se implementó una fábrica (SqliteDAOFactory) para la creación de los DAOs. Esta decisión de diseño permite instanciar los objetos de acceso a datos sin usar el operador 'new' directamente en la lógica del negocio, reduciendo el acoplamiento.

3. Patrón Strategy:
Se aplicó obligatoriamente en el módulo de procesamiento de pagos a través de la interfaz 'ProcesadorPago'. Esto permitió encapsular el algoritmo de cada medio de pago (Tarjeta de Crédito, Transferencia, Billetera Virtual, etc.) en clases independientes, utilizando el polimorfismo y evitando por completo el uso de estructuras de control 'if/else' o 'switch'.
