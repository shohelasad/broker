# Yield Lab - auction demo


## Prerequisites

* Docker 19.03.x (for production level readiness)
* Docker Compose 1.25.x

## Used Technologies

* Spring Boot 2.3.4
* Postgresql (for production level readiness)
* Spring Boot Validation
* Spring Boot Jpa
* Spring Boot Actuator
* Lombok
* Dev Tools
* Open Api 3




### Run as a Spring boot 

```sh
mvn spring-boot:run
```

### Run only test cases 

```sh
mvn test
```

### Package the application as a JAR file

```sh
mvn clean install -DskipTests
```

### Run the Spring Boot application with Docker Compose

```sh
docker-compose up -d
```




## Test API with Swagger 
http://localhost:8080/swagger-ui/index.html?configUrl=/api-docs/swagger-config#/

## Exception
* RestClientException: In case of any bidders are not reachable, then show error message in log with RestClientException. And calculate the highest bid for others bidder
* InterruptedException | ExecutionException: Interrupt or execution exception for future call
* BadRequestException: Param request with empty value will throw BadRequestException
* ResourceNotfound: In case of all bidder are not reachable, then show error message in log with RestClientException. And throw ResourceNotFound exception because no bid response from bidders to Auction Service

## Note
* Sample Unit tests are implemented for controller, service



