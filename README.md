🛥️ GSVessel

GSVessel es un sistema backend desarrollado con Spring Boot para asistir a los armadores de barcos pesqueros en la gestión integral de sus flotas.
Permite administrar tripulación, equipamiento y mantenimiento de cada embarcación mediante una API REST segura y escalable.

------------------------------------------------------------
🚀 Tecnologías utilizadas
------------------------------------------------------------
- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA / Hibernate
- MySQL
- Maven

------------------------------------------------------------
⚙️ Funcionalidades principales
------------------------------------------------------------
- Gestión de tripulación: alta, baja y actualización de los miembros del equipo.
- Gestión de equipamiento: registro y control del equipamiento de cada embarcación.
- Mantenimiento: planificación y registro de tareas de mantenimiento.
- Autenticación y autorización: login con JWT implementado con Spring Security.
- Arquitectura por capas: Controller, Service, Repository.
- Manejo global de errores y validaciones.

------------------------------------------------------------
🛠️ Configuración e instalación
------------------------------------------------------------
1. Clonar el repositorio:
   git clone https://github.com/ramirosacchetta/gsvessel.git

2. Crear la base de datos en MySQL:
   CREATE DATABASE gsvessel;

3. Configurar el archivo application.properties:
   spring.datasource.url=jdbc:mysql://localhost:3306/gsvessel
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.security.jwt.secret=tu_clave_secreta

4. Ejecutar el proyecto:
   mvn spring-boot:run

------------------------------------------------------------
📡 Endpoints principales (ejemplo)
------------------------------------------------------------
POST /auth/login          -> Inicia sesión y genera token JWT
GET  /crew                -> Lista todos los miembros de la tripulación
POST /crew                -> Crea un nuevo miembro
GET  /equipment           -> Lista el equipamiento disponible
PUT  /maintenance/{id}    -> Actualiza tarea de mantenimiento

------------------------------------------------------------
👨‍💻 Autores
------------------------------------------------------------
Ramiro Sacchetta  
Valentín Sacchetta

Desarrolladores Backend Java
