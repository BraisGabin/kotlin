// SNIPPET

var x: Int? = null
x = 1
val y: Int = x

fun foo() {
    x = null
}

val z: Int <!INITIALIZER_TYPE_MISMATCH!>=<!> x
