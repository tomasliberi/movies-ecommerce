# E-commerce de Peliculas

Pruebas en Insomnia o Postman

## Codigos HTTP

- `200 OK`
- `201 Created`
- `204 No Content`
- `400 Bad Request`
- `404 Not Found`

## Base URL

```text
http://localhost:8080/productos
```

## Crear pelicula

`POST http://localhost:8080/productos`

Pegar este JSON para probar:

```json
{
  "nombre": "Interstellar",
  "descripcion": "Pelicula de ciencia ficcion dirigida por Christopher Nolan",
  "precio": 15.99,
  "stock": 8
}
```

Posibles respuestas:

- `201 Created`
- `400 Bad Request` si el body esta mal enviado

## Consultar peliculas

`GET http://localhost:8080/productos`

Posibles respuestas:

- `200 OK`

## Consultar pelicula por ID

`GET http://localhost:8080/productos/1`

Posibles respuestas:

- `200 OK`
- `404 Not Found` si no existe el id

## Actualizar pelicula

`PUT http://localhost:8080/productos/1`

Pegar este JSON para probar:

```json
{
  "nombre": "Interstellar Edicion Especial",
  "descripcion": "Blu-ray coleccionable",
  "precio": 19.99,
  "stock": 5
}
```

Posibles respuestas:

- `200 OK`
- `404 Not Found` si no existe el id
- `400 Bad Request` si el body esta mal enviado

## Eliminar pelicula

`DELETE http://localhost:8080/productos/1`

Posibles respuestas:

- `204 No Content`
- `404 Not Found` si no existe el id

## Base de datos

- Base SQL: `H2`
- Tipo: persistente en archivo local
- URL: `jdbc:h2:file:./data/moviesdb`

## Scripts SQL

- `src/main/resources/schema.sql`: crea la tabla `producto`
- `src/main/resources/data.sql`: carga datos iniciales

## Dependencias usadas

- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `h2`
- `spring-boot-starter-test`

## ORM

Se usa JPA/Hibernate para mapear la clase `Producto` con la tabla `producto`.

## Ejecutar el proyecto

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```
