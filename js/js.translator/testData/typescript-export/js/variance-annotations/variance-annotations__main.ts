import Covariant = JS_TESTS.foo.Covariant;
import Contravariant = JS_TESTS.foo.Contravariant;
import Invariant = JS_TESTS.foo.Invariant;
import UnsafeCovariant = JS_TESTS.foo.UnsafeCovariant;

class Animal {
    sayName() {}
}
class Dog extends Animal {
    bark() {}
}

function box(): string {
    const c1 = new Covariant<string>("123");
    const c2 = new Contravariant<string>();
    const c3 = new Invariant<number>(123);
    const c4: UnsafeCovariant<Animal> = new UnsafeCovariant<Dog>(new Dog());

    return "OK";
}