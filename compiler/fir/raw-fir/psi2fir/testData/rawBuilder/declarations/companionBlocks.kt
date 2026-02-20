class C {
    companion {
        fun foo() {}
    }
    companion {
        val bar = 1
        val baz get() = 2
    }
    companion {
        fun qux() {
            class Local {
                fun insideLocal() {}
                val insideLocalVal = 1
            }
        }
    }
}