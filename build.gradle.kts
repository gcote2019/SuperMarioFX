val pkg = "ca.qc.bdeb.sim203.projetjavafx"
val main = "Main"
val application = "application"
val utf8 = "UTF-8"

plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.14"
}

group = pkg
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks {
    javadoc {
        options.encoding = utf8
    }
    compileJava {
        options.encoding = utf8
    }
    compileTestJava {
        options.encoding = utf8
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    version = "20"
    modules = listOf("javafx.controls")
}


application {
    mainClass.set("${pkg}.${main}")
}

/*task("runCmd", JavaExec::class) {
    group = application
    mainClass.set("${pkg}.${mainCmd}")
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
}*/
