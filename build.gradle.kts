plugins {
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("jacoco")
    id("org.jmailen.kotlinter") version "3.8.0"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    id("com.google.cloud.tools.jib") version "3.2.0"
}

group = "com.rnaufal.markets"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

extra["testcontainersVersion"] = "1.16.2"
val swaggerVersion = "1.6.4"
val mockkVersion = "1.12.2"
val kotlinLoggingVersion = "2.1.21"
val valiktorVersion = "0.12.0"
val commonsCsvVersion = "1.9.0"
val kotlinTestVersion = "1.4.31"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:$swaggerVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$swaggerVersion")
    implementation("org.valiktor:valiktor-spring-boot-starter:$valiktorVersion")
    implementation("org.apache.commons:commons-csv:$commonsCsvVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-debug")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(module = "mockito-core")
    }
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinTestVersion")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mongodb")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
    }
}

jib {
    container {
        jvmFlags = listOf("-Dfile.encoding=UTF-8")
        ports = listOf("8080")
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    compileTestJava {
        options.encoding = "UTF-8"
    }

    compileKotlin {
        kotlinOptions {
            jvmTarget = "15"
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable", "-Xinline-classes")
        }
    }

    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "15"
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable", "-Xinline-classes")
        }
    }

    jacocoTestReport {
        reports {
            xml.required.set(true)
            html.required.set(true)
            csv.required.set(true)
        }
    }

    jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    counter = "LINE"
                    value = "COVEREDRATIO"
                    minimum = 0.8.toBigDecimal()
                }
            }
        }
        dependsOn(jacocoTestReport)
    }

    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    check {
        dependsOn(jacocoTestCoverageVerification)
    }
}

jacoco {
    toolVersion = "0.8.7"
}

kotlinter {
    disabledRules = arrayOf("import-ordering")
}
