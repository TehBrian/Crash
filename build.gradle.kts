import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("java")
    id("java-library")
    id("com.github.johnrengelman.shadow") version("6.1.0")
}

group = "dev.kscott"
version = "1.0.0"

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = sourceCompatibility
}

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnlyApi("org.checkerframework:checker-qual:3.5.0")
    compileOnlyApi("com.google.guava:guava:21.0")

    compileOnly("com.destroystokyo.paper:paper-api:1.16.4-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")

    implementation("com.google.inject:guice:5.0.0-BETA-1")
    implementation("net.kyori:adventure-platform-bukkit:4.4.0")
    implementation("cloud.commandframework:cloud-paper:1.4.0")
    implementation("org.spongepowered:configurate-hocon:4.0.0")
    implementation("net.kyori:adventure-text-minimessage:4.0.0-SNAPSHOT")
    implementation("com.github.stefvanschie.inventoryframework:IF:0.9.1")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        fun relocates(vararg dependencies: String) {
            dependencies.forEach {
                val split = it.split('.')
                val name = split.last()
                relocate(it, "${rootProject.group}.dependencies.$name")
            }
        }

        archiveFileName.set("Crash-${archiveVersion.get()}.jar")

        dependencies {
            exclude(dependency("com.google.guava:"))
            exclude(dependency("com.google.errorprone:"))
        }

        relocates(
                "com.github.benmanes.caffeine",
                "com.google.inject",
                "org.antlr",
                "org.slf4j",
                "org.spongepowered.configurate",
                "cloud.commandframework",
                "net.kyori.adventure",
                "net.kyori.examination",
                "com.github.stevanschie.inventoryframework"
        )
    }

    processResources {
        filter<ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }
}