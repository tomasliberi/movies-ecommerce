MERGE INTO producto (nombre, descripcion, precio, stock)
KEY(nombre)
VALUES ('Inception', 'Pelicula de ciencia ficcion y suspenso', 14.99, 10);

MERGE INTO producto (nombre, descripcion, precio, stock)
KEY(nombre)
VALUES ('The Dark Knight', 'Pelicula de accion y superheroes', 12.50, 7);
