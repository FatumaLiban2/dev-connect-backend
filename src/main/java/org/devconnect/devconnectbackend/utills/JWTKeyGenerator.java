package org.devconnect.devconnectbackend.utills;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Jwks;
import io.jsonwebtoken.security.Keys;

import java.security.KeyPair;
import java.util.Base64;

public class JWTKeyGenerator {
    public static void main(String[] args) {
        System.out.println("Generating EdDSA Key Pair for JWT...\n");

        // Generate an EdDSA key pair
        KeyPair keyPair = Jwks.CRV.Ed25519.keyPair().build();

        // Encode Keys to Base64
        String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        // Print keys
        System.out.println("Copy these to your application.properties:");
        System.out.println("==========================================");
        System.out.println();
        System.out.println("jwt.private.key: " + privateKeyBase64);
        System.out.println();
        System.out.println("jwt.public.key: " + publicKeyBase64);
        System.out.println();
        System.out.println("# Access token expiration (15 minutes in milliseconds)");
        System.out.println("jwt.access.expiration=900000");
        System.out.println();
        System.out.println("# Refresh token expiration (30 days in milliseconds)");
        System.out.println("jwt.refresh.expiration=2592000000");
        System.out.println();
        System.out.println("==========================================");
        System.out.println("\n✅ Keys generated successfully!");
        System.out.println("⚠️  IMPORTANT: Keep these keys secret and never commit them to version control!");
    }
}
