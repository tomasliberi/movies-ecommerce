# E-commerce de Peliculas

API REST desarrollada con Spring Boot para la gestion de usuarios, categorias, productos, carrito y ordenes, con persistencia en MySQL y seguridad con JWT.

## Tecnologias

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT (`jjwt`)
- MySQL
- H2 para tests

## Base URL

```text
http://localhost:8080
```

## Codigos HTTP frecuentes

- `200 OK`
- `201 Created`
- `204 No Content`
- `400 Bad Request`
- `401 Unauthorized`
- `403 Forbidden`
- `404 Not Found`
- `409 Conflict`

## Seguridad

La API usa autenticacion stateless con JWT.

- `POST /auth/register` permite registrar usuarios
- `POST /auth/login` autentica por username o email y devuelve token
- el token se envia en el header `Authorization: Bearer <token>`
- las contrasenas se almacenan con `BCrypt`

### Roles

- `COMPRADOR`
- `VENDEDOR`
- `ADMIN`

### Reglas actuales

- `GET /productos` y `GET /categorias`: publicos
- `POST` y `PUT` sobre productos/categorias: `ADMIN` o `VENDEDOR`
- `DELETE` sobre productos/categorias: `ADMIN`
- `/auth/**`: publico

## Endpoints principales

### Registro

`POST /auth/register`

```json
{
  "username": "vendedor1",
  "nombre": "Juan",
  "apellido": "Perez",
  "email": "juan@movies.com",
  "password": "123456",
  "rol": "VENDEDOR",
  "direccion": "Calle 123"
}
```

Respuesta esperada:

```json
{
  "token": "jwt",
  "tipo": "Bearer",
  "usuarioId": 1,
  "username": "vendedor1",
  "rol": "VENDEDOR"
}
```

### Login

`POST /auth/login`

```json
{
  "usernameOrEmail": "vendedor1",
  "password": "123456"
}
```

Tambien se puede usar el email:

```json
{
  "usernameOrEmail": "juan@movies.com",
  "password": "123456"
}
```

### Crear producto

`POST /productos`

Requiere `Bearer Token` de un usuario `VENDEDOR` o `ADMIN`.

```json
{
  "nombre": "Interstellar",
  "descripcion": "Pelicula de ciencia ficcion dirigida por Christopher Nolan",
  "precio": 15.99,
  "stock": 8,
  "imagenUrl": "https://image.tmdb.org/t/p/w500/interstellar.jpg",
  "categoria": {
    "id": 1
  }
}
```

### Consultar productos

`GET /productos`

### Buscar y filtrar productos

Ejemplos:

```text
GET /productos?nombre=dark
GET /productos?categoriaId=1
GET /productos?precioMin=10&precioMax=20
GET /productos?disponibles=true
```

### Categorias

- `GET /categorias`
- `GET /categorias/{id}`
- `POST /categorias`
- `PUT /categorias/{id}`
- `DELETE /categorias/{id}`

Ejemplo de alta:

```json
{
  "nombre": "Drama",
  "descripcion": "Peliculas dramaticas"
}
```

### Usuarios

- `GET /usuarios`
- `GET /usuarios/{id}`
- `POST /usuarios`
- `PUT /usuarios/{id}`
- `DELETE /usuarios/{id}`

Ejemplo:

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

## Como probar en Insomnia o Postman

1. Registrar un usuario con `POST /auth/register`
2. Copiar el valor de `token` de la respuesta
3. En una request protegida, ir a `Auth > Bearer Token`
4. Pegar el token sin comillas
5. Enviar la request

Pruebas sugeridas:

- `GET /productos` sin token: debe funcionar
- `POST /productos` sin token: debe bloquear
- `POST /productos` con `COMPRADOR`: debe devolver `403`
- `POST /productos` con `VENDEDOR` o `ADMIN`: debe crear

## Base de datos

- Motor principal: `MySQL`
- Base por defecto: `moviesdb`
- URL por defecto:

```text
jdbc:mysql://localhost:3306/moviesdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Argentina/Buenos_Aires
```

### Variables de entorno opcionales

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS`

## Scripts SQL

- [schema.sql](c:/Users/Usuario/Desktop/movies/src/main/resources/schema.sql): crea las tablas `categoria`, `usuario`, `producto`, `carrito`, `item_carrito`, `orden` y `detalle_orden`
- [data.sql](c:/Users/Usuario/Desktop/movies/src/main/resources/data.sql): carga datos iniciales

## Modelo de datos

Relaciones principales:

- `Producto` pertenece a una `Categoria`
- `Carrito` pertenece a un `Usuario`
- `ItemCarrito` relaciona `Carrito` con `Producto`
- `Orden` pertenece a un `Usuario`
- `DetalleOrden` relaciona `Orden` con `Producto`
- `Usuario` tiene `rol` para distinguir permisos

## Ejecutar el proyecto

Si hace falta, crear la base:

```sql
CREATE DATABASE moviesdb;
```

Ejecutar en Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Tests

Los tests usan H2 en memoria para no depender de una instancia local de MySQL.

Ejecutar:

```powershell
.\mvnw.cmd test
```
