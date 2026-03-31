MERGE INTO categoria (nombre, descripcion)
KEY(nombre)
VALUES ('Ciencia Ficcion', 'Peliculas futuristas y de exploracion espacial');

MERGE INTO categoria (nombre, descripcion)
KEY(nombre)
VALUES ('Accion', 'Peliculas con ritmo dinamico y escenas intensas');

MERGE INTO usuario (email, username, nombre, apellido, password, rol, direccion)
KEY(email)
VALUES ('admin@movies.com', 'tliberi', 'Tomas', 'Liberi', '1234', 'ADMIN', 'Calle Principal 123');

MERGE INTO producto (nombre, descripcion, precio, stock, imagen_url, categoria_id)
KEY(nombre)
VALUES (
    'Inception',
    'Pelicula de ciencia ficcion y suspenso',
    14.99,
    10,
    'https://image.tmdb.org/t/p/w500/inception.jpg',
    (SELECT id FROM categoria WHERE nombre = 'Ciencia Ficcion')
);

MERGE INTO producto (nombre, descripcion, precio, stock, imagen_url, categoria_id)
KEY(nombre)
VALUES (
    'The Dark Knight',
    'Pelicula de accion y superheroes',
    12.50,
    7,
    'https://image.tmdb.org/t/p/w500/dark-knight.jpg',
    (SELECT id FROM categoria WHERE nombre = 'Accion')
);

MERGE INTO carrito (usuario_id)
KEY(usuario_id)
VALUES ((SELECT id FROM usuario WHERE email = 'admin@movies.com'));

MERGE INTO orden (usuario_id, fecha, total, estado)
KEY(usuario_id, fecha, estado)
VALUES (
    (SELECT id FROM usuario WHERE email = 'admin@movies.com'),
    DATE '2026-04-01',
    27.49,
    'CONFIRMADA'
);

MERGE INTO item_carrito (carrito_id, producto_id, cantidad)
KEY(carrito_id, producto_id)
VALUES (
    (SELECT id FROM carrito WHERE usuario_id = (SELECT id FROM usuario WHERE email = 'admin@movies.com')),
    (SELECT id FROM producto WHERE nombre = 'Inception'),
    1
);

MERGE INTO detalle_orden (orden_id, producto_id, cantidad, precio_unitario)
KEY(orden_id, producto_id)
VALUES (
    (SELECT id FROM orden WHERE usuario_id = (SELECT id FROM usuario WHERE email = 'admin@movies.com') AND estado = 'CONFIRMADA'),
    (SELECT id FROM producto WHERE nombre = 'The Dark Knight'),
    2,
    12.50
);
