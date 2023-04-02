package com.programming.techie.apigateway.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.nimbusds.jose.shaded.json.JSONObject;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Configure the Spring Security based on the OAuth2 authorization server.
 * 
 * NOTE: We enable WebFlux Security because Spring API Gateway is based on Spring WebFlux.
 */
@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig
{
	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuerUri;
	
	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	private String jwkSetUri;

	//@Value("${spring.security.oauth2.resourceserver.jwt.client-id}")
	//private String clientId;
	
	@Value("${spring.security.oauth2.resourceserver.jwt.audience}")
	private String audience;
	
	@Value("${server.ssl.enabled}")
	private String sslEnabled;
	
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity)
	{
		// disable CSRF
		serverHttpSecurity.csrf().disable()
		
		// specify the access to the given paths
		.authorizeExchange(exchange -> exchange
				// permit access to Eureka (it has own authentication)
				.pathMatchers("/eureka/web").permitAll()
				.pathMatchers("/eureka/**").permitAll()
				
				// permit access to (Angular) Web GUI server
				.pathMatchers("/").permitAll()
				.pathMatchers("/mspt-web").permitAll()
				.pathMatchers("/mspt-web/").permitAll()
				.pathMatchers("/mspt-web/**").permitAll()
				
				// permit access to Swagger API Docs for each API service
				//.pathMatchers("/api/*/swagger/**").permitAll()
				.pathMatchers("/api/*/swagger/api-docs").permitAll()
				
				// permit access to Swagger UI on this gateway
				.pathMatchers("/swagger/**").permitAll()
				.pathMatchers("/swagger-ui/**").permitAll()
				.pathMatchers("/swagger-resources/**").permitAll()
				
				// permit access to Actuator endpoints
				.pathMatchers("/actuator/**").permitAll()
				
				// permit access to Grafana service (it has own authentication)
				.pathMatchers("/grafana/**").permitAll()
				
				// API services are authenticated
				.pathMatchers("/api/product/**").authenticated()
				.pathMatchers("/api/order/**").authenticated()
				.pathMatchers("/api/cart/**").authenticated()
				.pathMatchers("/api/inventory/**").authenticated()
				.pathMatchers("/api/notification/**").authenticated()
				
				// any other paths are authenticated
				.anyExchange().authenticated()
		);
		
		// Configure HTTPS redirection: all non-HTTPS requests will be redirected to HTTPS
        if (sslEnabled != null && sslEnabled.toLowerCase().equals("true")) {
        	log.info("SSL is enabled.");
        	serverHttpSecurity.redirectToHttps(spec -> {
        		spec.httpsRedirectWhen((serverWebExchange) -> {
        			boolean needRedirect =  !"https".equals(serverWebExchange.getRequest().getURI().getScheme());
        			if (needRedirect) {
        				log.info("HTTPS redirect is required.");
        			}
	            	return needRedirect;
	            });
        	});
        }
		
		// https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
		// https://www.baeldung.com/spring-security-oauth-resource-server
		// https://learn.microsoft.com/en-us/answers/questions/639834/how-to-get-access-token-version-2-0
		// https://github.com/naridnevahgar/spring-webflux-azuread-rbac
		//
		// Configures OAuth 2.0 Resource Server support for this Spring Cloud Gateway
		// Enables JWT Resource Server support: our API Gateway acts as a resource server with supporting JWT token
        serverHttpSecurity.oauth2ResourceServer( spec -> spec.jwt()
										.jwtDecoder(jwtDecoder())
										.jwtAuthenticationConverter(grantedAuthoritiesExtractor())
		);
		
		return serverHttpSecurity.build();
	}
	
	/**
	 * Create JWT decoder with the configured JWK_SET_URI, and specifies the token validator.
	 */
	private ReactiveJwtDecoder jwtDecoder()
	{
		//NimbusReactiveJwtDecoder jwtDecoder = (NimbusReactiveJwtDecoder) ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
		NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
		
		jwtDecoder.setJwtValidator(createTokenValidator(issuerUri, audience));
		log.info("ReactiveJwtDecoder: " + jwtDecoder + " | specified JWT token validator.");
		
		return jwtDecoder;
	}
	
	/**
	 * Create OAuth2 Token Validator: Validating JWT token with the configured Issuer and Audience.
	 */
	private OAuth2TokenValidator<Jwt> createTokenValidator(final String issuerUri, final String audience)
	{
		OAuth2TokenValidator<Jwt> tokenValidator = new DelegatingOAuth2TokenValidator<Jwt>(
				JwtValidators.createDefaultWithIssuer(issuerUri),
				new JwtClaimValidator<List<String>>(JwtClaimNames.AUD, (aud) -> {
					log.info("Validating JWT token: audiences {} with {}", aud, audience);
					return aud != null && (aud.contains(audience) || aud.contains("api://"+audience));
				})
		);
		
		return tokenValidator;
	}
	
	/**
	 * Convert the "roles" from the JWT Claims to Spring "GrantedAuthority" object with "ROLE_" prefix.
	 */
	private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor()
	{
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter((jwt) ->
			{
				/*
				"realm_access": {
				    "roles": [
				      "offline_access",
				      "default-roles-mspt-microservice-realm",
				      "uma_authorization",
				      "ADMIN"
				    ]
				 }
				*/
				
				/*
				jwt.getClaims().forEach( (key, data) -> {
					log.info("Claim: name="+key+", data="+data+" ["+data.getClass().getTypeName()+"]");
				});
				*/
				
				// initialize an empty collection for authorities
				Collection<?> authorities = Collections.emptyList();
				
				// read the "realm_access" claim with its JSON data
				JSONObject json = (JSONObject) jwt.getClaims().getOrDefault("realm_access", null);
				if (json != null) {
					// if we have non-null JSON data, then we get "roles" from the JSON data
					authorities = (Collection<?>) json.getOrDefault("roles", Collections.emptyList());
				}
				//authorities = (Collection<?>) jwt.getClaims().getOrDefault("roles", Collections.emptyList());
				
				log.info("Roles from JWT claims: " + authorities);

				return authorities.stream()
						.map(Object::toString)
						.map(role -> "ROLE_".concat(role))
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());
			}
		);
		
		return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
	}
	
}
