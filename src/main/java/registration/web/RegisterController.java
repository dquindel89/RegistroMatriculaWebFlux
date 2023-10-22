package registration.web;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import registration.model.Register;
import registration.model.dto.RegisterDTO;
import registration.service.impl.RegisterServiceImpl;

import java.net.URI;

@RestController
@RequestMapping("/registers")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterServiceImpl service;
    private final ModelMapper mapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<RegisterDTO>>> listAll(){
        Flux<RegisterDTO> fx = service.findAll()
                                .map(this::convertDto);

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<RegisterDTO>> create(@RequestBody RegisterDTO dto, final ServerHttpRequest req){

        Register obj = convertEntity(dto);

        return service.saveRegisterWithPrePersistenceAction(obj)
               // save(convertEntity(dto))
                .map(this::convertDto)
                .map(e -> ResponseEntity.created(
                        URI.create(req.getURI().toString().concat("/").concat(e.getId()))
                ).contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());


    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<RegisterDTO>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(this::convertDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<RegisterDTO>> update(@RequestBody RegisterDTO registerDTO, @PathVariable("id") String id){
        return Mono.just(registerDTO)
                .map(e -> {
                    e.getId();
                    return e;
                })
                .flatMap(e -> service.update(convertEntity(registerDTO), id))
                .map(this::convertDto)
                .map(e -> ResponseEntity.ok()
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> delete(@PathVariable("id") String id){
        return service.delete(id)
                .flatMap(result -> {
                    if (result){
                        return Mono.just(ResponseEntity.noContent().build());
                    }else {
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                })
                .defaultIfEmpty(ResponseEntity.notFound().build());

    }

    private RegisterDTO convertDto(Register obj){
        return mapper.map(obj, RegisterDTO.class);
    }

    private Register convertEntity(RegisterDTO dto){
        return mapper.map(dto, Register.class);
    }

}
