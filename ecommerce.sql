-- CREACIÓN DE LA BASE DE DATOS Y TABLAS PARA EL SISTEMA E-COMMERCE
-- NOTA: cada DAOImpl crea/migra sus propias tablas al arrancar (CREATE TABLE IF NOT EXISTS / ALTER TABLE),
-- asi que este script es de referencia / creacion limpia, no es obligatorio ejecutarlo a mano.
CREATE DATABASE IF NOT EXISTS ecommerce;
USE ecommerce;

CREATE TABLE IF NOT EXISTS usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    fecha_alta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rol VARCHAR(30) DEFAULT 'CLIENTE',
    estado BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS productos (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DOUBLE NOT NULL,
    categoria VARCHAR(50),
    stock INT NOT NULL DEFAULT 0,
    peso DOUBLE DEFAULT 0,
    estado VARCHAR(20) DEFAULT 'ACTIVO',
    tipo VARCHAR(20) DEFAULT 'FISICO'
);

CREATE TABLE IF NOT EXISTS categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(100),
    estado VARCHAR(20) DEFAULT 'ACTIVO'
);

CREATE TABLE IF NOT EXISTS inventario_movimientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_codigo VARCHAR(50),
    tipo VARCHAR(20),
    cantidad INT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ordenes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_email VARCHAR(100),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DOUBLE NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'CREADA',
    id_pago INT NULL,
    id_envio INT NULL
);

CREATE TABLE IF NOT EXISTS orden_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_orden INT,
    producto_codigo VARCHAR(20),
    cantidad INT,
    precio_unitario DOUBLE,
    subtotal DOUBLE
);

CREATE TABLE IF NOT EXISTS pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_orden INT,
    metodo VARCHAR(40),
    monto DOUBLE,
    estado VARCHAR(20),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS envios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_orden INT,
    codigo_seguimiento VARCHAR(50),
    direccion VARCHAR(150),
    provincia VARCHAR(60),
    ciudad VARCHAR(60),
    codigo_postal VARCHAR(20),
    tipo_envio VARCHAR(30),
    estado VARCHAR(30) DEFAULT 'PENDIENTE',
    costo DOUBLE DEFAULT 0
);

CREATE TABLE IF NOT EXISTS reclamos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    motivo VARCHAR(200) NOT NULL,
    estado VARCHAR(30) DEFAULT 'ABIERTO',
    cliente_email VARCHAR(100),
    id_orden INT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS devoluciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_codigo VARCHAR(50),
    motivo TEXT,
    fecha DATE,
    estado VARCHAR(30),
    cliente_email VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS calificaciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_codigo VARCHAR(50),
    estrellas INT,
    comentario TEXT
);

-- DATOS DE PRUEBA INICIALES
INSERT IGNORE INTO productos (codigo, nombre, descripcion, precio, categoria, stock, peso, estado, tipo) VALUES
('PROD-01', 'Notebook Gamer', 'Notebook de alta gama', 1500000.00, 'Tecnologia', 10, 2.5, 'ACTIVO', 'FISICO');
