package com.DPETL.DPETL;

import com.DPETL.DPETL.models.Admin; // Import the Admin class
import com.DPETL.DPETL.models.AppelOffres;
import com.DPETL.DPETL.models.Gestionnaire;
import com.DPETL.DPETL.repositories.AdminRepository;
import com.DPETL.DPETL.repositories.GestionnaireRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DpetlApplication {

	public static void main(String[] args) {
		SpringApplication.run(DpetlApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AdminRepository adminRepository,
//			PasswordEncoder passwordEncoder
//	){
//		return args -> {
//			var admin = Admin.builder()
//					.name("admin")
//					.username("admin")
//					.role("Admin")
//					.password(passwordEncoder.encode("12344"))
//					.build();
//			adminRepository.save(admin);
//
//		};
//	}
}
