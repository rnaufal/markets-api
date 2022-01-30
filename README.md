# markets-api

![](https://github.com/rnaufal/markets-api/actions/workflows/ci.yml/badge.svg)
![Coverage](.github/badges/jacoco.svg)

Markets-API is a reactive Kotlin microservice for managing markets built with the SpringBoot framework and the MongoDB database.

## Content

1. [Requirements](#requirements)
2. [Technologies](#technologies)
3. [Makefile](#makefile)
4. [Building and running the application](#building-and-running-the-application)
5. [Test summary and JaCoCo coverage](#test-summary-and-jacoco-coverage)
6. [Logs](#logs)
7. [CI/CD](#cicd)
8. [Health check](#health-check)
9. [Kubernetes (K8s) probes](#kubernetes-k8s-probes)
10. [Swagger (API documentation)](#swagger-api-documentation)
11. [Architecture](#architecture)
    - [Design](#design)
    - [JSON API Response](#json-api-response)
15. [Request and response examples](#request-and-response-examples)
    - [Create a new market with success](#create-a-new-market-with-success)
    - [Create market request with validation errors](#create-market-request-with-validation-errors)
    - [Delete market by its registry code successfully](#delete-market-by-its-registry-code-successfully)
    - [Delete market with invalid registry code 1234-5](#delete-market-with-invalid-registry-code-1234-5)
    - [Get market by its id](#get-market-by-its-id)
    - [Get market with invalid id 12345](#get-market-with-invalid-id-12345)
    - [Update market by its registry code 4038-0](#update-market-by-its-registry-code-4038-0)
    - [Update market with invalid registry code 45678](#update-market-with-invalid-registry-code-45678)
    - [Search markets by firstZone and district criteria with page, size and sorting parameters](#search-markets-by-firstzone-and-district-criteria-with-page-size-and-sorting-parameters)
    - [Search markets by invalid values for name and neighborhood parameters producing no market results](#search-markets-by-invalid-values-for-name-and-neighborhood-parameters-producing-no-market-results)

## Requirements

Here are the requirements for markets-api service to run:

* [Java 17](https://openjdk.java.net/projects/jdk/17/) for building the application with [Gradle](https://gradle.org).
* [Docker](https://www.docker.com) should be installed and running as daemon.
* [DockerCompose](https://github.com/docker/compose) should be installed to build the application and its dependencies as containers.
* [Make](https://www.gnu.org/software/make/) as a tool to build the application artifacts.

## Technologies

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

The application uses a *Makefile* which contains some commands to build the application, create the Docker image and execute it.

## Building and running the application

Here are some commands that can be executed to prepare and run the application locally in the directory it was checked out:

| Command | About |
| ------ | ------ |
| `make dist` | Build, run unit and integration tests, generate JaCoCo coverage report and execute Kotlin linter |
| `make image` | Execute the same statements as `make dist` and generate the markets-api Docker image to be run  |
| `make run` | Execute the `make image` command, start the markets-api application and MongoDB containers locally |
| `make stop` | Stop the markets-api and MongoDB containers locally |

Below are the steps to build and run the markets-api service locally:

```bash
# Clone the repository
$ git clone git@github.com:rnaufal/markets-api.git

# Enter the directory the project was checked out
$ cd markets-api

# Build the application, run unit and integration tests, build the Docker image, start the application and MongoDB containers locally
$ make run
```

The application should be up and running at `http://localhost:8080` address.

## Test summary and JaCoCo coverage

The application uses the JaCoCo report coverage tool to measure code coverage. The test summary and JaCoCo report coverage can be found under the project directory at the following paths after the build:

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

[Here](https://github.com/rnaufal/markets-api/blob/master/.github/workflows/ci.yml) is the workflow file used to compile, build, test and deploy the application. [Here](https://github.com/rnaufal/markets-api/runs/4999253353?check_suite_focus=true) is an example of some workflow run to build the application. And [here](https://github.com/rnaufal/markets-api/actions) is all the workflow runs which continuosly build the application. 

Each workflow run produces the following outputs:

1. Test results - [Example](https://github.com/rnaufal/markets-api/runs/4999265345?check_suite_focus=true)
2. JaCoCO report - [Example](https://github.com/rnaufal/markets-api/suites/5114802564/artifacts/153502498)

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
- The dataset file *DEINFO_AB_FEIRASLIVRES_2014.csv* is loaded once on the application startup.
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

## Request and response examples

### [Create](http://localhost:8080/webjars/swagger-ui/index.html#/Markets/create) a new market with success

#### Request

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

#### Response

##### 201 (CREATED)

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

### [Create](http://localhost:8080/webjars/swagger-ui/index.html#/Markets/create) market request with validation errors

#### Request

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

#### Response

##### 400 (BAD REQUEST)

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

### [Delete](http://localhost:8080/webjars/swagger-ui/index.html#/Markets/delete) market by its registry code successfully

#### Request

```bash
curl -X 'DELETE' \
  'http://localhost:8080/api/v1/markets/7210-9' \
  -H 'accept: */*'
 ```

#### Response

##### 204 (NO CONTENT)

### [Delete](http://localhost:8080/webjars/swagger-ui/index.html#/Markets/delete) market with invalid registry code *1234-5*

#### Request

```bash
curl -X 'DELETE' \
  'http://localhost:8080/api/v1/markets/1234-5' \
  -H 'accept: */*'
 ```
 
#### Response
 
##### 404 (NOT FOUND)
 
 ```json
 {
  "message": "Market with code 1234-5 not found"
 }
 ```
 
### [Get](http://localhost:8080/webjars/swagger-ui/index.html#/Markets/getById) market by its id

#### Request

```bash
curl -X 'GET' \
  'http://localhost:8080/api/v1/markets/61f54902519fe5673eae7c9d' \
  -H 'accept: */*'
  ```
  
#### Response
   
##### 200 (OK)

```json
{
  "id": "61f54902519fe5673eae7c9d",
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
  "registryCode": "3048-1",
  "publicArea": "RUA FRANCISCO DE OLIVEIRA BRAGA",
  "number": "13.000000",
  "neighborhood": "VL NOVA GRANADA",
  "reference": "RUA OLIVIA DE OLIVEIRA"
}
```

### [Get](http://localhost:8080/webjars/swagger-ui/index.html#/Markets/getById) market with invalid id *12345*

#### Request

```bash
curl -X 'GET' \
  'http://localhost:8080/api/v1/markets/12345' \
  -H 'accept: */*'
  ```

#### Response

##### 404 (NOT FOUND)

```json
{
  "message": "Market with id 12345 not found"
}
```

### [Update](http://localhost:8080/webjars/swagger-ui/index.html#/Markets/update) market by its registry code *4038-0*

#### Request

```bash
curl -X 'PATCH' \
  'http://localhost:8080/api/v1/markets/4038-0' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "legacyIdentifier": 145,
  "longitude": -46527190,
  "latitude": -23530784,
  "setCens": 355030859000046,
  "area": 3550308005074,
  "districtCode": 60,
  "district": "PENHA",
  "townCode": 21,
  "town": "PENHA-2",
  "firstZone": "Leste - 2",
  "secondZone": "Leste - 2",
  "name": "MARIA CARLOTA DAS NEVES",
  "registryCode": "4038-0",
  "publicArea": "RUA MARIA CARLOTA DAS NEVES",
  "number": "12345",
  "neighborhood": "PENHA NOVA",
  "reference": "LOC AV AMADOR BUENO DA VEIGA"
}'
```

#### Response

##### 200 (OK)

```json
{
  "id": "61f54902519fe5673eae7ca3",
  "legacyIdentifier": 145,
  "longitude": -46527190,
  "latitude": -23530784,
  "setCens": 355030859000046,
  "area": 3550308005074,
  "districtCode": 60,
  "district": "PENHA",
  "townCode": 21,
  "town": "PENHA-2",
  "firstZone": "Leste - 2",
  "secondZone": "Leste - 2",
  "name": "MARIA CARLOTA DAS NEVES",
  "registryCode": "4038-0",
  "publicArea": "RUA MARIA CARLOTA DAS NEVES",
  "number": "12345",
  "neighborhood": "PENHA NOVA",
  "reference": "LOC AV AMADOR BUENO DA VEIGA"
}
```

### [Update](http://localhost:8080/webjars/swagger-ui/index.html#/Markets/update) market with invalid registry code *45678*

#### Request

```bash
curl -X 'PATCH' \
  'http://localhost:8080/api/v1/markets/45678' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "legacyIdentifier": 145,
  "longitude": -46527190,
  "latitude": -23530784,
  "setCens": 355030859000046,
  "area": 3550308005074,
  "districtCode": 60,
  "district": "PENHA",
  "townCode": 21,
  "town": "PENHA-2",
  "firstZone": "Leste - 2",
  "secondZone": "Leste - 2",
  "name": "MARIA CARLOTA DAS NEVES",
  "registryCode": "4038-0",
  "publicArea": "RUA MARIA CARLOTA DAS NEVES",
  "number": "12345",
  "neighborhood": "PENHA NOVA",
  "reference": "LOC AV AMADOR BUENO DA VEIGA"
}'
```

#### Response

##### 404 (NOT FOUND)

```json
{
  "message": "Market with code 45678 not found"
}
```

### [Search](http://localhost:8080/webjars/swagger-ui/index.html#/Markets/search) markets by *firstZone* and *district* criteria with page, size and sorting parameters

#### Request

```bash
curl -X 'GET' \
  'http://localhost:8080/api/v1/markets?district=JOSE%20BONIFACIO&firstZone=Leste&page=4&size=2&sort=name%2Casc%26sort%3Dneighborhood%2Casc' \
  -H 'accept: */*'
```

#### Response

##### 200 (OK)

```json
{
  "content": [
    {
      "id": "61f55a942fb61b56bc19af82",
      "legacyIdentifier": 827,
      "longitude": -46436077,
      "latitude": -23541074,
      "setCens": 355030847000081,
      "area": 3550308005261,
      "districtCode": 46,
      "district": "JOSE BONIFACIO",
      "townCode": 27,
      "town": "ITAQUERA",
      "firstZone": "Leste",
      "secondZone": "Leste 2",
      "name": "ITAQUERA II",
      "registryCode": "1370-6",
      "publicArea": "RUA LAURA BOSS",
      "number": "",
      "neighborhood": "GUAIANASES",
      "reference": "COHAB JOSE BONIFACIO"
    },
    {
      "id": "61f55a942fb61b56bc19ac68",
      "legacyIdentifier": 33,
      "longitude": -46427138,
      "latitude": -23537932,
      "setCens": 355030847000108,
      "area": 3550308005261,
      "districtCode": 46,
      "district": "JOSE BONIFACIO",
      "townCode": 27,
      "town": "ITAQUERA",
      "firstZone": "Leste",
      "secondZone": "Leste 2",
      "name": "JARDIM SANTA HELENA",
      "registryCode": "6056-9",
      "publicArea": "RUA SANTA EDITH",
      "number": "357.000000",
      "neighborhood": "JD STA HELENA",
      "reference": "GUAIANAZES"
    }
  ],
  "pageable": {
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    },
    "offset": 6,
    "pageNumber": 3,
    "pageSize": 2,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalPages": 5,
  "totalElements": 10,
  "size": 2,
  "number": 3,
  "sort": {
    "empty": false,
    "sorted": true,
    "unsorted": false
  },
  "first": false,
  "numberOfElements": 2,
  "empty": false
}
```

### [Search](http://localhost:8080/webjars/swagger-ui/index.html#/Markets/search) markets by invalid values for *name* and *neighborhood* parameters producing no market results

#### Request

```bash
curl -X 'GET' \
  'http://localhost:8080/api/v1/markets?name=Teste2&neighborhood=Teste' \
  -H 'accept: */*'
```

#### Response

##### 200 (OK)

```json
{
  "content": [],
  "pageable": {
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 0,
  "totalElements": 0,
  "size": 10,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 0,
  "empty": true
}
```
