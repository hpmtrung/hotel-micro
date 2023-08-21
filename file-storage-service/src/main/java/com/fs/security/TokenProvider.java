package com.fs.security;

import com.fs.domain.Client;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class TokenProvider {
  private SecretKey secretKey;
  private JwtParser parser;

  public void setSecretKey(String secret) {
    secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
  }

  public String createToken(Client client) {
    return Jwts.builder()
        .setSubject(client.getUsername())
        .setId(client.getId().toString())
        .signWith(secretKey, SignatureAlgorithm.HS512)
        .compact();
  }

  public Authentication getAuthentication(String token) {
    try {
      Claims claims = parser.parseClaimsJws(token).getBody();
      Client client = new Client();
      client.setUsername(claims.getSubject());
      if (claims.getId() != null) {
        client.setId(UUID.fromString(claims.getId()));
      }
      return new UsernamePasswordAuthenticationToken(client, token, client.getAuthorities());
    } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
      log.trace(e.getMessage());
      return null;
    }
  }
}