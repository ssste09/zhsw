package com.zhsw.auth.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final long expirationMs = 24 * 60 * 60 * 1000;

    public String generateToken(Long userId, String email, Role role) {
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    //    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
    //        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
    //
    //        Claims claims = Jwts.parserBuilder()
    //                .setSigningKey(key)
    //                .build()
    //                .parseClaimsJws(token)
    //                .getBody();
    //
    //        String email = claims.getSubject();
    //        String role = claims.get("role", String.class);
    //
    //        GrantedAuthority authority = new SimpleGrantedAuthority(role);
    //
    //        return new UsernamePasswordAuthenticationToken(email, null, List.of(authority));
    //    }
}
