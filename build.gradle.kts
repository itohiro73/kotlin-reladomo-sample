import org.jetbrains.kotlin.fir.expressions.builder.buildDoWhileLoop
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
}

group = "jp.itohiro"
version = "0.0.1-SNAPSHOT"

val reladomoVersion = "18.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.goldmansachs.reladomo:reladomo:$reladomoVersion")
    implementation("org.postgresql:postgresql")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

val reladomoGenTask by configurations.creating {

}

dependencies {
    reladomoGenTask("com.goldmansachs.reladomo:reladomogen:$reladomoVersion")
    reladomoGenTask("com.goldmansachs.reladomo:reladomo-gen-util:$reladomoVersion")
}

tasks.register("genReladomo") {
    doLast {
        ant.withGroovyBuilder {
            "taskdef"("name" to "genReladomo",
                    "classpath" to reladomoGenTask.asPath,
                    "classname" to "com.gs.fw.common.mithra.generator.MithraGenerator")

            "genReladomo"("xml" to "$projectDir/src/main/resources/reladomo/model/ReladomoClassList.xml",
            "generateEcListMethod" to "true",
            "generatedDir" to "$projectDir/build/generated-sources/reladomo",
            "nonGeneratedDir" to "$projectDir/src/main/java")
        }
    }
}

tasks.register("genDdl") {
    doLast {
        ant.withGroovyBuilder {
            "taskdef"("name" to "genDdl",
                    "classpath" to reladomoGenTask.asPath,
                    "classname" to "com.gs.fw.common.mithra.generator.dbgenerator.MithraDbDefinitionGenerator")

            "genDdl"("xml" to "$projectDir/src/main/resources/reladomo/model/ReladomoClassList.xml",
            "generatedDir" to "$projectDir/build/generated-db/sql",
            "databaseType" to "postgres")
        }
    }
}

sourceSets {
    main {
        java {
            srcDir("build/generated-sources/reladomo")
        }
    }
}

tasks.withType<KotlinCompile> {
    dependsOn("genReladomo")
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

