package com.bbva.minibank.application.services;

import com.bbva.minibank.application.repository.IUserRepository;
import com.bbva.minibank.infrastructure.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final IUserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Loading user by username: {}", username);
		UserEntity userEntity = userRepository.findByUsername(username)
		                                      .orElseThrow(() -> new UsernameNotFoundException(
				                                      "El usuario " + username + " no existe."));
		log.info("User loaded: {}", userEntity);
		Collection<? extends GrantedAuthority> authorities = userEntity.getRoles()
		                                                               .stream()
		                                                               .map(role -> new SimpleGrantedAuthority(
				                                                               "ROLE_".concat(role.getName()
				                                                                                  .name())))
		                                                               .collect(Collectors.toSet());
		log.info("Authorities loaded: {}", authorities);
		return new User(userEntity.getUsername(),
		                userEntity.getPassword(),
		                true,
		                true,
		                true,
		                true,
		                authorities);
	}
	
	
}
