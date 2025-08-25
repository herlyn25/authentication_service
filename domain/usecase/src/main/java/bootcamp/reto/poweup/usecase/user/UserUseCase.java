package bootcamp.reto.poweup.usecase.user;

import bootcamp.reto.poweup.model.role.Role;
import bootcamp.reto.poweup.model.role.gateways.RoleRepository;
import bootcamp.reto.poweup.model.user.User;
import bootcamp.reto.poweup.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Mono<User> saveUser(User user){
        return userRepository.save(user);
    }

    public Mono<Boolean> existsUserByEmail(String email){
        return userRepository.existsEmail(email);
    }

     public Mono<User> findUserById(Long id){
        return userRepository.findById(id);
    }
       
    public Mono<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Flux<User> findUserAll(){
        return userRepository.findAll();
    }

    public Mono<Void> deleteUserById(Long id){
        return userRepository.deleteById(id);
    }
}