package com.liberi.movies.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey signingKey; // Clave secreta para firmar los tokens 
    private final long expirationMillis; // Tiempo de expiración del token

    public JwtService(@Value("${app.jwt.secret}") String secret, // Se declara la clave secreta y tiempo de expiracion desde appproperties
                      @Value("${app.jwt.expiration-ms:3600000}") long expirationMillis) {
        this.signingKey = buildSigningKey(secret);  // Transforma la clave en un formato usable
        this.expirationMillis = expirationMillis;
    }

    public String generarToken(UserDetails userDetails) {
        Date ahora = new Date(); // Define fecha
        Date expiracion = new Date(ahora.getTime() + expirationMillis); // Aca lo que hace es, calcula los milisengundos actuales y le suma una hora, para asi calcular la expiracion 

        return Jwts.builder()
                .subject(userDetails.getUsername()) // Establece el username del token
                .issuedAt(ahora) // Establece la fecha de emisión del token
                .expiration(expiracion) // Establece la fecha de expiración del token
                .claim("roles", userDetails.getAuthorities().stream()   // Se usa un claim para los roles del user 
                        .map(authority -> authority.getAuthority())
                        .toList())
                .signWith(signingKey) //Firma el token con la clave secreta y lo devuelve como string
                .compact();
    }

    public String extraerUsername(String token) {
        return extraerClaims(token).getSubject(); // Extrae el username del token usando el claim "subject"
    }

    public boolean esTokenValido(String token, UserDetails userDetails) {
        Claims claims = extraerClaims(token); // Se guarda en una variable todos los token 
        return userDetails.getUsername().equals(claims.getSubject()) 
                && claims.getExpiration().after(new Date()); // Valida si pertenece a un user y si no expiro 
    }

    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey buildSigningKey(String secret) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException ex) {
            byte[] rawBytes = secret.getBytes(StandardCharsets.UTF_8);
            Key key = Keys.hmacShaKeyFor(rawBytes);
            return (SecretKey) key;
        }
    }
}
