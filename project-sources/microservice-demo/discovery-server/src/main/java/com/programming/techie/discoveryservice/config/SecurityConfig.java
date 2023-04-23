package com.programming.techie.discoveryservice.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig //extends WebSecurityConfigurerAdapter
{
	@Value("${eureka.username}")
	private String username;
	
	@Value("${eureka.password}")
	private String password;
	
	private static final String NOOP_PASSWORD_ENCODER_ID = "{noop}";

	/*
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.inMemoryAuthentication()
			.passwordEncoder(NoOpPasswordEncoder.getInstance())
			.withUser(username).password(password)
			.authorities("ADMIN");
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception
	{
		http.csrf().disable()
			.authorizeRequests()
				.antMatchers("/actuator/**").permitAll()
				.anyRequest().authenticated()
			.and()
			.httpBasic();
	}
	*/
	
	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception
	{ 
        AuthenticationManager authManager = authenticationConfiguration.getAuthenticationManager();
        
        return authManager;
    }
	
	@Bean
	public InMemoryUserDetailsManager configureAuthentication() throws Exception
	{
		UserDetails admin = User.withUsername(username)
								.password(NOOP_PASSWORD_ENCODER_ID + password)
								.authorities(Arrays.asList(new SimpleGrantedAuthority("ADMIN")))
							.build();
		
		InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager(admin);
		
		return userDetailsManager;
	}
	
	@Bean
	public SecurityFilterChain configureSecurityFilterChain(HttpSecurity http) throws Exception
	{
		http.csrf().disable()
		.authorizeRequests()
			.antMatchers("/actuator/**").permitAll()
			.anyRequest().authenticated()
		.and()
		.httpBasic();
		
		return http.build();
	}

}
