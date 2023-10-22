package registration.service.impl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import registration.repository.IGenericRepo;
import registration.service.ICRUDGeneric;

public abstract class CRUDGeneric<T, ID> implements ICRUDGeneric<T, ID> {

    protected abstract IGenericRepo<T, ID> getRepo();
    @Override
    public Flux<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    public Mono<T> save(T t) {
        return getRepo().save(t);
    }

    @Override
    public Mono<T> update(T t, ID id) {
        return getRepo().save(t);
    }

    @Override
    public Mono<T> findById(ID id) {
        return getRepo().findById(id);
    }

    @Override
    public Mono<Boolean> delete(ID id) {
        return getRepo().deleteById(id)
                .hasElement()
                .flatMap(result ->{
                    if(result){
                        return getRepo().deleteById(id).thenReturn(true);
                    }else {
                        return Mono.just(false);
                    }
                });
    }
}
