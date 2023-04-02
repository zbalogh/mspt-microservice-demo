package com.programming.techie.apigateway.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

//
// It is based on the following website:
// https://medium.com/@nalsuwailem/api-documentation-in-spring-cloud-gateway-or-how-to-expose-your-be-end-point-documentation-via-abab84547611
//

@Configuration
@Primary // register custom configuration first to avoid registration conflict with inMemorySwaggerResourcesProvider
public class SwaggerConfig implements SwaggerResourcesProvider
{
    private static final String SWAGGER_API_DOCS_URI = "/swagger/api-docs"; // OpenApi description URI for the downstream API services
    
    private static final String API_BASE_PATH = "/api";	// Base path for the downstream API services

    private final RouteDefinitionLocator routeLocator; // Gateway route locator

    /**
     * Constructor: Inject the RouteDefinitionLocator from Spring Cloud Gateway.
     * 
     * @param routeLocator
     */
    public SwaggerConfig(RouteDefinitionLocator routeLocator)
    {
        this.routeLocator = routeLocator;
    }

    /**
     * Create Swagger resource registration details.
     * 
     * @return List
     */
    @Override
    public List<SwaggerResource> get()
    {
        List<SwaggerResource> resources = new ArrayList<>(); // declare & initialize swagger resource list
        
        routeLocator.getRouteDefinitions().subscribe( // subscribe to Gateway locator definitions to monitor locator definition changes
                routeDefinition -> { // for each route definition
                    String resourceName = routeDefinition.getId(); // assign the route ID to resourceName
                    String location = routeDefinition.getPredicates()
                    		.get(0)
                    		.getArgs()
                    		.get("_genkey_0"); // get the Path value (e.g. /api/order/**)
                    // we are only interested in API services
                    if (location.startsWith(API_BASE_PATH)) {
                    	location = location.replace("/**", SWAGGER_API_DOCS_URI); // replace predicate extensions with /swagger/api-docs. The resulted location will become: /api/product/swagger/api-docs
                    	resources.add(swaggerResource(resourceName, location)); // add the route definition as a Swagger resource
                    }
                }
        );
        
        return resources;
    }

    /**
     * Swagger resource registration.
     * 
     * @param name
     * @param location
     * @return SwaggerResource
     */
    private SwaggerResource swaggerResource(String name, String location)
    {
        SwaggerResource swaggerResource = new SwaggerResource(); // initialize a swaggerResource pointer
        swaggerResource.setName(name); // set swaggerResource name to the given name
        swaggerResource.setLocation(location); // set swaggerResource location to the given location (e.g., /api/product/swagger/api-docs)
        swaggerResource.setSwaggerVersion("2.0"); // set swaggerResource version.
        return swaggerResource;
    }
    
}
