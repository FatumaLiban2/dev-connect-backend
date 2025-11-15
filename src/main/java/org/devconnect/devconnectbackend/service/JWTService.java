package org.devconnect.devconnectbackend.service;


import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTService {

    @Value("${JWT_PRIVATE_KEY_BASE64}")
    private String privateKey;

    @Value("${JWT_PUBLIC_KEY_BASE64}")
    private String publicKey;

    @Value("${JWT_ACCESS_EXPIRATION_MILLS}")
    private long accessTokenExpirationMills; // 15 minutes in milliseconds

    @Value("${JWT_REFRESH_EXPIRATION_MILLS}")
    private long refreshTokenExpirationMills; // 30 days in milliseconds

    private PrivateKey privateKeyActual;

    private PublicKey publicKeyActual;

    @PostConstruct
    public void init() {
        try {
            // Decode Base64 encoded keys
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);

            // Create Key specs
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);

            // Get KeyFactory for EdDSA
            KeyFactory keyFactory = KeyFactory.getInstance("EdDSA");

            // Generate actual key objects
            this.privateKeyActual = keyFactory.generatePrivate(privateKeySpec);
            this.publicKeyActual = keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize JWT keys: " + e.getMessage(), e);
        }
    }

    public String generateAccessToken(String email, Integer userId) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationMills))
                .signWith(privateKeyActual, Jwts.SIG.EdDSA)
                .compact();
    }

    public String generateRefreshToken(String email, Integer userId) {
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMills))
                .signWith(privateKeyActual, Jwts.SIG.EdDSA)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(publicKeyActual)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Integer extractUserId(String token) {
        return Jwts.parser()
                .verifyWith(publicKeyActual)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", Integer.class);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(publicKeyActual)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
