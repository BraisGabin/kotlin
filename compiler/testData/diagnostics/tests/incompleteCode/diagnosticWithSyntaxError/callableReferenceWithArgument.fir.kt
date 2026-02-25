// RUN_PIPELINE_TILL: FRONTEND
val lam = { "lam" }

fun foo() {
    ::lam<!SYNTAX!>(<!UNRESOLVED_REFERENCE!>unresolved<!>)<!>
    ::lam<!SYNTAX!>(::lam)<!>
    ::lam<!SYNTAX!>(fun() {})<!>
}

/* GENERATED_FIR_TAGS: callableReference, functionDeclaration, lambdaLiteral, propertyDeclaration, stringLiteral */
