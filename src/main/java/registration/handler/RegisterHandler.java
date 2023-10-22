package registration.handler;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import registration.model.Register;
import registration.model.dto.RegisterDTO;
import registration.service.IRegisterService;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class RegisterHandler {

    private final IRegisterService service;

    private final ModelMapper mapper;

    public Mono<ServerResponse> findAll(ServerRequest req){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll().map(this::convertDto), RegisterDTO.class);
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
        Mono<RegisterDTO> monoRegisterDTO = req.bodyToMono(RegisterDTO.class);
        return monoRegisterDTO
                .flatMap(e -> service.save(convertEntity(e)))
                .map(this::convertDto)
                .flatMap(e -> ServerResponse.created(URI.create(
                        req.uri().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(e)));
    }

    public Mono<ServerResponse> update(ServerRequest req){
        String id = req.pathVariable("id");

        return req.bodyToMono(RegisterDTO.class)
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


    private RegisterDTO convertDto(Register obj){
        return mapper.map(obj, RegisterDTO.class);
    }

    private Register convertEntity(RegisterDTO dto){
        return mapper.map(dto, Register.class);
    }
}
