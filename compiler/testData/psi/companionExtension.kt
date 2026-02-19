// COMPILATION_ERRORS

companion fun String.foo() {}
private companion fun String.foo() {}
companion inline fun String.foo() {}

companion val String.bar = 1
private companion val String.bar = 1
companion inline val String.bar = 1