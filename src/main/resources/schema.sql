CREATE TABLE IF NOT EXISTS categoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(255),
    direccion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    precio DOUBLE PRECISION NOT NULL,
    stock INTEGER NOT NULL,
    imagen_url VARCHAR(255),
    categoria_id BIGINT,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

CREATE TABLE IF NOT EXISTS carrito (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_carrito_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE IF NOT EXISTS orden (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    total DOUBLE PRECISION NOT NULL,
    estado VARCHAR(255) NOT NULL,
    CONSTRAINT fk_orden_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT uk_orden_usuario_fecha_estado UNIQUE (usuario_id, fecha, estado)
);

CREATE TABLE IF NOT EXISTS item_carrito (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrito_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    CONSTRAINT fk_item_carrito_carrito FOREIGN KEY (carrito_id) REFERENCES carrito(id),
    CONSTRAINT fk_item_carrito_producto FOREIGN KEY (producto_id) REFERENCES producto(id),
    CONSTRAINT uk_item_carrito_carrito_producto UNIQUE (carrito_id, producto_id)
);

CREATE TABLE IF NOT EXISTS detalle_orden (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    orden_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_detalle_orden_orden FOREIGN KEY (orden_id) REFERENCES orden(id),
    CONSTRAINT fk_detalle_orden_producto FOREIGN KEY (producto_id) REFERENCES producto(id),
    CONSTRAINT uk_detalle_orden_orden_producto UNIQUE (orden_id, producto_id)
);
