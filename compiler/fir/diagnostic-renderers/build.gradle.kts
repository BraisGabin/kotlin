plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":compiler:fir:tree"))
    api(project(":compiler:frontend"))
    api(project(":compiler:fir:semantics"))
}

sourceSets {
    "main" {
        projectDefault()
    }
    "test" { none() }
}
