plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.freefair.lombok") version "8.13"
}

group = "io.jotnkode"
version = "0.0.1-alpha"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(23)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.security:spring-security-crypto")
	implementation("org.xerial:sqlite-jdbc")
	implementation("org.springframework.data:spring-data-commons:3.4.3")
	implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
	implementation("org.hibernate.orm:hibernate-community-dialects")
	implementation("org.bouncycastle:bcpkix-jdk18on:1.80")
	implementation("io.github.nbaars:paseto4j-version4:2024.3")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks {
    bootRun {
        jvmArgs(
            "-Xms1024m",
            "-Xmx1024m"
        )
    }
}
