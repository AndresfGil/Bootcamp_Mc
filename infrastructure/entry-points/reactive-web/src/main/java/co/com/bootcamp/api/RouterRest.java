package co.com.bootcamp.api;

import co.com.bootcamp.api.docs.BootcampControllerDocs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest implements BootcampControllerDocs {
    
    @Bean
    @Override
    public RouterFunction<ServerResponse> routerFunction(BootcampHandler handler) {
        return route(POST("/api/bootcamp"), handler::listenGuardarBootcamp)
                .andRoute(GET("/api/bootcamp"), handler::listenListarBootcamps)
                .andRoute(DELETE("/api/bootcamp/{id}"), handler::listenEliminarBootcamp);
    }
}
