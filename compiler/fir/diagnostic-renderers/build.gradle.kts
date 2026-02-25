plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":compiler:fir:tree"))
    api(project(":compiler:fir:semantics"))
    implementation(project(":compiler:frontend.common-psi"))
}

sourceSets {
    "main" {
        projectDefault()
    }
    "test" { none() }
}
