plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.38"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

taboolib {
    description {
        dependencies {
            name("PlayerPoints")
        }
    }
    install("common")
    install("common-5")
    install("module-configuration")
    install("module-database")
    install("platform-bukkit")
    install("expansion-command-helper")
    classifier = null
    version = "6.0.7-52"
}

repositories {
    mavenCentral()
    maven("https://repo.rosewooddev.io/repository/public/")
}

dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11800:11800-minimize:api")
    compileOnly("ink.ptms.core:v11800:11800-minimize:mapped")

    compileOnly("org.black_ixx:playerpoints:3.2.3") { isTransitive = false }

    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = project.group.toString()
        }
    }
}