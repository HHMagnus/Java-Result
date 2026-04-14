Result

TODO:
- combine
- flatCombine
- List of results to result of list
- java docs

OptionalResult
```java
<R> OptionalResult<R, E> map(Function<Optional<T>, Optional<R>> mapper);
<R> OptionalResult<R, E> flatMap(Function<Optional<T>, OptionalResult<R, E>> mapper);
<R> Result<R, E> flatMapWithResult(Function<Optional<T>, Result<R, E>> mapper);
OptionalResult<T, E> consume(Consumer<Optional<T>> consumer);
OptionalResult<T, E> verify(Function<Optional<T>, VoidResult<E>> verifier);

<R> OptionalResult<R, E> mapValue(Function<T, R> mapper);
<R> OptionalResult<R, E> mapValueToOptional(Function<T, Optional<R>> mapper);
<R> OptionalResult<R, E> flatMapValue(Function<T, OptionalResult<R, E>> mapper);
<R> Result<R, E> flatMapValueToResult(Function<T, Result<R, E>> mapper);
OptionalResult<T, E> consumeValue(Consumer<T> consumer);
OptionalResult<T, E> verifyValue(Function<T, VoidResult<E>> verifier);
```
Result
```java
<R> OptionalResult<R, E> flatMapWithOptionalResult(Function<T, OptionalResult<R, E>> mapper);
```
VoidResult
```java
VoidResult<E> verify(Supplier<VoidResult<E>> consumer);
```