import org.jetbrains.kotlin.buildtools.api.ExperimentalBuildToolsApi
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    `kotlin-dsl`
    kotlin("jvm")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral { setUrl("https://cache-redirector.jetbrains.com/maven-central") }
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    testImplementation(kotlin("test-junit5"))
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)

    constraints {
        api(libs.apache.commons.lang)
    }
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalBuildToolsApi::class)
    compilerVersion = "2.2.0"
    jvmToolchain(17)
}

configurations {
    "kotlinCompilerPluginClasspathMain" {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin" && requested.name == "kotlin-serialization-compiler-plugin-embeddable") {
                useTarget("org.jetbrains.kotlin:kotlin-serialization-compiler-plugin-embeddable:2.2.0")
                because("Compatible with Kotlin compiler 2.2.0 version")
            }
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}

gradlePlugin {
    plugins {
        create("internal-gradle-setup") {
            id = "internal-gradle-setup"
            implementationClass = "org.jetbrains.kotlin.build.InternalGradleSetupSettingsPlugin"
        }
    }
}

project.configurations.named(org.jetbrains.kotlin.gradle.plugin.PLUGIN_CLASSPATH_CONFIGURATION_NAME + "Main") {
    resolutionStrategy {
        eachDependency {
            if (this.requested.group == "org.jetbrains.kotlin") useVersion("2.2.0")
        }
    }
}
