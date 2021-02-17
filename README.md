#JLox
##TheOneAndOnly
> A minimal language based on the project at 
> [craftinginterpreters.com](https://craftinginterpreters.com).
___

This is very similar to the original, although I made a few changes
after writing the initial code. Specifically, I:
* Added Anonymous Functions
> In the form `(fun (){})` they represent `LoxFunction`s without 
binding it to a variable. Example:
> ```
> fun named_func(arg1, callable) {
>   print arg1;
>   print callable();
> }
> 
> named_func("Hello", fun() {
>   return " World!";
> });
> ```
> This will print "Hello World!".
> 
> As a semantic choice, I allowed the expression statement `fun (){};`
as well as `fun (){}();`.
* Added Class Methods
> By prefixing a method name with `class` it makes the method
a class method, meaning it is owned by the class, but not any
instances.
> ```
> class MyClass {
>   class method() {
>       print "Class Method!";
>   }
> }
> MyClass.method();
> ```
> Note that it is a resolver error to use `this` in a class method.
* Refactored functions.
> After doing these two things, I was pulling my hair out with the complex
nature of handling both an expression form of functions, and a statement
form, so I formally conjoined the two and go rid of the statement form.
Semantically, it makes absolutely no difference at all that 
`fun identifier() {}` is syntax sugar for `var identifier = fun (){}`
but in the code base it makes for simpler code, so I know to only expect
an expression when dealing with functions. 

Thank you for reading this far down! Have a cookie :cookie: