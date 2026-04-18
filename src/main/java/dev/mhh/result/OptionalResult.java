package dev.mhh.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A result type that represents one of three states: a present value, an empty value, or an error.
 * This is conceptually similar to combining {@link Result} with {@link Optional} — it allows
 * operations that may produce a value, produce nothing, or fail with an error.
 *
 * <p>The three states are:
 * <ul>
 *   <li><b>Present</b> — a successful result containing a non-null value.</li>
 *   <li><b>Empty</b> — a successful result containing no value (analogous to {@link Optional#empty()}).</li>
 *   <li><b>Error</b> — a failed result containing an error value.</li>
 * </ul>
 *
 * @param <T> the type of the value.
 * @param <E> the type of the error.
 */
public interface OptionalResult<T, E> extends Shared<E, OptionalResult<T, E>> {

    /**
     * Returns the value wrapped in an {@link Optional}.
     *
     * @return {@link Optional#of(Object)} if this is a present result,
     *         {@link Optional#empty()} if this is an empty or error result.
     */
    Optional<T> optionalValue();

    /**
     * Creates a present {@code OptionalResult} wrapping the given value.
     *
     * @param value the value to wrap. Cannot be null.
     * @param <T>   the type of the value.
     * @param <E>   the type of the error.
     * @return a present {@code OptionalResult} containing the value.
     * @throws NullPointerException if value is null.
     */
    static <T, E> OptionalResult<T, E> ok(T value) {
        return new Present<>(value);
    }

    /**
     * Creates an {@code OptionalResult} from an {@link Optional}, producing a present result
     * if the optional has a value, or an empty result if it does not.
     *
     * <p>This is useful for fluently converting a returned {@link Optional} into an
     * {@code OptionalResult} in a chain without an intermediate variable.
     *
     * @param optionalValue the optional to convert.
     * @param <T>           the type of the value.
     * @param <E>           the type of the error.
     * @return a present {@code OptionalResult} if the optional has a value, otherwise an empty {@code OptionalResult}.
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <T, E> OptionalResult<T, E> okOptional(Optional<T> optionalValue) {
        return optionalValue.map(OptionalResult::<T, E>ok)
                .orElse(OptionalResult.empty());
    }

    /**
     * Creates an empty {@code OptionalResult}, representing a successful result with no value.
     *
     * @param <T> the type of the value.
     * @param <E> the type of the error.
     * @return an empty {@code OptionalResult}.
     */
    static <T, E> OptionalResult<T, E> empty() {
        return new Empty<>();
    }

    /**
     * Creates an error {@code OptionalResult} containing the given error.
     *
     * @param error the error value. Cannot be null.
     * @param <T>   the type of the value.
     * @param <E>   the type of the error.
     * @return an error {@code OptionalResult} containing the error.
     * @throws NullPointerException if error is null.
     */
    static <T, E> OptionalResult<T, E> err(E error) {
        return new OptErr<>(error);
    }

    /**
     * Maps the error value to a different error type, leaving present and empty states unchanged.
     *
     * @param function the mapping function applied to the error. Only called if this is an error result.
     * @param <N>      the new error type.
     * @return a new {@code OptionalResult} with the mapped error, or the same present/empty result unchanged.
     * @throws NullPointerException if the function is null and this is an error result.
     */
    <N> OptionalResult<T, N> mapError(Function<E, N> function);

    /**
     * Converts this {@code OptionalResult} to a {@link VoidResult}, discarding any present value.
     * An empty or present result becomes {@link VoidResult#ok()}, and an error result remains an error.
     *
     * @return a {@link VoidResult} representing the success or failure of this result.
     */
    VoidResult<E> toVoidResult();

    /**
     * Converts this {@code OptionalResult} to a {@link Result}, using the given supplier to produce
     * an error if this result is empty.
     *
     * @param errorIfEmpty a supplier providing the error to use if this result is empty.
     * @return a {@link Result} containing the value if present, or an error if empty or already an error.
     * @throws NullPointerException if the supplier is null and this result is empty.
     */
    Result<T, E> toResult(Supplier<E> errorIfEmpty);

    /**
     * Maps the {@link Optional} value of this result to a new {@link Optional} value.
     * Only called on present and empty results; error results are passed through unchanged.
     *
     * @param mapper a function from {@link Optional}&lt;T&gt; to {@link Optional}&lt;R&gt;.
     * @param <R>    the new value type.
     * @return a new {@code OptionalResult} with the mapped optional value, or the same error result.
     * @throws NullPointerException if the mapper is null and this is not an error result.
     */
    <R> OptionalResult<R, E> map(Function<Optional<T>, Optional<R>> mapper);

    /**
     * FlatMaps the {@link Optional} value of this result to a new {@code OptionalResult}.
     * Only called on present and empty results; error results are passed through unchanged.
     *
     * @param mapper a function from {@link Optional}&lt;T&gt; to {@code OptionalResult}&lt;R, E&gt;.
     * @param <R>    the new value type.
     * @return the {@code OptionalResult} returned by the mapper, or the same error result.
     * @throws NullPointerException if the mapper is null and this is not an error result.
     */
    <R> OptionalResult<R, E> flatMap(Function<Optional<T>, OptionalResult<R, E>> mapper);

    /**
     * FlatMaps the {@link Optional} value of this result to a {@link Result}.
     * Only called on present and empty results; error results are passed through unchanged.
     *
     * @param mapper a function from {@link Optional}&lt;T&gt; to {@link Result}&lt;R, E&gt;.
     * @param <R>    the new value type.
     * @return the {@link Result} returned by the mapper, or a converted error result.
     * @throws NullPointerException if the mapper is null and this is not an error result.
     */
    <R> Result<R, E> flatMapWithResult(Function<Optional<T>, Result<R, E>> mapper);

    /**
     * Passes the {@link Optional} value to the given consumer for side effects.
     * Only called on present and empty results; error results are passed through unchanged.
     *
     * @param consumer the consumer to receive the optional value.
     * @return this result, for fluent chaining.
     * @throws NullPointerException if the consumer is null and this is not an error result.
     */
    OptionalResult<T, E> consume(Consumer<Optional<T>> consumer);

    /**
     * Verifies the {@link Optional} value using the given function, which may return an error.
     * Only called on present and empty results; error results are passed through unchanged.
     *
     * @param verifier a function that receives the optional value and returns a {@link VoidResult}
     *                 indicating whether verification passed.
     * @return this result if verification passes, or an error result if verification fails.
     * @throws NullPointerException if the verifier is null and this is not an error result.
     */
    OptionalResult<T, E> verify(Function<Optional<T>, VoidResult<E>> verifier);

    /**
     * Filters the present value, leaving empty and error states unchanged.
     * @param filter a predicate that returns true if the value should be kept, false otherwise.
     * @return this result if the filter function returns true, otherwise an empty result.
     * @throws NullPointerException if the filter is null and this is a present result.
     */
    OptionalResult<T, E> filter(Predicate<T> filter);

    /**
     * Maps the present value to a new value, leaving empty and error states unchanged.
     * Unlike {@link #map}, this only operates when a value is actually present.
     *
     * @param mapper a function from T to R.
     * @param <R>    the new value type.
     * @return a present {@code OptionalResult} with the mapped value, or the same empty/error result.
     * @throws NullPointerException if the mapper is null and this is a present result.
     */
    <R> OptionalResult<R, E> mapValue(Function<T, R> mapper);

    /**
     * Maps the present value to an {@link Optional}, allowing the result to become empty.
     * Only called when a value is present; empty and error states are passed through unchanged.
     *
     * @param mapper a function from T to {@link Optional}&lt;R&gt;.
     * @param <R>    the new value type.
     * @return a present or empty {@code OptionalResult} based on the mapper's output,
     *         or the same empty/error result.
     * @throws NullPointerException if the mapper is null and this is a present result.
     */
    <R> OptionalResult<R, E> mapValueToOptional(Function<T, Optional<R>> mapper);

    /**
     * FlatMaps the present value to a new {@code OptionalResult}.
     * Only called when a value is present; empty and error states are passed through unchanged.
     *
     * @param mapper a function from T to {@code OptionalResult}&lt;R, E&gt;.
     * @param <R>    the new value type.
     * @return the {@code OptionalResult} returned by the mapper, or the same empty/error result.
     * @throws NullPointerException if the mapper is null and this is a present result.
     */
    <R> OptionalResult<R, E> flatMapValue(Function<T, OptionalResult<R, E>> mapper);

    /**
     * Passes the present value to the given consumer for side effects.
     * Only called when a value is present; empty and error states are passed through unchanged.
     *
     * @param consumer the consumer to receive the value.
     * @return this result, for fluent chaining.
     * @throws NullPointerException if the consumer is null and this is a present result.
     */
    OptionalResult<T, E> consumeValue(Consumer<T> consumer);

    /**
     * Verifies the present value using the given function, which may return an error.
     * Only called when a value is present; empty and error states are passed through unchanged.
     *
     * @param verifier a function that receives the value and returns a {@link VoidResult}
     *                 indicating whether verification passed.
     * @return this result if verification passes, or an error result if verification fails.
     * @throws NullPointerException if the verifier is null and this is a present result.
     */
    OptionalResult<T, E> verifyValue(Function<T, VoidResult<E>> verifier);
}