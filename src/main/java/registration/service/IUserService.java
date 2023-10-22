package registration.service;

import reactor.core.publisher.Mono;
import registration.model.User;

public interface IUserService extends ICRUDGeneric<User, String>{
    Mono<User> saveHash(User user);
    Mono<registration.security.User> searchByUser(String username);
}
