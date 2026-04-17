package dev.mhh.result;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A result type representing either success with no value, or failure with an error.
 * This is useful for operations that do not produce a meaningful return value —
 * analogous to {@code Result<(), E>} in Rust or a checked version of {@code void}.
 *
 * <p>Like the other result types, operations on an error result are passed through without
 * invoking any mapper or supplier, allowing fluent chains without defensive checks at each step.
 *
 * @param <E> the type of the error.
 */
public sealed interface VoidResult<E>
        extends Shared<E, VoidResult<E>>
        permits VoidErr, VoidOk {

    /**
     * Creates a successful {@code VoidResult} with no value.
     *
     * @param <E> the type of the error.
     * @return a successful {@code VoidResult}.
     */
    static <E> VoidResult<E> ok() {
        return new VoidOk<>();
    }

    /**
     * Creates an error {@code VoidResult} containing the given error.
     *
     * @param err the error value. Cannot be null.
     * @param <E> the type of the error.
     * @return an error {@code VoidResult} containing the error.
     * @throws NullPointerException if err is null.
     */
    static <E> VoidResult<E> err(E err) {
        return new VoidErr<>(err);
    }

    /**
     * Maps the error value to a different error type, leaving a successful result unchanged.
     *
     * @param function the mapping function applied to the error. Only called if this is an error result.
     * @param <N>      the new error type.
     * @return a new {@code VoidResult} with the mapped error, or the same successful result unchanged.
     * @throws NullPointerException if the function is null and this is an error result.
     */
    <N> VoidResult<N> mapError(Function<E, N> function);

    /**
     * Converts this {@code VoidResult} to a {@link Result} by supplying a success value.
     * If this is an error result, the value is ignored and the error is passed through.
     *
     * @param value the value to use if this result is ok.
     * @param <R>   the type of the success value.
     * @return an ok {@link Result} containing the value, or an error {@link Result}.
     */
    <R> Result<R, E> toResult(R value);

    /**
     * Converts this {@code VoidResult} to a present {@link OptionalResult} using the given value.
     * If this is an error result, the value is ignored and the error is passed through.
     *
     * @param optionalValue the value to wrap in a present {@link OptionalResult} if this result is ok.
     * @param <R>           the type of the value.
     * @return a present {@link OptionalResult} containing the value, or an error {@link OptionalResult}.
     */
    <R> OptionalResult<R, E> toOptionalResult(R optionalValue);

    /**
     * Converts this {@code VoidResult} to an empty {@link OptionalResult}.
     * If this is an error result, the error is passed through.
     *
     * @param <R> the type of the (absent) value.
     * @return an empty {@link OptionalResult} if ok, or an error {@link OptionalResult}.
     */
    <R> OptionalResult<R, E> toOptionalResult();

    /**
     * Chains another {@code VoidResult} onto this one.
     * If this result is ok, the provided result is returned.
     * If this result is an error, the argument is ignored and the error is passed through.
     *
     * @param verified the next {@code VoidResult} to return if this result is ok.
     * @return the given result if this is ok, or this error result if this is an error.
     */
    VoidResult<E> verify(VoidResult<E> verified);

    /**
     * Chains a lazily-supplied {@code VoidResult} onto this one.
     * If this result is ok, the supplier is called and its result is returned.
     * If this result is an error, the supplier is not called and the error is passed through.
     *
     * @param supplier a supplier providing the next {@code VoidResult} to evaluate.
     * @return the result from the supplier if this is ok, or this error result if this is an error.
     * @throws NullPointerException if the supplier is null and this result is ok.
     */
    VoidResult<E> verify(Supplier<VoidResult<E>> supplier);
}