// Top-level build file donde solo se declaran plugins
plugins {
    // Declaramos los plugins usando los alias de libs.versions.toml
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.androidx.navigation.safeargs.kotlin) apply false

    // El plugin de Google Services también se declara aquí
    id("com.google.gms.google-services") version "4.4.1" apply false // Usamos 4.4.1 que es una versión reciente y estable
}
