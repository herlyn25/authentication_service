package bootcamp.reto.poweup.r2dbc;

import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.model.user.gateways.UserRepository;
import bootcamp.reto.poweup.r2dbc.entities.UserEntity;
import bootcamp.reto.poweup.model.user.exceptions.EmailAlreadyUsedException;
import bootcamp.reto.poweup.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
    User, UserEntity, Long, UserReactiveRepository> implements UserRepository {

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, entity -> mapper.map(entity,User.class));
    }

    @Override
    public Mono<User> saveUser(User user) {
        return super.save(user);
    }

    @Override
    public Mono<User> findUserByDocumentId (String id) {
        return super.repository.findByDocumentId(id);
    }

    @Override
    public Mono<User> findUserByEmail(String email) {
        return super.repository.findByEmail(email)
                .map(entity -> mapper.map(entity,User.class));
    }

    @Override
    public Flux<User> findUsersAll(){
        return super.findAll();
    }
}