# -------------------------------
# Etapa 1: Build
# -------------------------------
FROM eclipse-temurin:21-jdk-alpine AS build

# Directorio de trabajo
WORKDIR /app

# Copiamos el wrapper de Maven y le damos permisos
COPY mvnw .
RUN chmod +x mvnw

# Copiamos la carpeta .mvn y el pom
COPY .mvn .mvn
COPY pom.xml .

# Descargar dependencias primero (cache)
RUN ./mvnw dependency:go-offline

# Copiamos el resto del proyecto
COPY src ./src

# Compilamos el jar (sin tests para no romper deploy)
RUN ./mvnw clean package -DskipTests

# -------------------------------
# Etapa 2: Run
# -------------------------------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copiamos el jar compilado desde la etapa build
COPY --from=build /app/target/GSVessel-0.0.1-SNAPSHOT.jar app.jar

# Puerto que Render asigna
ENV PORT=8080
EXPOSE $PORT

# Comando de arranque
ENTRYPOINT ["java","-jar","/app/app.jar"]
