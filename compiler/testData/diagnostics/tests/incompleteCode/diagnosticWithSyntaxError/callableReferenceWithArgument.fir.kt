// RUN_PIPELINE_TILL: FRONTEND
val lam = { "lam" }

fun foo() {
    ::lam<!SYNTAX!>(unresolved)<!>
    ::lam<!SYNTAX!>(::lam)<!>
    ::lam<!SYNTAX!>(fun() { return 42 })<!>
}

/* GENERATED_FIR_TAGS: callableReference, functionDeclaration, lambdaLiteral, propertyDeclaration, stringLiteral */
