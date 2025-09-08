package bootcamp.reto.poweup.r2dbc;

import bootcamp.reto.poweup.model.ConstanstsModel;
import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.model.user.UserClient;
import bootcamp.reto.poweup.model.user.exceptions.InvalidCredentials;
import bootcamp.reto.poweup.model.user.gateways.UserRepository;
import bootcamp.reto.poweup.r2dbc.entities.UserEntity;
import bootcamp.reto.poweup.model.user.exceptions.EmailAlreadyUsedException;
import bootcamp.reto.poweup.r2dbc.exceptions.CustomNoFoundException;
import bootcamp.reto.poweup.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User, UserEntity, Long, UserReactiveRepository> implements UserRepository {
    private final TransactionalOperator transactionalOperator;

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, entity -> mapper.map(entity,User.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<User> saveUser(User user) {
        log.info("Iniciando el guardado del usuario Repository active");
        return super.save(user).as(transactionalOperator::transactional)
                .doOnNext(userSaved -> log.trace("User created with id: {}", userSaved.getId()))
                .doOnError(error -> log.error("Error in UserReactiveRepositoryAdapter: {}", error));
    }

    @Override
    public Mono<User> findUserByEmail(String email) {
        return super.repository.findByEmail(email)
                .map(entity -> mapper.map(entity,User.class))
                .as(transactionalOperator::transactional)
                .doOnNext(user -> log.trace("User found with email: {}", user.getEmail()))
                .doOnError(error -> log.error("Error in UserReactiveRepositoryAdapter: {}", error));
    }

    @Override
    public Mono<User> authenticateUser(String email, String password) {
        return super.repository.findByEmailAndPassword(email, password)
                .map(entity -> mapper.map(entity,User.class))
                .switchIfEmpty(Mono.error(new InvalidCredentials(ConstanstsModel.INVALID_CREDENTIALS)));
    }

    @Override
    public Mono<UserClient> findUserByParam(String param) {
        return super.repository.findByEmailOrDocumentId(param)
                .map(entity -> mapper.map(entity,UserClient.class))
                .switchIfEmpty(Mono.error(new CustomNoFoundException(ConstanstsModel.USER_NO_FOUND)));
    }
}