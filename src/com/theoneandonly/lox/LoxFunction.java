package com.theoneandonly.lox;

import java.util.List;

public class LoxFunction implements LoxCallable {

    private final Expr.Function function;
    private final Environment closure;
    private final boolean isInitializer;

    LoxFunction(Expr.Function function, Environment closure, boolean isInitializer) {
        this.function = function;
        this.closure = closure;
        this.isInitializer = isInitializer;
    }

    LoxFunction bind(LoxInstance instance) {
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        return new LoxFunction(function, environment, isInitializer);
    }

    @Override
    public int arity() {
        return function.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        for (int i = 0; i < function.params.size(); i++) {
            environment.define(function.params.get(i).lexeme, arguments.get(i));
        }

        try {
            interpreter.executeBlock(function.body, environment);
        } catch (Return returnValue) {
            if (isInitializer) return closure.getAt(0, "this");
            return returnValue.value;
        }

        if (isInitializer) return closure.getAt(0, "this");
        return null;
    }

    @Override
    public String toString() {
        return "<lox function>";
    }
}
