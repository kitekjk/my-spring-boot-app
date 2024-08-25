# My Multi-Module Spring Boot Project

This project is a multi-module Spring Boot application built with Kotlin and Gradle. The project is divided into several modules, each with its own responsibilities, making the application modular, maintainable, and scalable.

## Project Structure

The project is structured as follows:

```
/root-project
 ├── /api
 │   ├── build.gradle.kts
 │   └── /src
 │       └── /main
 │           └── /kotlin
 │               └── /com/example/api
 │                   └── ApiApplication.kt
 ├── /application
 │   └── build.gradle.kts
 ├── /domain
 │   └── build.gradle.kts
 ├── /infra
 │   └── build.gradle.kts
 ├── /build.gradle.kts (Root build script)
 ├── /settings.gradle.kts
 └── /gradle.properties
```

### Modules

#### 1. **domain**

**Purpose**: The `domain` module represents the core business logic and domain entities. It contains all the domain models, business rules, and logic that are independent of any infrastructure or application-specific concerns.

**Dependencies**:
- This module has no dependencies on other modules. It is purely focused on the business logic, ensuring that it remains independent and reusable across different parts of the application.

```kotlin
plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.projectreactor:reactor-core")

    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.projectreactor:reactor-test")
}
```

#### 2. **application**

**Purpose**: The `application` module is responsible for implementing the use cases and orchestrating business logic by interacting with the `domain` module. It contains service classes that use the domain entities and perform operations defined by the business requirements.

**Dependencies**:
- Depends on the `domain` module to access business entities and logic.
- This module acts as a bridge between the `api` and `infra` modules, ensuring that the business logic is executed according to the use cases.

```kotlin
plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":domain"))
    implementation("io.projectreactor:reactor-core")

    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito.kotlin:mockito-kotlin")
    testImplementation("io.projectreactor:reactor-test")
}
```

#### 3. **infra**

**Purpose**: The `infra` (infrastructure) module handles all the infrastructure concerns, such as data persistence, communication with external systems, and integrating with third-party libraries. It is where the actual implementation of repositories, database connections, and other infrastructure services reside.

**Dependencies**:
- Depends on the `domain` module to implement the persistence of domain entities.
- Works alongside the `application` module by providing implementations of repositories and services required by the use cases.

```kotlin
plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":domain"))
    implementation("io.awspring.cloud:spring-cloud-aws-starter-dynamodb")
    implementation("org.springframework.boot:spring-boot-starter-data-cassandra-reactive")

    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito.kotlin:mockito-kotlin")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
```

#### 4. **api**

**Purpose**: The `api` module exposes the REST API for the application. It handles HTTP requests and responses, and interacts with the `application` module to execute business logic and return results to the client.

**Dependencies**:
- Depends on the `application` module to execute business logic and handle use cases.
- Depends on the `infra` module indirectly via the `application` module, where necessary repositories and services are implemented.

```kotlin
plugins {
    kotlin("jvm")
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":infra"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}
```

### Gradle Configuration

### `gradle.properties`

The `gradle.properties` file is used to manage the versions of dependencies across the project.

```properties
kotlinVersion=1.9.10
springBootVersion=3.1.2
dependencyManagementVersion=1.1.2
springCloudAwsVersion=3.0.2
mockitoKotlinVersion=5.4.0
```

### Root `build.gradle.kts`

The root `build.gradle.kts` file defines common configurations for all modules.

```kotlin
plugins {
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management")
    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
}

allprojects {
    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "io.spring.dependency-management")

    dependencyManagement {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:${property("springBootVersion")}")
            mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:${property("springCloudAwsVersion")}")
        }

        dependencies {
            dependency("org.mockito.kotlin:mockito-kotlin:${property("mockitoKotlinVersion")}")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

```

### `settings.gradle.kts`

The `settings.gradle.kts` file includes all the modules in the project and manages plugin versions.

```kotlin
rootProject.name = "my-spring-boot-app"
include("api", "application", "domain", "infra")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        kotlin("jvm") version extra["kotlinVersion"] as String
        kotlin("plugin.spring") version extra["kotlinVersion"] as String
        id("org.springframework.boot") version extra["springBootVersion"] as String
        id("io.spring.dependency-management") version extra["dependencyManagementVersion"] as String
    }
}
```

### Application Entry Point

The application entry point is located in the `src/main/kotlin/com/example/Application.kt` file.

```kotlin
package com.example.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.example"])
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
```

### How to Build and Run

To build the project, use the following command:

```bash
./gradlew build
```

To run the application, use:

```bash
./gradlew bootRun
```

### Conclusion

This multi-module project setup allows for a clean separation of concerns within your Spring Boot application. Each module can be independently managed and tested, leading to a more maintainable and scalable codebase.

By centralizing dependency and plugin management, and using Kotlin and Gradle, this setup provides a robust foundation for developing large-scale applications.

Feel free to extend this structure with additional modules or configurations as needed for your specific project requirements.

