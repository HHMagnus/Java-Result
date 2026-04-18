package dev.mhh.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A result type that represents either a successful value or an error.
 * This is the core result type, analogous to {@code Either} in functional programming
 * or {@code Result} in Rust — it holds exactly one of: a non-null value ({@code Ok}),
 * or a non-null error ({@code Err}).
 *
 * <p>Operations on an error result are passed through without invoking any mapper or consumer,
 * allowing chains to be written without defensive checks at each step.
 *
 * @param <T> the type of the success value.
 * @param <E> the type of the error.
 */
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

    /**
     * Maps the success value to a new value using the given function.
     * If this is an error result, the mapper is not called and the error is passed through.
     *
     * @param mapper a function from T to R.
     * @param <R>    the new value type.
     * @return a new ok {@code Result} with the mapped value, or the same error result.
     * @throws NullPointerException if the mapper is null and this is an ok result.
     */
    <R> Result<R, E> map(Function<T, R> mapper);

    /**
     * FlatMaps the success value to a new {@code Result} using the given function.
     * If this is an error result, the mapper is not called and the error is passed through.
     *
     * @param mapper a function from T to {@code Result}&lt;R, E&gt;.
     * @param <R>    the new value type.
     * @return the {@code Result} returned by the mapper, or the same error result.
     * @throws NullPointerException if the mapper is null and this is an ok result.
     */
    <R> Result<R, E> flatMap(Function<T, Result<R, E>> mapper);

    /**
     * Passes the success value to the given consumer for side effects.
     * If this is an error result, the consumer is not called.
     *
     * @param consumer the consumer to receive the value.
     * @return this result, for fluent chaining.
     * @throws NullPointerException if the consumer is null and this is an ok result.
     */
    Result<T, E> consume(Consumer<T> consumer);

    /**
     * Verifies the success value using the given function, which may return an error.
     * If verification fails, the returned error replaces this result.
     * If this is an error result, the verifier is not called.
     *
     * @param verifier a function that receives the value and returns a {@link VoidResult}
     *                 indicating whether verification passed.
     * @return this result if verification passes, or an error result if verification fails.
     * @throws NullPointerException if the verifier is null and this is an ok result.
     */
    Result<T, E> verify(Function<T, VoidResult<E>> verifier);

    /**
     * Converts this {@code Result} to a {@link VoidResult}, discarding the success value.
     * An ok result becomes {@link VoidResult#ok()}, and an error result remains an error.
     *
     * @return a {@link VoidResult} representing the success or failure of this result.
     */
    VoidResult<E> toVoidResult();

    /**
     * Converts this {@code Result} to an {@link OptionalResult}.
     * An ok result becomes a present {@code OptionalResult}, and an error result remains an error.
     *
     * @return a present {@link OptionalResult} if ok, or an error {@link OptionalResult} if error.
     */
    OptionalResult<T, E> toOptionalResult();

    /**
     * Maps the success value to an {@link Optional}, converting this {@code Result} into an
     * {@link OptionalResult}. This allows the result to become empty if the mapper returns
     * {@link Optional#empty()}.
     * If this is an error result, the mapper is not called and the error is passed through.
     *
     * @param mapper a function from T to {@link Optional}&lt;R&gt;.
     * @param <R>    the new value type.
     * @return a present or empty {@link OptionalResult} based on the mapper's output,
     *         or an error {@link OptionalResult} if this is an error result.
     * @throws NullPointerException if the mapper is null and this is an ok result.
     */
    <R> OptionalResult<R, E> mapToOptional(Function<T, Optional<R>> mapper);

    /**
     * FlatMaps the success value to an {@link OptionalResult} using the given function.
     * If this is an error result, the mapper is not called and the error is passed through.
     *
     * @param mapper a function from T to {@link OptionalResult}&lt;R, E&gt;.
     * @param <R>    the new value type.
     * @return the {@link OptionalResult} returned by the mapper, or an error {@link OptionalResult}.
     * @throws NullPointerException if the mapper is null and this is an ok result.
     */
    <R> OptionalResult<R, E> flatMapWithOptionalResult(Function<T, OptionalResult<R, E>> mapper);

    /**
     * Filter the value of this result using the given predicate.
     * Returns an empty result if the predicate returns false. Otherwise, returns this result as an `OptionalResult`.
     * If this is an error result, the filter is not called and the error is passed through.
     *
     * @param filter a predicate that returns true if the value should be kept, false otherwise.
     * @return this result if the filter function returns true, otherwise an empty result.
     * @throws NullPointerException if the filter is null and this is a present result.
     */
    OptionalResult<T, E> filter(Predicate<T> filter);

    /**
     * For an Ok {@code Result} it gives back the value.
     * For an Err {@code Result} it will throw the exception provided by the {@code exceptionSupplier}
     * @param exceptionSupplier Supplier of the exception to throw if {@code Result} is an error
     * @return the value of the {@code Result}
     * @param <X> Type of exception to throw if {@code Result} is an error
     * @throws X If the {@code Result} is an error
     * @throws NullPointerException If the {@code Result} is an error and the {@code exceptionSupplier} or its return is null
     */
    <X extends Throwable> T orElseThrow(Function<E, X> exceptionSupplier) throws X;
}