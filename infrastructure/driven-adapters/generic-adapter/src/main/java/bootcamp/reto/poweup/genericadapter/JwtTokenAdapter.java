package bootcamp.reto.poweup.genericadapter;

import bootcamp.reto.poweup.model.ConstanstsModel;
import bootcamp.reto.poweup.model.auth.gateways.JwtTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Component
public class JwtTokenAdapter implements JwtTokenRepository {
   @Value("${jwt.secret}")
    private String jwtScret;

   @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtScret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<String> generateToken(String email, Long roleId) {
        String role = ConstanstsModel.ROLES_MAP.get(roleId).toUpperCase();

        log.debug("Generating JWT Token for email: {}", email);
        log.debug("Genero Correctamente el token para {}", jwtScret);
        return Mono.fromCallable(()->{
            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", email);
            claims.put("created", new Date());
            claims.put("roles", List.of(role));
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(email)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(getSigningKey())
                    .compact();
        }).subscribeOn(Schedulers.boundedElastic());

    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(()->{
            Claims claims = Jwts.parser()
                            .setSigningKey(jwtScret)
                            .parseClaimsJws(token)
                            .getBody();
            Date expiration = claims.getExpiration();
                return expiration.after(new Date());
        }).onErrorReturn(false);
}
}