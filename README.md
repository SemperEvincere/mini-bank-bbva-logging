# Mini Aplicación Bancaria

Este es un proyecto de una mini aplicación bancaria desarrollada como parte de un laboratorio de capacitación en
tecnologías Java y Spring.
Corresponde al laboratorio 2 del modulo V de la capacitación en desarrollo backend del banco BBVA 2023.

## Descripción

La mini aplicación bancaria permite a los clientes crear su usuario y gestionar sus cuentas bancarias. Los usuarios
pueden realizar acciones como cargar saldo, consultar el estado de la cuenta, tener un cotitular y realizar
transferencias a otras cuentas de clientes del banco.

La aplicación utiliza tecnologías como Java 17, Tomcat, MySQL, Spring, Hibernate, Arquitectura Hexagonal, Spring
Security, Log4j y Swagger.

## Estructura del Proyecto

El proyecto sigue una estructura basada en la arquitectura hexagonal y se organiza en los siguientes paquetes
principales:

- `application`: Contiene los servicios de aplicación que implementan la lógica de negocio y orquestan las operaciones.
- `domain`: Contiene las entidades del dominio y las interfaces de repositorio.
- `infrastructure`: Contiene las implementaciones de los repositorios y otros componentes de infraestructura, como
  mapeadores.
- `presentation`: Contiene los controladores REST para manejar las solicitudes HTTP.

## Requisitos

Asegúrate de tener instalado lo siguiente en tu entorno de desarrollo:

- Java Development Kit (JDK) 17 o superior.
- Tomcat 9 o superior.
- MySQL Server.
- Maven.

## Configuración

Sigue estos pasos para configurar y ejecutar el proyecto:

1. Clona este repositorio en tu máquina local.
2. Importa el proyecto en tu entorno de desarrollo preferido.
3. Configura las credenciales de la base de datos en el archivo `application.properties` ubicado en la
   carpeta `src/main/resources`.
4. Ejecuta los scripts de inicialización de la base de datos ubicados en la carpeta `database` para crear las tablas y
   datos iniciales.
5. Ejecuta la aplicación en tu entorno de desarrollo o genera el archivo WAR y despliégalo en tu servidor Tomcat.

## Documentación de la API

La documentación de la API se genera automáticamente utilizando Swagger. Para acceder a la documentación de la API,
sigue estos pasos:

1. Ejecuta la aplicación.
2. Abre tu navegador y navega a `http://localhost:8080/swagger-ui.html`.

## Licencia

Este proyecto está licenciado bajo [MIT License](LICENSE).


