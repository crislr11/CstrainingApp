package com.example.csTraining.config;

import com.example.csTraining.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String JWT_SECRET = "33dcb707ebbfa817768d1784cba77e3df2bf982de7d2f2c538b55bfe64031369a14eb630821ccd8d11920c95ed0bfce5d784f213f3d5a65c95f850cde3d339fcbcc932686eddbf6160504ccfc267cc174b4e08c18003c56225d007675923b5dfd7e6e076421da0245ea27b2202c4c4b6c2fd570aa4c7e70e0f435de6d4bd7dded9b628d3317e0105e7c3c4a4dc8697d54075f1e3b92f6d7cbe0868bf83bd65b961fec5cbb48459037b6f1e93b58ac1e9790033cb1bbfc827e26e54d26d3d2a3127eebcb451f8213a146375693c13872138f6d00f713621c24eb1b808ef6242dab1325ca2d7b1e8ef005fbc4a2b894b04456620c535208355ae182e1103f144f1";

    public String generateJwtToken(UserDetails userDetails) {
        User user = (User) userDetails;
        String nombre = user.getUsername();
        String oposicion = user.getOposion().name();
        String role = user.getRole().name();
        return generateToken(userDetails, nombre, oposicion, role);
    }

    public String generateToken(UserDetails userDetails, String nombre, String oposicion, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("nombre", nombre);
        claims.put("oposicion", oposicion);
        claims.put("rol", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    public String getNombreFromToken(String token) {
        return getClaim(token, claims -> claims.get("nombre", String.class));
    }

    public String getOposicionFromToken(String token) {
        return getClaim(token, claims -> claims.get("oposicion", String.class));
    }

    public String getRoleFromToken(String token) {
        return getClaim(token, claims -> claims.get("rol", String.class));
    }
}
