INSERT INTO categoria (nombre, descripcion)
VALUES ('Ciencia Ficcion', 'Peliculas futuristas y de exploracion espacial')
ON DUPLICATE KEY UPDATE nombre = nombre;

INSERT INTO categoria (nombre, descripcion)
VALUES ('Accion', 'Peliculas con ritmo dinamico y escenas intensas')
ON DUPLICATE KEY UPDATE nombre = nombre;

INSERT INTO usuario (email, username, nombre, apellido, password, rol, direccion)
VALUES ('admin@movies.com', 'tliberi', 'Tomas', 'Liberi', '1234', 'ADMIN', 'Calle Principal 123')
ON DUPLICATE KEY UPDATE email = email;

INSERT INTO producto (nombre, descripcion, precio, stock, imagen_url, categoria_id)
VALUES (
    'Inception',
    'Pelicula de ciencia ficcion y suspenso',
    14.99,
    10,
    'https://image.tmdb.org/t/p/w500/inception.jpg',
    (SELECT id FROM categoria WHERE nombre = 'Ciencia Ficcion')
)
ON DUPLICATE KEY UPDATE nombre = nombre;

INSERT INTO producto (nombre, descripcion, precio, stock, imagen_url, categoria_id)
VALUES (
    'The Dark Knight',
    'Pelicula de accion y superheroes',
    12.50,
    7,
    'https://image.tmdb.org/t/p/w500/dark-knight.jpg',
    (SELECT id FROM categoria WHERE nombre = 'Accion')
)
ON DUPLICATE KEY UPDATE nombre = nombre;

INSERT INTO carrito (usuario_id)
VALUES ((SELECT id FROM usuario WHERE email = 'admin@movies.com'))
ON DUPLICATE KEY UPDATE usuario_id = usuario_id;

INSERT INTO orden (usuario_id, fecha, total, estado)
VALUES (
    (SELECT id FROM usuario WHERE email = 'admin@movies.com'),
    DATE '2026-04-01',
    27.49,
    'CONFIRMADA'
)
ON DUPLICATE KEY UPDATE estado = estado;

INSERT INTO item_carrito (carrito_id, producto_id, cantidad)
VALUES (
    (SELECT id FROM carrito WHERE usuario_id = (SELECT id FROM usuario WHERE email = 'admin@movies.com')),
    (SELECT id FROM producto WHERE nombre = 'Inception'),
    1
)
ON DUPLICATE KEY UPDATE cantidad = cantidad;

INSERT INTO detalle_orden (orden_id, producto_id, cantidad, precio_unitario)
VALUES (
    (SELECT id FROM orden WHERE usuario_id = (SELECT id FROM usuario WHERE email = 'admin@movies.com') AND estado = 'CONFIRMADA'),
    (SELECT id FROM producto WHERE nombre = 'The Dark Knight'),
    2,
    12.50
)
ON DUPLICATE KEY UPDATE precio_unitario = precio_unitario;
