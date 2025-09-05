package bootcamp.reto.poweup.usecase.user;

import bootcamp.reto.poweup.model.role.gateways.RoleRepository;
import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.model.user.exceptions.EmailAlreadyUsedException;
import bootcamp.reto.poweup.model.user.gateways.UserRepository;
import bootcamp.reto.poweup.model.user.validations.UserDomainValidation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Mono<User> save(User user) {
        return UserDomainValidation.validateUser(user)
                .flatMap(validUser -> {
                    return roleRepository.findRoleById(validUser.getRole_id())
                            .flatMap(role -> {
                                return userRepository.findUserByEmail(validUser.getEmail())
                                        .hasElement()
                                        .flatMap(exists -> {
                                            if (exists) {
                                                return Mono.error(new EmailAlreadyUsedException(validUser.getEmail()));
                                            }
                                            return userRepository.saveUser(validUser);
                                        });
                            });
                });
    }
}