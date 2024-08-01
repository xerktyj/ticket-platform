package it.platform.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import it.platform.service.DatabaseUserDetailsService;

@Configuration
public class SecurityConfiguration {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http)
	throws Exception {
	     http.authorizeHttpRequests()
	                    .requestMatchers(HttpMethod.POST,"/tickets/addticket").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.GET,"/tickets/addticket").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.POST,"/tickets/delete/**").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.GET,"/notes/deletenote/**").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.POST,"/notes/deletenote/**").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.GET,"/users/list/**").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.POST,"/users/adduser").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.GET,"/users/adduser").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.POST,"/users/deleteuser/**").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.POST,"/categories/**").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.GET,"/categories/**").hasAuthority("ADMIN")
	                    .requestMatchers(HttpMethod.GET,"/home","/tickets/**","/users/**","/notes/**").hasAnyAuthority("USER","ADMIN")
	                    .requestMatchers("/**").permitAll()
	                    .and().formLogin()
	                    .and().logout()
	                    .and().exceptionHandling()
	                    .and().csrf().disable();
	    return http.build();
	}
	
	
	@Bean
	DatabaseUserDetailsService userDetailsService() {
		return new DatabaseUserDetailsService();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

}
