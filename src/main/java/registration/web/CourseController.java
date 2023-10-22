package registration.web;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import registration.model.Course;
import registration.model.dto.CourseDTO;
import registration.service.impl.CourseServiceImpl;

import java.net.URI;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl service;
    private final ModelMapper mapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<CourseDTO>>> listAll(){
        Flux<CourseDTO> fx = service.findAll()
                                .map(this::convertDto);

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<CourseDTO>> create(@RequestBody CourseDTO dto, final ServerHttpRequest req){
        return service.save(convertEntity(dto))
                .map(this::convertDto)
                .map(e -> ResponseEntity.created(
                        URI.create(req.getURI().toString().concat("/").concat(e.getId()))
                ).contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CourseDTO>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(this::convertDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CourseDTO>> update(@RequestBody CourseDTO courseDTO, @PathVariable("id") String id){
        return Mono.just(courseDTO)
                .map(e -> {
                    e.getId();
                    return e;
                })
                .flatMap(e -> service.update(convertEntity(courseDTO), id))
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

    private CourseDTO convertDto(Course obj){
        return mapper.map(obj, CourseDTO.class);
    }

    private Course convertEntity(CourseDTO dto){
        return mapper.map(dto, Course.class);
    }

}
