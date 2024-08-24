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