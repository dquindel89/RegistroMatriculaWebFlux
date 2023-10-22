package registration.handler;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import registration.model.Student;
import registration.model.dto.StudentDTO;
import registration.service.IStudentService;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class StudentHandler {

    private final IStudentService service;

    private final ModelMapper mapper;

    public Mono<ServerResponse> findAll(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll().map(this::convertDto), StudentDTO.class);
    }

    public Mono<ServerResponse> findDescAge(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findByOrderByAgeDesc().map(this::convertDto), StudentDTO.class);
    }

    public Mono<ServerResponse> findAscAge(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findByOrderByAgeAsc().map(this::convertDto), StudentDTO.class);
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
        Mono<StudentDTO> monoStudentDTO = req.bodyToMono(StudentDTO.class);
        return monoStudentDTO
                .flatMap(e -> service.save(convertEntity(e)))
                .map(this::convertDto)
                .flatMap(e -> ServerResponse.created(URI.create(
                        req.uri().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e)));
    }

    public Mono<ServerResponse> update(ServerRequest req){
        String id = req.pathVariable("id");

        return req.bodyToMono(StudentDTO.class)
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


    private StudentDTO convertDto(Student obj){
        return mapper.map(obj, StudentDTO.class);
    }

    private Student convertEntity(StudentDTO dto){
        return mapper.map(dto, Student.class);
    }
}
