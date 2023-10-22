package registration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import registration.handler.CourseHandler;
import registration.handler.RegisterHandler;
import registration.handler.StudentHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> register(RegisterHandler handler){
        return route(GET("/v2/registers"), handler::findAll)
                .andRoute(GET("/v2/registers/{id}"), handler::findAllId)
                .andRoute(POST("/v2/registers"), handler::create)
                .andRoute(PUT("/v2/registers/{id}"), handler::update)
                .andRoute(DELETE("/v2/registers/{id}"), handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> course(CourseHandler handler){
        return route(GET("/v2/courses"), handler::findAll)
                .andRoute(GET("/v2/courses/{id}"), handler::findAllId)
                .andRoute(POST("/v2/courses"), handler::create)
                .andRoute(PUT("/v2/courses/{id}"), handler::update)
                .andRoute(DELETE("/v2/courses/{id}"), handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> student(StudentHandler handler){
        return route(GET("/v2/students"), handler::findAll)
                .andRoute(GET("/v2/students/{id}"), handler::findAllId)
                .andRoute(POST("/v2/students"), handler::create)
                .andRoute(PUT("/v2/students/{id}"), handler::update)
                .andRoute(DELETE("/v2/students/{id}"), handler::delete)
                .andRoute(GET("v2/orderDesc"), handler::findDescAge)
                .andRoute(GET("v2/orderAsc"), handler::findAscAge);
    }
}
