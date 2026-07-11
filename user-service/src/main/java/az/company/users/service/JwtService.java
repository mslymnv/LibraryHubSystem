package az.company.users.service;

import az.company.users.dao.entity.UserEntity;
import az.company.users.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Getter
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String key;
    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    public String generateAccesToken(UserPrincipal userPrincipal) {
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("roles", userPrincipal.getRoles())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (accessTokenExpiration * 60 * 60 * 1000)))
                .signWith(secretKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }

    public boolean isTokenValid(String token) {
        return extractAllClaims(token).getExpiration().after(new Date());
    }
}
