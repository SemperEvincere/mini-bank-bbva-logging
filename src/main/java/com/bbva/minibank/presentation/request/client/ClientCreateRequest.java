package com.bbva.minibank.presentation.request.client;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ClientCreateRequest {
	
	@NotBlank(message = "el apellido no puede estar vacío")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "El nombre debe contener solo caracteres alfabéticos")
	@Size(min = 2, max = 30, message = "El apellido debe tener entre 2 y 30 caracteres")
	private String lastName;
	
	@NotBlank(message = "el nombre no puede estar vacío")
	@Pattern(regexp = "^[a-zA-Z]+$", message = "El nombre debe contener solo caracteres alfabéticos")
	@Size(min = 2, max = 30, message = "El nombre debe tener entre 2 y 30 caracteres")
	private String firstName;
	
	//  @NotBlank(message = "el email no puede estar vacío")
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "El email debe ser válido")
	@Email
	private String email;
	
	@Pattern(regexp = "^[0-9]+$", message = "El teléfono debe contener solo caracteres numéricos")
	@Nullable
	private String phone;
	
	@Size(max = 50, message = "La dirección no puede tener más de 50 caracteres")
	@Nullable
	private String address;

//  @NotBlank(message = "el username no puede estar vacío")
//  private String username;

//  @NotBlank(message = "el password no puede estar vacío")
//  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
//      message = "El password debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un caracter " +
//                    "especial")
//  private String password;
	
	//  private String role;
	private UUID userId;
}
