import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.devtools.ksp) apply false
    alias(libs.plugins.arturbosch.detekt)
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    detekt {
        buildUponDefaultConfig = true // preconfigure defaults
        allRules = true // activate all available (even unstable) rules.
        config.setFrom("$rootDir/default-detekt-config.yml")
        autoCorrect = true
    }

    tasks.withType<Detekt>().configureEach {
        reports {
            html.required.set(true) // observe findings in your browser with structure and code snippets
            xml.required.set(false) // checkstyle like format mainly for integrations like Jenkins
            txt.required.set(false) // similar to the console output, contains issue signature to manually edit baseline files
            sarif.required.set(false) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
            md.required.set(false) // simple Markdown format
        }
    }

    tasks.withType<Detekt>().configureEach {
        jvmTarget = "18"
    }
    tasks.withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = "18"
    }

    dependencies {
        detektPlugins(rootProject.libs.arturbosch.detekt.detektFormatting)
    }
}