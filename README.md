# E-commerce de Peliculas

API REST desarrollada con Spring Boot para la gestion de usuarios, categorias, productos, carrito y ordenes, con persistencia en MySQL y seguridad con JWT.


## Base URL

```text
http://localhost:8080
```


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
