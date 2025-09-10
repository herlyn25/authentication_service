package bootcamp.reto.poweup.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey secretKey;

    @PostConstruct
    void init() {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    @Bean
    ReactiveJwtDecoder jwtDecoder(){
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
    }

    // Convert "roles" claim -> ROLE_* for what functions hasRole("...")
    private ReactiveJwtAuthenticationConverter jwtAuthConverter() {
        var roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("roles");  // tu claim
        roles.setAuthorityPrefix("ROLE_");       // requerido por hasRole

        var converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(
                new ReactiveJwtGrantedAuthoritiesConverterAdapter(roles)
        );
        return converter;
    }

    @Bean
    SecurityWebFilterChain springSecurityPermissions(ServerHttpSecurity http, ReactiveJwtDecoder decoder) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex->ex
                        .pathMatchers(
                        "/api/v1/login",
                        "/v3/api-docs/**",
                        "/swagger-ui/**"/*,
                       "/api/v1/users/**",
                       "/api/v1/apps"*/)
                        .permitAll()
                        .pathMatchers(HttpMethod.POST,"/api/v1/users").hasAnyRole("ADMIN","ASESOR")
                        .pathMatchers(HttpMethod.GET,"/api/v1/users").hasRole("ASESOR")                        
                        .pathMatchers(HttpMethod.POST,"/api/v1/apps").hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET,"/api/v1/apps").hasRole("ASESOR")
                        .anyExchange().authenticated())
                        .oauth2ResourceServer(oauth-> oauth
                        .jwt(jwt->jwt
                                .jwtDecoder(decoder)
                                .jwtAuthenticationConverter(jwtAuthConverter())))
                .build();
    }
}
