Result

TODO:
- combine
- flatCombine
- List of results to result of list
- java docs

OptionalResult
```java
OptionalResult<T, E> verify(Function<Optional<T>, VoidResult<E>> verifier);

<R> OptionalResult<R, E> mapValue(Function<T, R> mapper);
<R> OptionalResult<R, E> mapValueToOptional(Function<T, Optional<R>> mapper);
<R> OptionalResult<R, E> flatMapValue(Function<T, OptionalResult<R, E>> mapper);
<R> Result<R, E> flatMapValueToResult(Function<T, Result<R, E>> mapper);
OptionalResult<T, E> consumeValue(Consumer<T> consumer);
OptionalResult<T, E> verifyValue(Function<T, VoidResult<E>> verifier);
```

All test that gives an argument should also verify the argument.
All test err test should also verify the supplier/consumer is not called.