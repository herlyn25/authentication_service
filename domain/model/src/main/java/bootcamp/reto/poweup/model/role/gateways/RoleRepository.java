package bootcamp.reto.poweup.model.role.gateways;

import bootcamp.reto.poweup.model.role.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> save(Role role);
    Mono<Role> findById(Long id);
    Flux<Role> findAll();
    Mono<Void> deleteById(Long id);
}
