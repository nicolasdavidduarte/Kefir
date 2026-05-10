package com.kefir.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.*;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String SECRET_KEY;

  private SecretKey cachedKey;

  public String generateToken(String username, List<String> roles) {
    return Jwts.builder()
        .claims() // Replacement for setClaims
        .add("roles", roles)
        .and()
        .subject(username) // setSubject -> subject
        .issuedAt(new Date()) // setIssuedAt -> issuedAt
        .expiration(
            new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // setExpiration -> expiration
        .signWith(getSecretKey())
        .compact();
  }

  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  private SecretKey getSecretKey() {
    if (cachedKey == null) {
      byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
      cachedKey = Keys.hmacShaKeyFor(keyBytes);
    }
    return cachedKey;
  }

  public boolean isTokenValid(String token, String username) {
    final String extractedUsername = extractUsername(token);
    return (extractedUsername.equals(username) && !isTokenExpired(token));
  }

  public boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> resolver) {
    final Claims claims = extractAllClaims(token);
    return resolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser() // parserBuilder() -> parser()
        .verifyWith(getSecretKey()) // setSigningKey() -> verifyWith()
        .build()
        .parseSignedClaims(token) // parseClaimsJws() -> parseSignedClaims()
        .getPayload(); // getBody() -> getPayload()
  }

  public List<String> extractRoles(String token) {
    Claims claims = extractAllClaims(token);
    Object rolesObject = claims.get("roles");

    if (rolesObject instanceof List<?> list) {
      return list.stream().map(Object::toString).toList();
    }

    return Collections.emptyList();
  }
}
