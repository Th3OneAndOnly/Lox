package com.theoneandonly.lox;

import java.util.List;
import java.util.Map;

class LoxClass extends LoxInstance implements LoxCallable {

    final String name;
    final LoxClass superclass;
    private final Map<String, LoxFunction> classMethods;
    private final Map<String, LoxFunction> methods;

    LoxClass(String name,
             LoxClass superclass,
             Map<String, LoxFunction> methods,
             Map<String, LoxFunction> classMethods) {
        super(null);
        this.name = name;
        this.methods = methods;
        this.superclass = superclass;
        this.classMethods = classMethods;
    }

    LoxFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }

        if (superclass != null) {
            return superclass.findMethod(name);
        }

        return null;
    }

    LoxFunction findClassMethod(String name) {
        if (classMethods.containsKey(name)) {
            return classMethods.get(name);
        }

        if (superclass != null) {
            return superclass.findClassMethod(name);
        }

        return null;
    }

    @Override
    public String toString() {
        return "<class " + name + ">";
    }

    @Override
    public int arity() {
        LoxFunction initializer = findMethod(name);
        if (initializer == null && superclass != null) {
            initializer = superclass.findMethod(superclass.name);
        }
        if (initializer == null) return 0;
        return initializer.arity();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        LoxInstance instance = new LoxInstance(this);
        LoxFunction initializer = findMethod(name);
        if (initializer == null && superclass != null) {  // If we don't have an initializer, grab our superclass.
            initializer = superclass.findMethod(superclass.name);
            // Not just findMethod cause that might match a regular method, not an initializer.
            // class Test {Test() {}} class Test2 < Test {Test() {}} should point to Test's initializer.
        }
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }

        return instance;
    }

    @Override
    Object get(Token name) {
        LoxFunction method = findClassMethod(name.lexeme);  // If we are obtaining a value from a class, it will always be a method.
        if (method != null) return method.bind(this);

        throw new RuntimeError(name,
                "Undefined class method '" + name.lexeme + "'.");
    }
}
