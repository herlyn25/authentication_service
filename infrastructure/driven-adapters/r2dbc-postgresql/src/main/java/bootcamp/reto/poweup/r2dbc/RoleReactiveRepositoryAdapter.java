package bootcamp.reto.poweup.r2dbc;

import bootcamp.reto.poweup.model.role.Role;
import bootcamp.reto.poweup.model.role.gateways.RoleRepository;
import bootcamp.reto.poweup.r2dbc.entities.RoleEntity;
import bootcamp.reto.poweup.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role, RoleEntity, Long, RoleReactiveRepository
> implements RoleRepository {
    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
    }

    @Override
    public Mono<Role> save(Role role) {
        return super.save(role);
    }

    @Override
    public Mono<Role> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Flux<Role> findAll() {
        return super.findAll();
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }
}