package com.liberi.movies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.liberi.movies.repository.CarritoRepository;
import com.liberi.movies.repository.CategoriaRepository;
import com.liberi.movies.repository.DetalleOrdenRepository;
import com.liberi.movies.repository.ItemCarritoRepository;
import com.liberi.movies.repository.OrdenRepository;
import com.liberi.movies.repository.ProductoRepository;
import com.liberi.movies.repository.UsuarioRepository;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoviesApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeEach
    void limpiarRepositorio() {
        detalleOrdenRepository.deleteAll();
        itemCarritoRepository.deleteAll();
        ordenRepository.deleteAll();
        carritoRepository.deleteAll();
        productoRepository.deleteAll();
        usuarioRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void deberiaCrearYListarProductos() throws Exception {
        String registerBody = """
                {
                  "username": "vendedor",
                  "nombre": "Vale",
                  "apellido": "Paz",
                  "email": "vendedor@movies.com",
                  "password": "secreto123",
                  "rol": "VENDEDOR",
                  "direccion": "Av Siempre Viva 742"
                }
                """;

        HttpResponse<String> registerResponse = httpClient.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl() + "/auth/register"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(registerBody))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(HttpStatus.CREATED.value(), registerResponse.statusCode());
        String token = extraerToken(registerResponse.body());

        String body = """
                {
                  "nombre": "Combo cine",
                  "descripcion": "Entrada y pochoclos",
                  "precio": 12.5,
                  "stock": 30
                }
                """;

        HttpResponse<String> postResponse = httpClient.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl() + "/productos"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(HttpStatus.CREATED.value(), postResponse.statusCode());
        assertTrue(postResponse.body().contains("\"nombre\":\"Combo cine\""));
        assertTrue(postResponse.body().contains("\"id\":"));

        HttpResponse<String> getResponse = httpClient.send(
                HttpRequest.newBuilder()
                        .uri(URI.create(baseUrl() + "/productos"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(HttpStatus.OK.value(), getResponse.statusCode());
        assertTrue(getResponse.body().contains("\"nombre\":\"Combo cine\""));
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private String extraerToken(String responseBody) {
        String marker = "\"token\":\"";
        int start = responseBody.indexOf(marker);
        int end = responseBody.indexOf("\"", start + marker.length());
        return responseBody.substring(start + marker.length(), end);
    }
}
