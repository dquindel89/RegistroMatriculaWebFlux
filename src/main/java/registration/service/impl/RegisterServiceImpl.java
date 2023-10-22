package registration.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import registration.model.Register;
import registration.repository.IGenericRepo;
import registration.repository.IRegisterRepo;
import registration.service.IRegisterService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl extends CRUDGeneric<Register, String> implements IRegisterService {

   private  final IRegisterRepo getRepo;

    @Override
    protected IGenericRepo<Register, String> getRepo() {
        return getRepo;
    }

    public Mono<Register> saveRegisterWithPrePersistenceAction(Register register ){
        register.setRegistrationDate(LocalDateTime.now());
        return getRepo.save(register);
    }
}
