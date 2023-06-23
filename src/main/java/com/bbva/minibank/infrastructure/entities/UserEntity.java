package com.bbva.minibank.infrastructure.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	UUID id;
	
	@Email
	@NotBlank
	@Size(max = 80)
	private String email;
	
	@NotBlank
	@Size(max = 30)
	private String username;
	
	@NotBlank
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class, cascade = CascadeType.PERSIST)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private ClientEntity client;
	
	
}
