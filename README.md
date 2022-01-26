# markets-api

![](https://github.com/rnaufal/markets-api/actions/workflows/ci.yml/badge.svg)
![Coverage](.github/badges/jacoco.svg)

Markets-API is a reactive Kotlin microservice for managing markets built with the SpringBoot framework and the MongoDB database.

# Requirements
* [Java 17](https://openjdk.java.net/projects/jdk/17/) for building the application locally with [Gradle](https://gradle.org).
* [Docker](https://www.docker.com) installed and running as daemon.
* [DockerCompose](https://github.com/docker/compose) to build the application and its dependencies as containers.

# Technologies

* [Kotlin](https://kotlinlang.org)
* [SpringBoot](https://spring.io/projects/spring-boot)
* [Spring Webflux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
* [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
* [SpringDoc](https://springdoc.org)
* [MongoDB](https://www.mongodb.com)
* [Kotlin-Logging](https://github.com/MicroUtils/kotlin-logging)
* [Gradle](https://gradle.org)
* [Docker](https://www.docker.com)
* [DockerCompose](https://github.com/docker/compose)
* [Jib](https://github.com/GoogleContainerTools/jib)
* [Ktlint](https://ktlint.github.io)
* [JUnit5](https://junit.org/junit5)
* [Testcontainers](https://www.testcontainers.org)
* [Mockk](https://mockk.io)
* [AssertJ](https://assertj.github.io/doc)
* [Valiktor](https://github.com/valiktor/valiktor)

## Build and run

```bash
# Clone the repository
$ git clone git@github.com:rnaufal/markets-api.git

# Build the application, run unit and integrated tests, build and start the application and MongoDB containers 
$ make run

```

## Swagger (API documentation)

```
http://localhost:8080/swagger-ui.html
```
