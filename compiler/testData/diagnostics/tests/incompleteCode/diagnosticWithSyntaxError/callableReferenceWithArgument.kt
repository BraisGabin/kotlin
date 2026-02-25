// RUN_PIPELINE_TILL: FRONTEND
val lam = { "lam" }

fun foo() {
    ::lam<!SYNTAX!>(<!DEBUG_INFO_MISSING_UNRESOLVED!>unresolved<!>)<!>
    ::lam<!SYNTAX!>(::<!DEBUG_INFO_MISSING_UNRESOLVED!>lam<!>)<!>
    ::lam<!SYNTAX!>(fun() {})<!>
}

/* GENERATED_FIR_TAGS: callableReference, functionDeclaration, lambdaLiteral, propertyDeclaration, stringLiteral */
