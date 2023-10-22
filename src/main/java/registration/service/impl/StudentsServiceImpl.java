package registration.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import registration.model.Student;
import registration.repository.IGenericRepo;
import registration.repository.IStudentsRepo;
import registration.service.IStudentService;

@Service
@RequiredArgsConstructor
public class StudentsServiceImpl extends CRUDGeneric<Student, String> implements IStudentService {

    private final IStudentsRepo repo;

    @Override
    protected IGenericRepo<Student, String> getRepo() {
        return repo;
    }

    @Override
    public Flux<Student> findByOrderByAgeDesc() {
        return repo.findByOrderByAgeDesc();
    }

    @Override
    public Flux<Student> findByOrderByAgeAsc() {
        return repo.findByOrderByAgeAsc();
    }
}
