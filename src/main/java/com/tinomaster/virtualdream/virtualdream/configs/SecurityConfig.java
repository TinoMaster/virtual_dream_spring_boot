package com.tinomaster.virtualdream.virtualdream.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tinomaster.virtualdream.virtualdream.utils.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/v1/public/**").permitAll()
						.requestMatchers("/api/v1/superadmin/**").hasAuthority("SUPERADMIN")
						.requestMatchers("/api/v1/owner/**").hasAnyAuthority("SUPERADMIN", "OWNER")
						.requestMatchers("/api/v1/admin/**").hasAnyAuthority("ADMIN", "SUPERADMIN", "OWNER")
						.requestMatchers("/api/v1/private/**").authenticated().anyRequest().authenticated())
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		http.cors(Customizer.withDefaults());

		return http.build();
	}
}
