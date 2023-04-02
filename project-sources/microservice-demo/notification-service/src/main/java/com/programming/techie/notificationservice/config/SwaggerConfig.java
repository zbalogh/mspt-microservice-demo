package com.programming.techie.notificationservice.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

	/**
	 * Override the server URL by setting an empty string.
	 * In that case, the Swagger UI will use the browser location when you try out an API endpoint.
	 */
	@Bean
	OpenApiCustomiser getDefaultOpenApiCustomiser()
	{
		return openApi -> {
			List<Server> servers = openApi.getServers();
			if (servers != null) {
				servers = servers.stream()
						.map(server -> {
							// remove the URL, we use empty string: So Swagger UI will use the browser location
							server.setUrl("");
							server.description("Notification API service");
							return server;
						})
						.collect(Collectors.toList());
				//
				openApi.setServers(servers);
			}
		};
	}
	
	/**
     * Customize the OpenAPI definition with JWT security schema.
     */
    @Bean
    public OpenAPI customizeOpenAPI()
    {
        final String securitySchemeName = "Bearer Authentication";
        return new OpenAPI()
          .addSecurityItem(new SecurityRequirement()
            .addList(securitySchemeName))
          .components(new Components()
            .addSecuritySchemes(securitySchemeName, new SecurityScheme()
              .name(securitySchemeName)
              .type(SecurityScheme.Type.HTTP)
              .scheme("bearer")
              .bearerFormat("JWT")
              .description("Provide the JWT token.")
            )
          )
          .info(new Info().title("MSPT Notification API")
          .description("OpenAPI definition for the MSPT Notification API endpoints."));
    }

}
