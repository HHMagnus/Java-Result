# Result<T, E> - Java

This library is an implementation of `Result<T, E>` in Java. It is heavily inspired by the *Rust* implementation, but uses *Java* names where appropriate.

This unique features of this specific implementation is:
- `switch` pattern match over the result types.
- No inheritance, everything is `sealed interface`s and `record`s.
- Cyclomatic complexity of 1. There is no `if` statements in the code.
- Fluent chaining between the result types.
- Serializable by default (depends on generic types serializability)

Due to the explicit error types the implementation does not support `combine` and result lists easily.

# Example

Instead of throwing an `Exception` you can return a `Result<T, E>`:
```java
Result<String, String> verifyInput(String input) {
    final var trimmed = input.trim();
    if (trimmed.isBlank()) {
        return Result.err("Input is required");
    }
    return Result.ok(trimmed);
}
```

This allows any caller of that function to specify how they want to handle the `Result`:
```java
void internalMethod(String input) {
    final var result = verifyInput(input);
    switch (result) {
        case Ok(var value) -> handleInput(value);
        case Err(var err) -> throw new RuntimeException("Input should already have been confirmed, but got: " + err);
    }
}

Json restEndpoint(String input) {
    final var result = verifyInput(input);
    return switch (result) {
        case Ok(var value) -> buildSuccess(value);
        case Err(var err) -> buildError(err);
    };
}
```
Allowing either for it to be handle or for them to throw if appropriate.

# Result types

The library contains three result types:
- `Result<T, E>` - Either `Ok(T value)` or `Err(E err)`.
- `OptionalResult<T, E>` - Either `Present(T value)`, `Empty` or `OptErr(E err)`.
- `VoidResult<E>` - Either `VoidOk` or `VoidErr(E err)`

# Fluent chaining

These can be chained between fluently:
```java
VoidResult result = Result.ok(initial)
        .map(input -> input.trim())
        .mapToOptional(input -> Optional.of(input).filter(Predicate.not(String::isBlank)))
        .toResult(() -> "default value")
        .verify(InputVerifier::verify)
        .toVoidResult();
```

# Naming patterns

The naming patterns is as follows:
- `flat` specifies merging two results.
- `map` specifies going from one *value type* to another.
    - `mapError` is the same for *error type*.
    - Can be combined with `flatMap` for mapping into a result and merging them.
- `consume` allows code to be run with the inner *value*
  - `consumeError` is the same for the inner *error*.
- `run` runs a runnable depending on the scenario
- `ok` and `err` used for construction depending on type.
- `toResult` or `toVoidResult` or `toOptionalResult` to go between result types
- `verify` to verify the value with a `VoidResult`.
- `value` for running `OptionalResult` when the *value* is present.