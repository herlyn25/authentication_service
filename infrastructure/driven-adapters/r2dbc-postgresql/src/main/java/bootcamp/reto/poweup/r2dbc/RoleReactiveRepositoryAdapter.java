package bootcamp.reto.poweup.r2dbc;

import bootcamp.reto.poweup.model.role.Role;
import bootcamp.reto.poweup.model.role.gateways.RoleRepository;
import bootcamp.reto.poweup.r2dbc.entities.RoleEntity;
import bootcamp.reto.poweup.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role, RoleEntity, Long, RoleReactiveRepository
> implements RoleRepository {
    private final TransactionalOperator transactionalOperator;

    public RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper, TransactionalOperator transactionalOperator) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Role> save(Role role) {
        log.info("Iniciando Guardado del Rol");
        return super.save(role).as(transactionalOperator::transactional)
                .doOnNext(roleSaved -> log.trace("Role created with id: {}", roleSaved.getId()))
                .doOnError(error -> log.error("Error in RoleReactiveRepositoryAdapter: {}", error));

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