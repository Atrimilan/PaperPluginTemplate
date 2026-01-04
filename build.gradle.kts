
group = "io.github.atrimilan.paperplugintemplate"

plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

dependencies {
    // PaperMC (using paperweight-userdev)
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
    // JUnit
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

val localServerDir = "local-server"

tasks {
    runServer {
        runDirectory.set(file(localServerDir))

        jvmArgs(
            "-Dcom.mojang.eula.agree=true", "-Dserver.port=25565"
        )

        doFirst {
            val serverProperties = file("${localServerDir}/server.properties")
            val bukkitYml = file("${localServerDir}/bukkit.yml")

            listOf(serverProperties, bukkitYml).forEach { file ->
                file.parentFile.mkdirs()
            }
            serverProperties.writeText(
                """
                allow-nether=false
                enable-command-block=true
                gamemode=creative
                level-type=minecraft\:flat
                motd=A local Paper server
                """.trimIndent()
            )
            bukkitYml.writeText(
                """
                settings:
                  allow-end: false
                """.trimIndent()
            )
        }
    }

    test {
        useJUnitPlatform()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
