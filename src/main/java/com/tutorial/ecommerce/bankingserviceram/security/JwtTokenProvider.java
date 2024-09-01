package com.tutorial.ecommerce.bankingserviceram.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
public class JwtTokenProvider {

  @Value("${security.jwt.secret}")
  private String secretKey;

  @Value("${security.jwt.expiration}")
  private long expirationTime;


  @Value("${security.client.id}")
  private String clientId;

  @Value("${security.client.secret}")
  private String clientSecret;

  private Algorithm algorithm;
  private JWTVerifier jwtVerifier;


  @PostConstruct
  public void init() {
    this.algorithm = Algorithm.HMAC256(secretKey);
    this.jwtVerifier = JWT.require(algorithm).build();
  }

  public String generateToken(String email, String role) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationTime);

    return JWT.create()
            .withSubject(email)
            .withClaim("role", role)
            .withClaim("email", email)
            .withIssuedAt(now)
            .withExpiresAt(expiryDate)
            .sign(algorithm);
  }

  public String extractEmailFromToken(String token) {
    DecodedJWT decodedJWT = jwtVerifier.verify(token);
    return decodedJWT.getSubject();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    try {
      DecodedJWT decodedJWT = jwtVerifier.verify(token);
      String email = decodedJWT.getSubject();
      if (isTokenExpired(decodedJWT)) {
        throw new JWTVerificationException("Expired token");
      }
      return email.equals(userDetails.getUsername()) && !isTokenExpired(decodedJWT);
    } catch (Exception e) {
      return false;
    }
  }

  private boolean isTokenExpired(DecodedJWT decodedJWT) {
    Date expiration = decodedJWT.getExpiresAt();
    return expiration.before(new Date());
  }

  public boolean validateCredentials(String[] credentials) {
    return clientId.equals(credentials[0]) && clientSecret.equals(credentials[1]);
  }

  public Authentication getClientAuthentication() {
    return new UsernamePasswordAuthenticationToken(clientId, null, Collections.emptyList());
  }
}
