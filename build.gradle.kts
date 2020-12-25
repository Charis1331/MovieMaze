buildscript {
    repositories.deps()
    dependencies {
        classpath(Config.Plugins.android)
        classpath(Config.Plugins.kotlin)
    }
}

allprojects {
    repositories.deps()
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}
