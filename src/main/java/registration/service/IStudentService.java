package registration.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import registration.model.Student;

public interface IStudentService extends ICRUDGeneric<Student, String> {

    Flux<Student> findByOrderByAgeDesc();
    Flux<Student> findByOrderByAgeAsc();
}
