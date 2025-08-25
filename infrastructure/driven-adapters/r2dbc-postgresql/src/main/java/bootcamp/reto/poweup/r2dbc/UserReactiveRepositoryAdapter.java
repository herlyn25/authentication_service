package bootcamp.reto.poweup.r2dbc;

import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.model.user.gateways.UserRepository;
import bootcamp.reto.poweup.r2dbc.entities.UserEntity;
import bootcamp.reto.poweup.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User, UserEntity, Long, UserReactiveRepository> implements UserRepository {

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity,User.class));
    }

    @Override
    public Mono<User> save(User user) {
        return Mono.fromCallable(()->mapper.map(user,UserEntity.class))
                .flatMap(repository::save)
                .map(entity -> mapper.map(entity,User.class));
    }

    @Override
    public Mono<Boolean> existsEmail(String email) {
        return repository.existsByEmail(email)
                .doOnError(exists -> {
                    throw new RuntimeException("Email already exists");
                });
    }

    @Override
    public Mono<User> findById(Long id) {
        return super.findById(id).map(entity -> mapper.map(entity,User.class))
                .doOnSuccess(Mono::just)
                .doOnError(exists -> {
                    throw new RuntimeException("User not found");
                });
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> mapper.map(entity,User.class))
                .doOnSuccess(Mono::just).doOnError(exists -> {
                    throw new RuntimeException(String.format("User with email %s not found", email));
                });
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }
}
