Result

TODO:
- combine
- flatCombine
- List of results to result of list
- java docs

OptionalResult
```java
<R> OptionalResult<R, E> flatMapValue(Function<T, OptionalResult<R, E>> mapper);
<R> Result<R, E> flatMapValueToResult(Function<T, Result<R, E>> mapper);
OptionalResult<T, E> consumeValue(Consumer<T> consumer);
OptionalResult<T, E> verifyValue(Function<T, VoidResult<E>> verifier);
```
