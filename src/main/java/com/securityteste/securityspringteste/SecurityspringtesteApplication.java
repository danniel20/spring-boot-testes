package com.securityteste.securityspringteste;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SecurityspringtesteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityspringtesteApplication.class, args);
		//System.out.println(new BCryptPasswordEncoder().encode("123"));
	}

}
