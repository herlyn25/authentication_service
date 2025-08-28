package bootcamp.reto.poweup.r2dbc;

import bootcamp.reto.poweup.model.role.Role;
import bootcamp.reto.poweup.r2dbc.entities.RoleEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface RoleReactiveRepository extends ReactiveCrudRepository<RoleEntity, Long>, ReactiveQueryByExampleExecutor<RoleEntity> {
    public Mono<RoleEntity> findByCode(String code);
}
