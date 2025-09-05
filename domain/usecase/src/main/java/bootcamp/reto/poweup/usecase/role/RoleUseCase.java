package bootcamp.reto.poweup.usecase.role;

import bootcamp.reto.poweup.model.role.Role;
import bootcamp.reto.poweup.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoleUseCase {
  private final RoleRepository roleRepository;
    public Mono<Boolean> findRoleByCode(Long id){
        return roleRepository.findRoleById(id);
    }
}