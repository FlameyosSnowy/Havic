plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version("7.1.1")
}

group = "me.flame.havic"
version = "1.0.0"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(11))

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/central/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    compileOnly("org.jetbrains:annotations:24.0.1")
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("com.google.errorprone:error_prone_annotations:2.23.0")
    implementation(project(":common"))
}