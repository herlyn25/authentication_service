package bootcamp.reto.poweup.usecase.role;

import bootcamp.reto.poweup.model.role.Role;
import bootcamp.reto.poweup.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RoleUseCase {
    private final RoleRepository roleRepository;

    public Mono<Role> saveRole(Role role){
        return roleRepository.save(role);
    }
    public Mono<Role> findRoleById(Long id){ return roleRepository.findById(id);}
    public Flux<Role> findAllRoles(){ return roleRepository.findAll(); }
    public Mono<Void> deleteRoleById(Long id){ return roleRepository.deleteById(id);}

}
