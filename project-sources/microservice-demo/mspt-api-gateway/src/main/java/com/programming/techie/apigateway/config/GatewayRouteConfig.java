package com.programming.techie.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRouteConfig
{
	/**
	 * Configure here the route locator which is used by the gateway to route the incoming traffic to the downstream services.
	 */
	@Bean
    public RouteLocator configureRoute(RouteLocatorBuilder builder)
	{
       return builder.routes()
    		   /*
    		   .route("discovery-server", r -> r.path("/eureka/web")
    				   					 .filters(f -> f.setPath("/"))
    				   					 .uri("http://mspt-discovery-server:8761")
    		    )
    		   .route("discovery-server-static", r -> r.path("/eureka/**")
    				   				   		.uri("http://mspt-discovery-server:8761")
    		    )
    		   */
    		   .route("grafana-service", r -> r.path("/grafana/**")
    				   						// preserve the host header on the ongoing request from Gateway to Grafana
    				   						// otherwise the  Grafana Dashboard refuses the query POST request with 'origin not allowed' message
    				   						// so we keep the original host from where the grafana UI downloaded in the browser.
    				   						.filters(f -> f.preserveHostHeader()
    				   						 )
  											.uri("http://grafana:3000")
    			)
    		   .route("webgui-server-r1", r -> r.path("/mspt-web/**")
    				   						.uri("http://mspt-webgui-server:8050")
    		    )
    		   .route("webgui-server-r2", r -> r.path("/")
  											.uri("http://mspt-webgui-server:8050")
    		    )
      .build();
    }
	
	/*
	## Discovery Server Route
	spring.cloud.gateway.routes[8].id=discovery-server
	spring.cloud.gateway.routes[8].uri=http://mspt-discovery-server:8761
	spring.cloud.gateway.routes[8].predicates[0]=Path=/eureka/web
	spring.cloud.gateway.routes[8].filters[0]=SetPath=/

	## Discovery Server Static Resources Route (CSS, JS, etc files)
	spring.cloud.gateway.routes[9].id=discovery-server-static
	spring.cloud.gateway.routes[9].uri=http://mspt-discovery-server:8761
	spring.cloud.gateway.routes[9].predicates[0]=Path=/eureka/**
	*/
	
}
