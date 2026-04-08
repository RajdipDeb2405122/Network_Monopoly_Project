plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    // Explicitly set (optional but clearer when using toolchain)
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("demof")
    mainClass.set("application.Main")

    applicationDefaultJvmArgs = listOf(
        "--add-opens=java.base/java.lang=com.jfoenix",
        "--add-opens=java.base/java.lang.reflect=com.jfoenix",
        "--add-opens=javafx.graphics/com.sun.javafx.scene=com.jfoenix",
        "--add-opens=javafx.graphics/com.sun.javafx.scene.traversal=com.jfoenix",
        "--add-opens=javafx.controls/com.sun.javafx.scene.control.behavior=com.jfoenix"
        // You can add more if needed, e.g.:
        // "--add-opens=javafx.base/com.sun.javafx.binding=com.jfoenix",
    )
}

javafx {
    version = "21.0.6"
    modules = listOf(
        "javafx.controls",
        "javafx.fxml",
        "javafx.web",
        "javafx.swing",
        "javafx.media"
    )
}

dependencies {
    // JFoenix 9.0.10 is the last official release (2020); still widely used with --add-opens workaround
    implementation("com.jfoenix:jfoenix:9.0.10")

    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0") {
        exclude(group = "org.openjfx")
    }
    implementation("net.synedra:validatorfx:0.6.1") {
        exclude(group = "org.openjfx")
    }
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.kordamp.bootstrapfx:bootstrapfx-core:0.4.0")
    implementation("eu.hansolo:tilesfx:21.0.9") {
        exclude(group = "org.openjfx")
    }
    implementation("com.github.almasb:fxgl:17.3") {
        exclude(group = "org.openjfx")
        exclude(group = "org.jetbrains.kotlin")
    }

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.1")
}

jlink {
    imageZip.set(layout.buildDirectory.file("distributions/app-${javafx.platform.classifier}.zip"))

    options = listOf(
        "--strip-debug",
        "--compress", "2",
        "--no-header-files",
        "--no-man-pages"
        // NO --add-opens here — they are invalid for jlink!
    )

    launcher {
        name = "app"

        // These will be baked into the generated launch scripts (bin/app, bin/app.bat)
        // They apply when you run the bundled app from build/image/bin/app
        jvmArgs = listOf(
            "--add-opens=java.base/java.lang=com.jfoenix",
            "--add-opens=java.base/java.lang.reflect=com.jfoenix",
            "--add-opens=javafx.graphics/com.sun.javafx.scene=com.jfoenix",
            "--add-opens=javafx.graphics/com.sun.javafx.scene.traversal=com.jfoenix",
            "--add-opens=javafx.controls/com.sun.javafx.scene.control.behavior=com.jfoenix"
            // Add more if new InaccessibleObjectExceptions appear at runtime
        )
    }
}