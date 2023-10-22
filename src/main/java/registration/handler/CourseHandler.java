package registration.handler;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import registration.model.Course;
import registration.model.dto.CourseDTO;
import registration.service.ICourseService;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class CourseHandler {

    private final ICourseService service;

    private final ModelMapper mapper;

    public Mono<ServerResponse> findAll(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll().map(this::convertDto), CourseDTO.class);
    }

    public Mono<ServerResponse> findAllId(ServerRequest req){

        String id = req.pathVariable("id");

        return service.findById(id)
                .map(this::convertDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                ).switchIfEmpty(ServerResponse
                        .notFound()
                        .build());
    }

    public Mono<ServerResponse> create(ServerRequest req){
        Mono<CourseDTO> monoCourseDTO = req.bodyToMono(CourseDTO.class);
        return monoCourseDTO
                .flatMap(e -> service.save(convertEntity(e)))
                .map(this::convertDto)
                .flatMap(e -> ServerResponse.created(URI.create(
                        req.uri().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e)));
    }

    public Mono<ServerResponse> update(ServerRequest req){
        String id = req.pathVariable("id");

        return req.bodyToMono(CourseDTO.class)
                .map(e ->{
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> service.update(convertEntity(e), id))
                .map(this::convertDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e))
                ).switchIfEmpty(ServerResponse.notFound().build());

    }

    public Mono<ServerResponse> delete(ServerRequest req){
        String id = req.pathVariable("id");

        return service.delete(id)
                .flatMap(result ->{
                    if(result){
                        return ServerResponse.noContent().build();
                    }else{
                        return ServerResponse.notFound().build();
                    }
                });
    }


    private CourseDTO convertDto(Course obj){
        return mapper.map(obj, CourseDTO.class);
    }

    private Course convertEntity(CourseDTO dto){
        return mapper.map(dto, Course.class);
    }
}
