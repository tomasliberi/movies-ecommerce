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

Otras rutas disponibles:

```text
http://localhost:8080/categorias
http://localhost:8080/usuarios
```

## Crear pelicula

`POST http://localhost:8080/productos`

Pegar este JSON para probar:

```json
{
  "nombre": "Interstellar",
  "descripcion": "Pelicula de ciencia ficcion dirigida por Christopher Nolan",
  "precio": 15.99,
  "stock": 8,
  "imagenUrl": "https://image.tmdb.org/t/p/w500/interstellar.jpg"
}
```

Posibles respuestas:

- `201 Created`
- `400 Bad Request` si el body esta mal enviado

## Consultar peliculas

`GET http://localhost:8080/productos`

Posibles respuestas:

- `200 OK`

## Buscar y filtrar peliculas

`GET http://localhost:8080/productos?nombre=dark`

`GET http://localhost:8080/productos?categoriaId=1`

`GET http://localhost:8080/productos?precioMin=10&precioMax=20`

`GET http://localhost:8080/productos?disponibles=true`

## Crear categoria

`POST http://localhost:8080/categorias`

Pegar este JSON para probar:

```json
{
  "nombre": "Drama",
  "descripcion": "Peliculas dramaticas"
}
```

## Consultar categorias

`GET http://localhost:8080/categorias`

## Crear usuario

`POST http://localhost:8080/usuarios`

Pegar este JSON para probar:

```json
{
  "username": "ana.movies",
  "nombre": "Ana",
  "apellido": "Gomez",
  "email": "ana@movies.com",
  "password": "1234",
  "rol": "COMPRADOR",
  "direccion": "Av. Siempre Viva 123"
}
```

## Consultar usuarios

`GET http://localhost:8080/usuarios`

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

- `src/main/resources/schema.sql`: crea las tablas `categoria`, `usuario`, `producto`, `carrito`, `item_carrito`, `orden` y `detalle_orden`
- `src/main/resources/data.sql`: carga datos iniciales y relaciones de ejemplo

## Dependencias usadas

- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `h2`
- `spring-boot-starter-test`

## ORM

Se usa JPA/Hibernate para mapear las entidades principales del ecommerce:

- `Producto` pertenece a una `Categoria`
- `Producto` incluye `imagenUrl` para mostrar la fotografia
- `Carrito` pertenece a un `Usuario`
- `ItemCarrito` relaciona `Carrito` con `Producto`
- `Orden` pertenece a un `Usuario`
- `DetalleOrden` relaciona `Orden` con `Producto`
- `Usuario` incluye `username` y `rol` para distinguir comprador, vendedor o admin

## Ejecutar el proyecto

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```
