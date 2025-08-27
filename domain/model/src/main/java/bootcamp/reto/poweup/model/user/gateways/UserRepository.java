package bootcamp.reto.poweup.model.user.gateways;

import bootcamp.reto.poweup.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> saveUser(User user);

    Mono<User> findByDocumentId(String id);

    Mono<User> findUserByEmail(String email);

    Flux<User> findUsersAll();
}