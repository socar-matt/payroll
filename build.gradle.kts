import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.71"
    kotlin("plugin.spring") version "1.3.71"
    kotlin("plugin.jpa") version "1.3.71"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    jcenter()
}

apply(plugin = "java")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

val ktlint by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:0.35.0")
}

val verifyKtlint = task("ktlint", JavaExec::class) {
    description = "Check *.gradle.kts code style."
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args("**/*.gradle.kts", "src/**/*.kt")
}

tasks.check.get().dependsOn(verifyKtlint)

task("ktlintFormat", JavaExec::class) {
    description = "Fix *.gradle.kts code style violations."
    classpath = verifyKtlint.classpath
    main = verifyKtlint.main
    args("-F")
    args(verifyKtlint.args)
}
