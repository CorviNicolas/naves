# Naves Backend API
Backend de prueba para la API de naves

## Run
Se puede correr con nuestro IDE favorito o bien desde la consola:  

    .\mvnw clean package

y luego  

    .\mvn spring-boot:run

Tener en cuenta que en caso de no tener un servidor Kafka funcionando, aparecerán varios errores en la consola referidos a ésto.  
La manera recomendada de levantar la aplicación es con docker compose como se verá mas abajo.

## Docker
### Docker compose
La aplicación está dockerizada.  


Se puede o bien correr con docker compose (recomendado ya que también va a correr contenedores para zookeeper y kafka).  
Para utilizar docker compose, pararse en la raíz del proyecto y correr:  

    docker compose -f docker-compose.yml up

Esto levantará un contenedor zookeeper, el servidor de Kafka y por último nuestra aplicación en **http://localhost:8080**.   

### Otras opciones
Otra opción es descargar la imagen ncorvi94/naves:1.0.0 del Docker Hub, o bien utilizar el Dockerfile ya existente 
y buildear una nueva imagen con el comando docker build.  
Tener en cuenta que con éstas opciones, se va a necesitar un servidor Kafka funcionando (si se quiere utilizar las funcionalidades de Kafka) 
y editar las properties de kafka según corresponda.  

## H2
Para acceder a la consola h2, levantar la aplicación y dirigirse a http://localhost:8080/h2-console

## Aspect
La aplicación tiene configurado un aspecto el cual generará un log al recibir dentro del servicio de naves, un parametro con nombre id cuyo valor sea negativo

## Kafka
La aplicación produce un evento al crearse una nueva nave.  
Dispone también de un consumer con un listener, el cual genera un log al recibir el evento con el nombre de la nueva nave.  

Esto requiere un servidor Kafka funcionando y configurado en la aplicación.  
La manera más sencilla es seguir los pasos del docker compose más arriba.  

## Manejo de excepciones centralizado
Se utiliza un @RestControllerAdvice el cual retorna las excepciones con una estructura determinada y permite configurar los códigos de error.  
Ver com.w2m.naves.config/GlobalExceptionHandler.java

## Caché
Está configurado el cacheo de una nave en la consulta de naves por id.  

## Swagger
Para acceder a la documentación swagger http://localhost:8080/docs

## Seguridad
La app está segurizada con Basic auth, usuario: ***user*** password ***user***

## Paginado  
La consulta de naves viene con soporte para paginado, utilizando los query params **page** (por defecto 0) y **size** (por defecto 20).  
Tambien dispone de sorting, con el query param **sort**.  

## Librería para facilitar mantenimiento scripts DDL
Se configuró JPA para que genere un script dentro de la raíz del proyecto, llamado ***schema.sql*** (configurable).  
Para generar el script:  
Descomentar property ***spring.jpa.properties.javax.persistence.schema-generation.scripts.action=create***  
Descomentar property ***spring.jpa.properties.javax.persistence.schema-generation.create-source=metadata***  
Generará el script una vez que se apague la aplicación.
