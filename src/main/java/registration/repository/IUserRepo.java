package registration.repository;

import reactor.core.publisher.Mono;
import registration.model.User;

public interface IUserRepo extends IGenericRepo<User, String>{
    Mono<User> findOneByUsername(String username);
}
