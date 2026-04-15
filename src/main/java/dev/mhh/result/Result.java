package dev.mhh.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface Result<T, E>
        extends Shared<E, Result<T, E>>
        permits Err, Ok {

    /**
     * Creates an ok Result
     * @param value the value to be wrapped in the Result. Cannot be null.
     * @return a Result containing the value.
     * @param <T> the type of the value.
     * @param <E> the type of the error.
     * @throws NullPointerException if the value is null.
     */
    static <T, E> Result<T, E> ok(T value) {
        return new Ok<>(value);
    }

    /**
     * Creates an error Result
     * @param error the error value. Cannot be null.
     * @return an error Result containing the error value.
     * @param <T> the type of the value.
     * @param <E> the type of the error.
     * @throws NullPointerException if the value is null.
     */
    static <T, E> Result<T, E> err(E error) {
        return new Err<>(error);
    }

    /**
     * @return the value if the Result is an Ok, otherwise returns an empty Optional.
     */
    Optional<T> optionalValue();

    /**
     * Maps the error value of the Result to a different error value.
     * @param mapper the mapper function. This is only called if the Result is an error.
     * @return a new Result with the mapped error value.
     * @param <N> the new error type.
     * @throws NullPointerException if the mapper function is null and the Result is an error.
     */
    <N> Result<T, N> mapError(Function<E, N> mapper);

    <R> Result<R, E> map(Function<T, R> mapper);
    <R> Result<R, E> flatMap(Function<T, Result<R, E>> mapper);
    Result<T, E> consume(Consumer<T> consumer);
    Result<T, E> verify(Function<T, VoidResult<E>> verifier);

    VoidResult<E> toVoidResult();
    OptionalResult<T, E> toOptionalResult();

    <R> OptionalResult<R, E> mapToOptional(Function<T, Optional<R>> mapper);
    <R> OptionalResult<R, E> flatMapWithOptionalResult(Function<T, OptionalResult<R, E>> mapper);
}
