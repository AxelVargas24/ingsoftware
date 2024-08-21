package com.ingenieriaweb.bdringo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
	            .requestMatchers("/", "/home", "/template_usuario", "/carrito", "/usuario/**", "/search",
	                    "/delete/cart/**", "/productos/**", "/cart", "/getCart", "/saveOrder", "/order", "/images/**",
	                    "/vendor/**", "/css/**","/administrador/**")
	            .permitAll()
	            .requestMatchers("/productohome/**").permitAll()
	            .requestMatchers("/administrador/**").hasRole("ADMIN")
	            .anyRequest().authenticated())
	        .formLogin(formLogin -> formLogin
	            .loginPage("/usuario/login")
	            .defaultSuccessUrl("/administrador/home", true)
	            .permitAll())
	        .logout(logout -> logout.permitAll())
	        .csrf(csrf -> csrf.disable());

	    return http.build();
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
