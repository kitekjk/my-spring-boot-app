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
}