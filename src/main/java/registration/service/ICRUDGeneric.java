package registration.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICRUDGeneric<T, ID> {

    public Flux<T> findAll();
    public Mono<T> save(T t);

    public Mono<T> update(T t, ID id);

    public Mono<T> findById(ID id);

    public Mono<Boolean> delete(ID id);
}
