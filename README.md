Documentación del Proyecto: CRUD System

Descripción:

Este es un sistema básico de gestión (CRUD) diseñado para practicar la integración entre un backend robusto en Java y un frontend dinámico. 

El enfoque principal es el aprendizaje de arquitecturas escalables y estándares de industria.

Repositorio oficial: https://github.com/jacobugat/java-springboot-crud

Arquitectura y Diseño:

Para este proyecto se implementó una estructura que separa las responsabilidades para facilitar el mantenimiento:

Arquitectura: MVC (Modelo-Vista-Controlador).

- Modelo: Gestión de datos y lógica de base de datos.
- Vista: Interfaz de usuario (Angular / JSON).
- Controlador: Gestión de rutas y peticiones HTTP.

Patrones de Diseño:

- Repository Pattern: Centraliza el acceso a los datos, desacoplando la lógica de negocio de la base de datos.

- Dependency Injection (DI): Delegamos la creación de objetos al framework (Spring Boot) mediante @Autowired.

- Data Transfer Object (DTO): (Opcional, pero recomendado) para mover datos entre capas de forma segura.

Tecnologías Usadas

- Lenguaje: Java 21 (LTS).
- Framework Backend: Spring Boot 3.x.
- Framework Frontend: Angular.
- Persistencia: Spring Data JPA / Hibernate.
- Base de Datos: SQL (H2 para pruebas / PostgreSQL para producción).
- Herramientas: IntelliJ IDEA, Maven y Git.

Guía de Instalación y Ejecución

1. Requisitos Previos

- Instalar JDK 21.
- Instalar IntelliJ IDEA (u otro IDE compatible).
- Contar con un gestor de paquetes como Maven (incluido en el proyecto).

2. Configuración del Backend

- Descargar el esqueleto del proyecto desde Spring Initializr con las dependencias necesarias (Web, JPA, SQL Driver, Lombok).
- Importar el proyecto en IntelliJ.
- Ejecutar la clase principal CrudApplication.java.

3. Verificación

- Terminal: El proyecto se ha iniciado correctamente si visualizas el mensaje:
- Started CrudApplication in X.XXX seconds
- Navegador: Accede a http://localhost:8080. Si ves la página de error por defecto (Whitelabel Error Page), el servidor está activo y respondiendo.

4. Control de Versiones (Git)

Para mantener el flujo de trabajo y el respaldo en la nube, se utiliza Git con los siguientes comandos estándar:

- Descargar el proyecto (por primera vez):
`git clone https://github.com/jacobugat/java-springboot-crud.git`

- Actualizar tu código local (antes de empezar a trabajar):
`git pull`

- Preparar cambios:
`git add .`

- Confirmar avance:
`git commit -m "Descripción de lo que hiciste"`

- Sincronizar con GitHub:
`git push`
