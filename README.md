Result

TODO:
- combine
- flatCombine
- List of results to result of list
- java docs

OptionalResult
```java
OptionalResult<T, E> consumeValue(Consumer<T> consumer);
OptionalResult<T, E> verifyValue(Function<T, VoidResult<E>> verifier);
```
