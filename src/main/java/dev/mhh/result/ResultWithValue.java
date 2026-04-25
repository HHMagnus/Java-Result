package dev.mhh.result;

import java.util.function.Consumer;

/**
 * Marker interface for Result and OptionalResult to determine their value types
 */
public sealed interface ResultWithValue<T, E> permits OptionalResult, Result {
    Object consume(Consumer<T> consumer);
    Object consumeError(Consumer<E> consumer);
}
