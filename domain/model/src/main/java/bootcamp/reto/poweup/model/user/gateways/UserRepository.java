package bootcamp.reto.poweup.model.user.gateways;

import bootcamp.reto.poweup.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {    
    Mono<User> save(User user);
    Mono<Boolean> existsEmail(String email);
    Mono<User>findById(Long id);
    Mono<User>findByEmail(String email);
    Flux<User> findAll();
    Mono<Void> deleteById(Long id);
}