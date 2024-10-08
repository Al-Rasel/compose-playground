package com.composeplayground.buildplugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CommonAndroidTestDependencyPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("testImplementation", libs.findLibrary("junit").get())
                add("testImplementation", libs.findLibrary("androidx.test.rules").get())
                add("testImplementation", libs.findLibrary("kotlinx.coroutines.test").get())
                add("testImplementation", libs.findLibrary("mockk.mockk").get())
                add("testImplementation", libs.findLibrary("androidx.paging.testing").get())
            }
        }
    }
}