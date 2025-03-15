package io.jotnkode.siguranj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.jotnkode.siguranj.services.SiguranService;
/**
    Router for siguranj API. Provides the following endpoints
    - /api/user POST
    - /api/authenticate POST
    - /api/heartbeat GET
*/
@Configuration
public class SiguranRouter {
    @Autowired
    private SiguranService service;

    /**
    * Initializes siguranj routes.
    * @return @{link RouterFunction<ServerResponse> }
    */
    @Bean
    RouterFunction<ServerResponse> routes() {
        return RouterFunctions
            .route(RequestPredicates.POST("api/user").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), service::addUser)
            .andRoute(RequestPredicates.POST("api/authenticate").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), service::authenticate)
            .andRoute(RequestPredicates.GET("api/heartbeat"), service::heartbeat);
    }
}
