package dev.mhh.results;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface Result<T, E>
        extends Shared<E, Result<T, E>>
        permits Err, Ok {

    static <T, E> Result<T, E> ok(T value) {
        return new Ok<>(value);
    }

    static <T, E> Result<T, E> err(E value) {
        return new Err<>(value);
    }

    Optional<T> value();

    <R> Result<R, E> map(Function<T, R> mapper);
    <R> Result<R, E> flatMap(Function<T, Result<R, E>> mapper);
    Result<T, E> consume(Consumer<T> consumer);
    Result<T, E> flatConsume(Function<T, VoidResult<E>> consumer);

    VoidResult<E> toVoidResult();
}
