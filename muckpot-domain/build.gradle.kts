val kotestVersion = "5.5.4"
val testContainerVersion = "1.17.6"

plugins {
    kotlin("kapt")
    kotlin("plugin.noarg")
    `java-test-fixtures`
}

dependencies {
    implementation(project(":muckpot-infra"))

    val kapt by configurations
    // querydsl
    api("com.querydsl:querydsl-jpa:5.0.0")
    kapt("com.querydsl:querydsl-apt:5.0.0:jpa")
    // https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-spatial
    implementation("org.hibernate:hibernate-spatial:5.6.15.Final")

    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")

    testFixturesImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testFixturesImplementation("org.testcontainers:testcontainers:$testContainerVersion")
    testFixturesImplementation("org.testcontainers:junit-jupiter:$testContainerVersion")
    testFixturesImplementation("org.testcontainers:mariadb:$testContainerVersion")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

noArg {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

tasks {
    withType<Jar> { enabled = true }
    withType<org.springframework.boot.gradle.tasks.bundling.BootJar> { enabled = false }
}
