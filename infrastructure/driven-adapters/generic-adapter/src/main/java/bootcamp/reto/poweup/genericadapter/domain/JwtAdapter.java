package bootcamp.reto.poweup.genericadapter.domain;

import bootcamp.reto.poweup.model.user.auth.JwtTokenInfo;
import bootcamp.reto.poweup.model.user.gateways.JwtTokenRepository;
import bootcamp.reto.poweup.r2dbc.constants.ConstantsDA;
import bootcamp.reto.poweup.r2dbc.exceptions.ExpiratedTokenException;
import bootcamp.reto.poweup.r2dbc.exceptions.ExtractNameException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtAdapter implements JwtTokenRepository {
   @Value("${jwt.secret}")
    private String jwtScret;

   @Value("${jwt.expiration}")
    private Long jwtExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtScret.getBytes());
    }

    @Override
    public Mono<String> generateToken(String email) {
        log.debug("Generating JWT Token for email: {}", email);
        log.debug("Genero Correctamente el token para {}", jwtScret);
        return Mono.fromCallable(()->{
            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", email);
            claims.put("created", new Date());
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(email)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }).subscribeOn(Schedulers.boundedElastic());

    }

    @Override
    public Mono<JwtTokenInfo> validateToken(String token) {
        return Mono.fromCallable(()->{
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();
                boolean expired = claims.getExpiration().before(new Date());

                return JwtTokenInfo.builder()
                        .email(email)
                        .token(token)
                        .isValid(true)
                        .isExpired(expired)
                        .build();
                }).doOnError(error -> new ExpiratedTokenException(ConstantsDA.EXPIRED_TOKEN))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<String> extractEmail(String token) {
        return Mono.fromCallable(()-> {
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(getSigningKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
                    return claims.getSubject();
                }).doOnError(error -> new ExtractNameException(ConstantsDA.NO_EXTRACTED_NAME))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Boolean> isTokenExpired(String token) {
        return Mono.fromCallable(() -> {

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                return claims.getExpiration().before(new Date());

        }).doOnError(error-> new ExpiratedTokenException(ConstantsDA.EXPIRED_TOKEN))
                .subscribeOn(Schedulers.boundedElastic());
    }
}