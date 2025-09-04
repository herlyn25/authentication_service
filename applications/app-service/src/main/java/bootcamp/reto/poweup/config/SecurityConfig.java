package bootcamp.reto.poweup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityPermissions(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex-> ex
                        .pathMatchers(
                                "/api/v1/users",
                                "/api/v1/login",
                                "/v3/api-docs/**",
                                "/swagger-ui/**")
                        .permitAll()
                        .anyExchange().authenticated())
                .httpBasic((ServerHttpSecurity.HttpBasicSpec::disable))
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();

    }
}
