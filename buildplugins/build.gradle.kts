import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}
tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("buildplugins.commonandroiddependency") {
            id = libs.plugins.buildplugins.commonandroiddependency.get().pluginId
            implementationClass = "com.composeplayground.buildplugins.CommonAndroidDependencyPlugin"
        }
        register("buildplugins.hilt") {
            id = libs.plugins.buildplugins.hilt.get().pluginId
            implementationClass = "com.composeplayground.buildplugins.HiltDependencyPlugin"
        }
        register("buildplugins.testdependency") {
            id = libs.plugins.buildplugins.testdependency.get().pluginId
            implementationClass = "com.composeplayground.buildplugins.CommonAndroidTestDependencyPlugin"
        }
    }
}