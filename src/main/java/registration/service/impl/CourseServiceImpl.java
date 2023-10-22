package registration.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import registration.model.Course;
import registration.repository.ICourseRepo;
import registration.repository.IGenericRepo;
import registration.service.ICourseService;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends CRUDGeneric<Course,String> implements ICourseService {

    private final ICourseRepo repo;
    @Override
    protected IGenericRepo<Course, String> getRepo() {
        return repo;
    }
}
