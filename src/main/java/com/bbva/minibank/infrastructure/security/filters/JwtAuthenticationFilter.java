package com.bbva.minibank.infrastructure.security.filters;

import com.bbva.minibank.infrastructure.entities.UserEntity;
import com.bbva.minibank.infrastructure.security.utils.JwtUtils;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final JwtUtils jwtUtils;
	
	public JwtAuthenticationFilter(JwtUtils jwtUtils) {
		log.info("JwtAuthenticationFilter created");
		this.jwtUtils = jwtUtils;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
	                                            HttpServletResponse response) throws AuthenticationException {
		
		UserEntity userEntity = null;
		String username = "";
		String password = "";
		try {
			log.info("Attempting authentication");
			userEntity = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
			username = userEntity.getUsername();
			password = userEntity.getPassword();
		} catch (StreamReadException e) {
			log.error("Error reading request input stream");
			throw new RuntimeException(e);
		} catch (DatabindException e) {
			log.error("Error mapping request input stream");
			throw new RuntimeException(e);
		} catch (IOException e) {
			log.error("Error reading request input stream");
			throw new RuntimeException(e);
		}
		log.info("Username: {}", username);
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, password);
		log.info("Authentication token: {}", authenticationToken);
		return getAuthenticationManager().authenticate(authenticationToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
	                                        HttpServletResponse response,
	                                        FilterChain chain,
	                                        Authentication authResult) throws IOException, ServletException {
		log.info("Authentication successful");
		User user = (User) authResult.getPrincipal();
		String token = jwtUtils.generateAccesToken(user.getUsername());
		log.info("Token generated: {}", token);
		response.addHeader("Authorization", token);
		log.info("Authorization header added to response");
		Map<String, Object> httpResponse = new HashMap<>();
		httpResponse.put("token", token);
		httpResponse.put("Message", "Autenticacion Correcta");
		httpResponse.put("Username", user.getUsername());
		log.info("Response body: {}", httpResponse);
		response.getWriter()
		        .write(new ObjectMapper().writeValueAsString(httpResponse));
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter()
		        .flush();
		log.info("Response sent");
		super.successfulAuthentication(request, response, chain, authResult);
	}
	
	
}
