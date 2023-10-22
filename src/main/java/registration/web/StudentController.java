package registration.web;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import registration.model.Student;
import registration.model.dto.StudentDTO;
import registration.service.impl.StudentsServiceImpl;

import java.net.URI;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentsServiceImpl service;
    private final ModelMapper mapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<StudentDTO>>> listAll(){
        Flux<StudentDTO> fx = service.findAll()
                                .map(this::convertDto);

        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/orderDesc")
    public Mono<ResponseEntity<Flux<StudentDTO>>> listDescAgeStudent(){
        Flux<StudentDTO> fx = service.findByOrderByAgeDesc()
                .map(this::convertDto);
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/orderAsc")
    public Mono<ResponseEntity<Flux<StudentDTO>>> listAscAgeStudent(){
        Flux<StudentDTO> fx = service.findByOrderByAgeAsc()
                .map(this::convertDto);
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<StudentDTO>> create(@RequestBody StudentDTO dto, final ServerHttpRequest req){
        return service.save(convertEntity(dto))
                .map(this::convertDto)
                .map(e -> ResponseEntity.created(
                        URI.create(req.getURI().toString().concat("/").concat(e.getId()))
                ).contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<StudentDTO>> findById(@PathVariable("id") String id){
        return service.findById(id)
                .map(this::convertDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<StudentDTO>> update(@RequestBody StudentDTO studentDTO, @PathVariable("id") String id){
        return Mono.just(studentDTO)
                .map(e -> {
                    e.getId();
                    return e;
                })
                .flatMap(e -> service.update(convertEntity(studentDTO), id))
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



    private StudentDTO convertDto(Student obj){
        return mapper.map(obj, StudentDTO.class);
    }

    private Student convertEntity(StudentDTO dto){
        return mapper.map(dto, Student.class);
    }

}
