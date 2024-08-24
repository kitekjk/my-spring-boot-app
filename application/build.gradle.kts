plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":domain"))
    implementation("io.projectreactor:reactor-core")
}