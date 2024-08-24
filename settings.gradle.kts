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