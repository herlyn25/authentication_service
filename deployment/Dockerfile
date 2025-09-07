# deployment/Dockerfile
FROM eclipse-temurin:21-jdk-alpine

# Crear usuario no-root para seguridad
#RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR de la aplicación
COPY ../applications/app-service/build/libs/crediya_authentication.jar /app/crediya_authentication.jar
COPY .deploy.env /app/.env
# Cambiar propietario del archivo
#RUN chown appuser:appgroup /app/crediya_authentication.jar

# Crear directorio para caché (si es necesario)
#RUN mkdir -p /cache && chown appuser:appgroup /cache

# Variables de entorno
#ENV JAVA_OPTS="-Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
#ENV SERVER_PORT=8080

# Exponer puerto
EXPOSE 8080

# Cambiar a usuario no-root
#USER appuser

# Punto de entrada
ENTRYPOINT ["java","-jar","crediya_authentication.jar"]