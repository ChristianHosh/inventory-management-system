package com.chris.ims.entity.security;

import com.chris.ims.entity.exception.BxException;
import com.chris.ims.entity.utils.BDate;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.UUID;

@Slf4j
@Component
public final class JwtUtils {

  @Value("${chris.entity.jwt-secret}")
  private String jwtSecret;

  @Value("${chris.entity.jwt-expirationDays}")
  private int jwtExpirationDays;

  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

    return Jwts.builder()
        .id(UUID.randomUUID().toString())
        .subject(userPrincipal.getUsername())
        .issuedAt(BDate.currentDate().toDate())
        .expiration(BDate.currentDate().addDay(jwtExpirationDays).toDate())
        .signWith(key())
        .compact();
  }

  public boolean validateJwtToken(String token) {
    if (token == null)
      return false;
    
    try {
      Jwts.parser()
          .verifyWith(key())
          .build()
          .parse(token);
      return true;
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
      throw BxException.unauthorized("Invalid JWT token");
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
      throw BxException.unauthorized("JWT token is expired");
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
      throw BxException.unauthorized("JWT token is unsupported");
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
      throw BxException.unauthorized("JWT claims string is empty");
    }
  }

  private SecretKey key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public String getUsernameFromJwtToken(String token) {
    return Jwts.parser()
        .decryptWith(key())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }
}
