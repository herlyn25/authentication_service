package bootcamp.reto.poweup.r2dbc;

import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.r2dbc.entities.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {
Mono<UserEntity> findByEmail(String email);
Mono<User> findByEmailAndPassword(String email, String password);
}
