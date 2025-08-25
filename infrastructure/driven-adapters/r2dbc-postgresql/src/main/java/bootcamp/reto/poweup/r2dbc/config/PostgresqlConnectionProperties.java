package bootcamp.reto.poweup.r2dbc.config;

// TODO: Load properties from the application.yaml file or from secrets manager
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "postgres")
public record PostgresqlConnectionProperties(
        String host,
        Integer port,
        String database,
        String schema,
        String username,
        String password) {
}
