import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.springframework.boot") version "2.5.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
}

group = "dk.rbyte"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}



dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-security:2.5.6")
    implementation ("org.springframework.security:spring-security-test:5.5.1")
    implementation ("jakarta.xml.bind:jakarta.xml.bind-api:2.3.2")
    implementation ("org.glassfish.jaxb:jaxb-runtime:2.3.2")
    implementation ("io.jsonwebtoken:jjwt:0.9.1")
    implementation ("com.google.code.gson:gson:2.8.8")
    implementation("org.springframework.boot:spring-boot-starter:2.5.5")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-web:2.5.4")
    implementation("junit:junit:4.13.2")
    implementation("org.junit.jupiter:junit-jupiter:5.8.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools:2.5.6")
    runtimeOnly("org.postgresql:postgresql:42.2.23.jre7")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.5.6")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.21")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.test {
    useJUnitPlatform()
}