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

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.mockito", module = "mockito-core")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
}