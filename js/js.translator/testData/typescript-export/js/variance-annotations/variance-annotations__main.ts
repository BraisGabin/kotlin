import Covariant = JS_TESTS.foo.Covariant;
import Contravariant = JS_TESTS.foo.Contravariant;
import Invariant = JS_TESTS.foo.Invariant;

function box(): string {
    const c1 = new Covariant<string>("123");
    const c2 = new Contravariant<string>();
    const c3 = new Invariant<number>(123);
    return "OK";
}