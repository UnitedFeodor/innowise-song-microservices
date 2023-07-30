package com.innowise.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    //TODO test routes config
//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("file_api_route", route -> route.path("/files/**")
//                        .uri("lb://file-api"))
//                .route("song_api_route", route -> route.path("/songs/**")
//                        .uri("lb://song-api"))
//                .build();
//    }
}
