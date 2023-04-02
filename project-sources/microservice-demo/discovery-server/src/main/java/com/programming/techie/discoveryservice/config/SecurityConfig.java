package com.programming.techie.discoveryservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	@Value("${eureka.username}")
	private String username;
	
	@Value("${eureka.password}")
	private String password;

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

}
