package com.bbva.minibank.infrastructure.security.filters;

import com.bbva.minibank.application.services.UserDetailsServiceImpl;
import com.bbva.minibank.infrastructure.security.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	
	private final JwtUtils jwtUtils;
	private final UserDetailsServiceImpl userDetailsService;
	
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
	                                @NonNull HttpServletResponse response,
	                                @NonNull FilterChain filterChain) throws ServletException, IOException {
		log.info("JwtAuthorizationFilter.doFilterInternal");
		String tokenHeader = request.getHeader("Authorization");
		
		if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
			log.info("Token header: {}", tokenHeader);
			String token = tokenHeader.substring(7);
			
			if (jwtUtils.isTokenValid(token)) {
				log.info("Token is valid");
				String username = jwtUtils.getUsernameFromToken(token);
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				log.info("User details: {}", userDetails);
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
				log.info("Authentication token: {}", authenticationToken);
				SecurityContextHolder.getContext()
				                     .setAuthentication(authenticationToken);
			}
		}
		log.info("Filter chain");
		filterChain.doFilter(request, response);
	}
}
