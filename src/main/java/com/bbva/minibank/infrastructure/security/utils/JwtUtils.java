package com.bbva.minibank.infrastructure.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {
	
	@Value("${jwt.secret.key}")
	private String secretKey;
	
	@Value("${jwt.time.expiration}")
	private String timeExpiration;
	
	
	// Generar token de acceso
	public String generateAccesToken(String username) {
		log.info("Generando token de acceso");
		return Jwts.builder()
		           .setSubject(username)
		           .setIssuedAt(new Date(System.currentTimeMillis()))
		           .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
		           .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
		           .compact();
	}
	
	// Validar el token de acceso
	public boolean isTokenValid(String token) {
		log.info("Validando token");
		try {
			log.info("Token: {}", token);
			Jwts.parserBuilder()
			    .setSigningKey(getSignatureKey())
			    .build()
			    .parseClaimsJws(token)
			    .getBody();
			return true;
		} catch (Exception e) {
			log.error("Token invalido, error: ".concat(e.getMessage()));
			return false;
		}
	}
	
	// Obtener el username del token
	public String getUsernameFromToken(String token) {
		log.info("Obteniendo username del token");
		return getClaim(token, Claims::getSubject);
	}
	
	// Obtener un solo claim
	public <T> T getClaim(String token, Function<Claims, T> claimsTFunction) {
		log.info("Obteniendo claim");
		Claims claims = extractAllClaims(token);
		log.info("Claims: {}", claims);
		return claimsTFunction.apply(claims);
	}
	
	// Obtener todos los claims del token
	public Claims extractAllClaims(String token) {
		log.info("Obteniendo todos los claims del token");
		return Jwts.parserBuilder()
		           .setSigningKey(getSignatureKey())
		           .build()
		           .parseClaimsJws(token)
		           .getBody();
	}
	
	// Obtener firma del token
	public Key getSignatureKey() {
		log.info("Obteniendo firma del token");
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		log.info("Key bytes: {}", keyBytes);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
