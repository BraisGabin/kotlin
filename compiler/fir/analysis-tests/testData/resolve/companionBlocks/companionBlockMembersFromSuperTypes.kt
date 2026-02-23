// RUN_PIPELINE_TILL: FRONTEND
// FILE: A.java
public class A extends K {
    public static void foo() {}
}

// FILE: B.java
public class B {
    public static void baz() {}
}

// FILE: test.kt
open class K : B() {
    companion {
        fun bar() {}
    }
}

class KK : K() {
    companion {
        fun qux() {}
    }
}

fun test() {
    // Java receiver => find statics from Java (transitive) supertypes only
    A.foo()
    A.<!UNRESOLVED_REFERENCE!>bar<!>()
    A.baz()

    // Kotlin receiver => only companion members declared directly in the class
    K.bar()
    K.<!UNRESOLVED_REFERENCE!>baz<!>()

    KK.qux()
    KK.<!UNRESOLVED_REFERENCE!>bar<!>()
    KK.<!UNRESOLVED_REFERENCE!>baz<!>()
}

/* GENERATED_FIR_TAGS: classDeclaration, functionDeclaration, javaFunction, javaType */
