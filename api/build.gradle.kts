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