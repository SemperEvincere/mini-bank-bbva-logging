package com.bbva.minibank;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class MiniBankApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MiniBankApplication.class, args);
		
	}
	
}
