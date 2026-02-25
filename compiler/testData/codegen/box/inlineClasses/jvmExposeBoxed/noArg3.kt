// WITH_STDLIB
// TARGET_BACKEND: JVM_IR

// FILE: J.java
public class J {
    public static RegularClass createRegularClass() {
        return new RegularClass();
    }
}

// FILE: Box.kt
@file:OptIn(ExperimentalStdlibApi::class)

@JvmInline
value class IntWrapper(val i: Int = 0)

@JvmExposeBoxed
class RegularClass(val property: IntWrapper = IntWrapper(2))

fun box(): String {
    val kotlin = RegularClass().property
    val java = J.createRegularClass().property
    if (kotlin != java) return "$kotlin != $java"
    return "OK"
}