# Result&lt;T, E&gt; for Java

A type-safe Result library for Java, inspired by Rust's `Result<T, E>`. Instead of throwing exceptions, methods return explicit success or failure values — making error handling visible, composable, and impossible to accidentally ignore.

## Features

- **Pattern matching** — use `switch` expressions directly over result types
- **Sealed interfaces and records** — no class hierarchies; everything is a closed, known type
- **Fluent chaining** — convert and transform across all three result types
- **Serializable by default** — as long as your generic types are serializable
- **100% test coverage** — quite easy without branching
- **List and combination support** — stream over lists of results or combine them into a single result.
---

## The three result types

| Type | States | Use when |
|---|---|---|
| `Result<T, E>` | `Ok(T)` or `Err(E)` | An operation that either succeeds with a value or fails |
| `OptionalResult<T, E>` | `Present(T)`, `Empty`, or `OptErr(E)` | An operation that may succeed with a value, succeed with nothing, or fail |
| `VoidResult<E>` | `VoidOk` or `VoidErr(E)` | An operation that either succeeds (with no value) or fails |

---

## Quick start

Import into a gradle project using:
```groovy
implementation 'dev.mhh:result:1.0.0'
```

Instead of throwing an exception, return a `Result`:

```java
Result<String, String> parseInput(String input) {
    var trimmed = input.trim();
    if (trimmed.isBlank()) return Result.err("Input is required");
    return Result.ok(trimmed);
}
```

Callers decide how to handle the result — with a `switch` expression, fluent chain, or any combination:

```java
// Pattern match: handle each case explicitly
Json restEndpoint(String input) {
    return switch (parseInput(input)) {
        case Ok(var value) -> buildSuccess(value);
        case Err(var err)  -> buildError(err);
    };
}

// Or throw in contexts where the error is truly unexpected
void internalMethod(String input) {
    switch (parseInput(input)) {
        case Ok(var value) -> process(value);
        case Err(var err)  -> throw new IllegalStateException("Unexpected error: " + err);
    }
}
```

---

## Fluent chaining

Results can be transformed and converted across types in a single chain, without intermediate variables or defensive checks at each step:

```java
VoidResult<String> result = Result.ok(rawInput)
    .map(String::trim)
    .mapToOptional(s -> Optional.of(s).filter(Predicate.not(String::isBlank)))
    .toResult(() -> "Input must not be blank")
    .verify(InputVerifier::verify)
    .toVoidResult();
```

If any step produces an error, the rest of the chain short-circuits — the error is carried through unchanged.

---

## Collecting streams of results

`ResultCollector` is a standard `Collector` that partitions a stream of `Result` values into either a list of all successes or a list of all errors. The stream is always consumed in full before the final result is determined.

```java
Result<List<Integer>, List<String>> result = Stream.of(
        Result.ok(1),
        Result.err("oops"),
        Result.ok(3)
    )
    .collect(ResultCollector.collector());
```

If every element is `Ok`, the returned result is `Ok` containing a list of all unwrapped values. If any element is `Err`, the returned result is `Err` containing a list of all unwrapped errors. Encounter order is preserved in both cases.
 
---

## Combining independent results

`ResultCombiner` combines 2–8 independent `Result` values using a function. All results are evaluated up front; if any are errors, all errors are collected and returned together. The combining function is only invoked when every input is successful.

```java
Result<Address, List<String>> address = ResultCombiner.combine(
    Address::new,
    parseStreet(input),
    parseCity(input),
    parsePostalCode(input)
);
```

This is useful for validating multiple independent fields simultaneously and surfacing all failures at once, rather than short-circuiting on the first error as fluent chaining does.
 
---

## Naming conventions

The API follows consistent naming patterns:

| Prefix / name                                    | Meaning                                                                    |
|--------------------------------------------------|----------------------------------------------------------------------------|
| `map`                                            | Transform the **value** to a new type                                      |
| `mapError`                                       | Transform the **error** to a new type                                      |
| `flatMap`                                        | Transform the value into a new result and flatten (merge) it               |
| `consume`                                        | Run a side effect with the **value** (returns the same result)             |
| `consumeError`                                   | Run a side effect with the **error**                                       |
| `run`                                            | Run a `Runnable` depending on the result state                             |
| `verify`                                         | Validate the value with a function returning `VoidResult`                  |
| `value`                                          | Suffix on `OptionalResult` methods — only acts when a value is **present** |
| `filter`                                         | Checks a `Predicate<T>` similar to `Optional.filter`                        |
| `ok` / `err`                                     | Factory methods for construction                                           |
| `toResult` / `toVoidResult` / `toOptionalResult` | Convert between result types                                               |

### `value` suffix on `OptionalResult`

`OptionalResult` has two sets of transformation methods. Without the `value` suffix, the method receives an `Optional<T>` and operates on all non-error states (present or empty). With the suffix, it only fires when a value is actually present:

```java
// Operates on Optional<T> — runs whether value is present or empty
optionalResult.map(optional -> optional.map(String::toUpperCase));

// Only runs when a value is present — empty passes through unchanged
optionalResult.mapValue(String::toUpperCase);
```

## `Optional`-like behaviour

The `Result` types aims to be like `Optional`, but there are differences to ensure proper error handling:
- The `ifPresent` naming pattern did not fit well, so it has been renamed to `consume`.
  - Furthermore, there is no `ifPresentOrElse` method as those can be presented by an `consume` followed by `consumeError`.
- There are no `get`, `orElse` or `orElseThrow` (without exception) methods as these will throw away the error without proper handling.
  - Instead, use either `orElseThrow` with a given `exceptionSupplier` or switch over the values.
- The `of` is called `ok` to be more expressive of an error also technically being an `of`.
- There is no `stream` method as it would also throw away the error without proper handling.
- `or` is only supported for `OptionalResult` and not `Result` as it would throw away the error without proper handling.
