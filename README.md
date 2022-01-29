# markets-api

![](https://github.com/rnaufal/markets-api/actions/workflows/ci.yml/badge.svg)
![Coverage](.github/badges/jacoco.svg)

Markets-API is a reactive Kotlin microservice for managing markets built with the SpringBoot framework and the MongoDB database.

# Requirements
* [Java 17](https://openjdk.java.net/projects/jdk/17/) for building the application with [Gradle](https://gradle.org).
* [Docker](https://www.docker.com) should be installed and running as daemon.
* [DockerCompose](https://github.com/docker/compose) should be installed to build the application and its dependencies as containers.
* [Make](https://www.gnu.org/software/make/) as a tool to build the application artifacts.

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
* [JaCoCo](https://www.jacoco.org/jacoco/trunk/index.html)
* [Mockk](https://mockk.io)
* [AssertJ](https://assertj.github.io/doc)
* [Valiktor](https://github.com/valiktor/valiktor)
* [Logback](https://logback.qos.ch/)

## Makefile

The application uses a *Makefile* which contains some commands to build the application, run it and create its image to run on Docker.

## Building and running the application

```bash
# Clone the repository
$ git clone git@github.com:rnaufal/markets-api.git

# Build the application, run unit and integration tests, build and start the application and MongoDB containers 
$ make run
```

The application should be up and running at `http://localhost:8080` address.

## Test summary and JaCoCo coverage

The application uses the JaCoCo report coverage tool to measure code coverage. The test summary and JaCoCo report coverage can be found under the project directory at the following paths:

```bash
{PROJECT_DIR}/build/reports/tests/test/index.html
{PROJECT_DIR}/build/reports/jacoco/test/html/index.html
```

where *PROJECT_DIR* is the absolute path in which the repository was cloned.

## Logs

The log file can be found at the following path:

```bash
/tmp/logs/markets-api/markets-api.log
```

## CI/CD

GitHub Actions is used to set up the environment (JDK 17, Kotlin, Docker), build the application, test and create the application image on Docker daemon at every pushed commit on branch *master*. 

[Here](https://github.com/rnaufal/markets-api/blob/master/.github/workflows/ci.yml) is the workflow file used to compile, build, test and deploy the application. [Here](https://github.com/rnaufal/markets-api/runs/4959470787?check_suite_focus=true) is an example of some workflow run to build the application. And [here](https://github.com/rnaufal/markets-api/actions) is all the workflow runs which continuosly build the application. 

Each workflow run produces the following outputs:

1. Test results - [Example](https://github.com/rnaufal/markets-api/runs/4959489623?check_suite_focus=true)
2. JaCoCO report - [Example](https://github.com/rnaufal/markets-api/suites/5076343453/artifacts/151129187)

The JaCoCO report can be downloaded as a *zip* file containing the Jacoco test reports.

## Health check

```bash
http://localhost:8080/actuator/health
```

## Kubernetes (K8s) probes

```bash
# It checks the internal state of the application 
http://localhost:8080/actuator/health/liveness

# It checks whether the application is ready to receive traffic including the validation of dependencies state, in this case, the MongoDB database
http://localhost:8080/actuator/health/readiness
```

## Swagger (API documentation)

```
http://localhost:8080/swagger-ui.html
```

## Architecture 

The application architecture follows the [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html).

### Design

Here are some design decisions that were made in the project:

- MongoDB was chosen as the database because of its *schemaless* architecture and the uncertainty about the markets dataset structure in advance.
- Some simple validations were applied to the markets dataset for validating nullable and positive fields.
- The *number* and *reference* fields were not validated because they could be nullable according to the dataset. 
- As MongoDB has its own *id* for each document, the markets dataset *ID* field was imported into a specific field called *legacyIdentifier* to keep track of it.
- The market field named as *REGISTRO*, which is mapped as *registryCode* in the domain, is not updatable and thus is considered unique between the markets. An unique index on MongoDB was created to enforce this rule.

### JSON API Response

The JSON response has the following mapping within the markets dataset:

| Field | JSON |
| ------ | ------ |
| ID | legacyIdentifier |
| LONG | longitude |
| LAT | latitude |
| SETCENS | setCens |
| AREAP | area |
| CODDIST | districtCode |
| DISTRITO | district |
| CODSUBPREF | townCode |
| SUBPREFE | town |
| REGIAO5 | firstZone |
| REGIAO8 | secondZone |
| NOME_FEIRA | name |
| REGISTRO | registryCode |
| LOGRADOURO | publicArea |
| NUMERO | number |
| BAIRRO | neighborhood |
| REFERENCIA | reference |

## Examples

1. Create a new market with success

### Request

```bash
curl -X 'POST' \
  'http://localhost:8080/api/v1/markets' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "legacyIdentifier": 2,
  "longitude": -46574716,
  "latitude": -23584852,
  "setCens": 355030893000035,
  "area": 3550308005042,
  "districtCode": 95,
  "district": "VILA PRUDENTE",
  "townCode": 29,
  "town": "VILA PRUDENTE",
  "firstZone": "Leste",
  "secondZone": "Leste 1",
  "name": "PRACA SANTA HELENA",
  "registryCode": "4045-2",
  "publicArea": "RUA JOSE DOS REIS",
  "number": "909.000000",
  "neighborhood": "VL ZELINA",
  "reference": "RUA OLIVEIRA GOUVEIA"
}'
```

### Response

#### 201 (CREATED)

```json
{
  "id": "61f4669070f21965b0ee9435",
  "legacyIdentifier": 2,
  "longitude": -46574716,
  "latitude": -23584852,
  "setCens": 355030893000035,
  "area": 3550308005042,
  "districtCode": 95,
  "district": "VILA PRUDENTE",
  "townCode": 29,
  "town": "VILA PRUDENTE",
  "firstZone": "Leste",
  "secondZone": "Leste 1",
  "name": "PRACA SANTA HELENA",
  "registryCode": "4045-2",
  "publicArea": "RUA JOSE DOS REIS",
  "number": "909.000000",
  "neighborhood": "VL ZELINA",
  "reference": "RUA OLIVEIRA GOUVEIA"
}
```

2. Create market request with validation errors

### Request

```bash
curl -X 'POST' \
  'http://localhost:8080/api/v1/markets' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "61f4669070f21965b0ee9437",
  "legacyIdentifier": 4,
  "longitude": -46513450,
  "latitude": -23520880,
  "setCens": 355030859000173,
  "area": 3550308005145,
  "districtCode": 60,
  "district": "PENHA",
  "townCode": 21,
  "town": "PENHA",
  "firstZone": "Leste",
  "secondZone": "Leste 1",
  "name": "VILA NOVA GRANADA",
  "publicArea": "RUA FRANCISCO DE OLIVEIRA BRAGA",
  "number": "13.000000",
  "neighborhood": "VL NOVA GRANADA",
  "reference": "RUA OLIVIA DE OLIVEIRA"
}'
```

### Response

#### 400 (BAD REQUEST)

```json
{
  "errors": [
    {
      "property": "registryCode",
      "value": "null",
      "errorMessage": "Must not be null"
    }
  ]
}
```

3. Delete market by its registry code successfully

### Request

```bash
curl -X 'DELETE' \
  'http://localhost:8080/api/v1/markets/7210-9' \
  -H 'accept: */*'
 ```

### Response

#### 204 (NO CONTENT)

4. Delete not found market by its registry code

### Request

```bash
curl -X 'DELETE' \
  'http://localhost:8080/api/v1/markets/1234-5' \
  -H 'accept: */*'
 ```
 
 #### Response
 
 #### 404 (NOT FOUND)
 
 ```json
 {
  "message": "Market with code 1234-5 not found"
 }
 ```
