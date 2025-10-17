# Etapa 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build

# Establece directorio de trabajo
WORKDIR /app

# Copia pom.xml y descarga dependencias primero (caché)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline

# Copia el resto del proyecto
COPY src ./src

# Compila el jar
RUN ./mvnw clean package -DskipTests

# Etapa 2: Run
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copia el jar compilado de la etapa build
COPY --from=build /app/target/GSVessel-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto que Render asigna
ENV PORT=8080
EXPOSE $PORT

# Start command
ENTRYPOINT ["java","-jar","/app/app.jar"]
