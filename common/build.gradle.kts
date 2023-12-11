plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("8.1.1")
}

group = "me.flame.havic"
version = "1.0.0.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    compileOnly("org.jetbrains:annotations:24.0.1")
    implementation(project(":common"))
}