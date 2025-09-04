package bootcamp.reto.poweup.model.role.gateways;

import bootcamp.reto.poweup.model.role.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Boolean> findRoleById(Long id);
}
