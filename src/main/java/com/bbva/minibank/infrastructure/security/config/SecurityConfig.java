package com.bbva.minibank.infrastructure.security.config;

import com.bbva.minibank.infrastructure.security.filters.JwtAuthenticationFilter;
import com.bbva.minibank.infrastructure.security.filters.JwtAuthorizationFilter;
import com.bbva.minibank.infrastructure.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Log4j2
public class SecurityConfig {
	
	private final JwtUtils jwtUtils;
	private final UserDetailsService userDetailsService;
	private final JwtAuthorizationFilter authorizationFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
	                                        AuthenticationManager authenticationManager) throws Exception {
		log.info("Configuring security filter chain");
		JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
		log.info("Configuring authentication manager");
		jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
		log.info("Configuring authentication filter");
		jwtAuthenticationFilter.setFilterProcessesUrl("/user/login");
		log.info("Configuring authorization filter");
		return httpSecurity
				       .csrf(AbstractHttpConfigurer::disable)
				       .authorizeHttpRequests(auth -> {
					       auth.requestMatchers("/user/login")
					           .permitAll();
					       auth.requestMatchers("/user/**")
					           .hasRole("ADMIN");
					       auth.requestMatchers("/account/**")
					           .hasRole("ADMIN");
					       auth.requestMatchers("/transactions/**")
					           .hasRole("USER");
					       auth.anyRequest()
					           .authenticated();
				       })
				       .sessionManagement(session -> {
					       session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				       })
				       .addFilter(jwtAuthenticationFilter)
				       .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
				       .build();
		
	}
	
	
	@Bean
	PasswordEncoder passwordEncoder() {
		log.info("Configuring password encoder");
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity,
	                                            PasswordEncoder passwordEncoder) throws Exception {
		log.info("Configuring authentication manager");
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
		                   .userDetailsService(userDetailsService)
		                   .passwordEncoder(passwordEncoder)
		                   .and()
		                   .build();
	}
}
