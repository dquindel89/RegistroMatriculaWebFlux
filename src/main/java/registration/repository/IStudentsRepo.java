package registration.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import registration.model.Student;

public interface IStudentsRepo extends IGenericRepo<Student, String> {

    public Flux<Student> findByOrderByAgeDesc();

    public Flux<Student> findByOrderByAgeAsc();
}
