package com.hba.apigateway;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class TokenProvider {
  private static final Logger log = LoggerFactory.getLogger(TokenProvider.class);
  private final SecretKey secretKey;
  private final JwtParser parser;

  private TokenProvider(SecretKey secretKey, JwtParser parser) {
    this.secretKey = secretKey;
    this.parser = parser;
  }

  public static TokenProvider signedTokenProvider(String secret) {
    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    return new TokenProvider(secretKey, parser);
  }

  public static TokenProvider unsignedTokenProvider() {
    JwtParser parser = Jwts.parserBuilder().build();
    return new TokenProvider(null, parser);
  }

  public String createToken(User user) {
    if (secretKey == null) {
      throw new UnsupportedOperationException();
    }
    return Jwts.builder()
        .setId(String.valueOf(user.getId()))
        .setSubject(user.getUsername())
        .claim("authorities", getAuthoritiesAsString(user.getAuthorities()))
        .signWith(secretKey, SignatureAlgorithm.HS512)
        .compact();
  }

  public String createUnsignedToken(String token) {
    Claims claims = parser.parseClaimsJws(token).getBody();
    return Jwts.builder().setClaims(claims).compact();
  }

  public Authentication getAuthentication(String token) {
    try {
      Claims claims;
      if (secretKey == null) {
        claims = parser.parseClaimsJwt(token).getBody();
      } else {
        claims = parser.parseClaimsJws(token).getBody();
      }
      User user = new User();
      user.setId(Integer.parseInt(claims.getId()));
      user.setEmail(claims.getSubject());
      Collection<? extends GrantedAuthority> authorities =
          getAuthoritiesFromClaim(claims.get("authorities", String.class));
      if (!authorities.isEmpty()) {
        user.setAuthority(authorities.iterator().next().getAuthority());
      }
      return new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
    } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
      log.trace(e.getMessage());
      return null;
    }
  }

  private Collection<? extends GrantedAuthority> getAuthoritiesFromClaim(String authoritiesClaim) {
    return Arrays.stream(authoritiesClaim.split(","))
        .filter(auth -> !auth.trim().isEmpty())
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  private String getAuthoritiesAsString(Collection<? extends GrantedAuthority> authorities) {
    return authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));
  }
}