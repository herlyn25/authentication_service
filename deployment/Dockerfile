# deployment/Dockerfile
FROM eclipse-temurin:21-jdk-alpine

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR de la aplicación
COPY ../applications/app-service/build/libs/crediya_authentication.jar /app/crediya_authentication.jar
COPY .env /app/.env

# Exponer puerto
EXPOSE 8080

# Punto de entrada
ENTRYPOINT ["java","-jar","crediya_authentication.jar"]