// COMPILATION_ERRORS

fun err0() {
    a::b()
}

fun err1() {
    a.b::c()
}

fun err2() {
    A::
}

fun err3() {
    ::
}

fun err4() {
    ::x()
}

fun err5() {
    ::x()()
}

fun err6() {
    ::x(foo)
}

fun err7() {
    ::x(fun foo() {})
}

fun typeArgumentsError() {
    ::a<b>
    ::a<b,c<*>>
    a::b<c>

    ::a<b>()
}
