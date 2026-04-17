package dev.mhh.result;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Shared behaviour across all result types: {@link Result}, {@link VoidResult},
 * and {@link OptionalResult}.
 *
 * <p>Provides common state-inspection methods ({@link #isOk()}, {@link #isError()}, {@link #error()})
 * and fluent side-effect methods that only act when the result is in a specific state.
 *
 * @param <E> the type of the error.
 * @param <T> the concrete result type (self-referential bound for fluent return types).
 */
interface Shared<E, T extends Shared<E, T>> {

    /**
     * @return Optional.of(err) if the result is an error, Optional.empty() otherwise.
     */
    Optional<E> error();

    /**
     * Check if the result is an error.
     * @return if the result is an error.
     */
    boolean isError();

    /**
     * Check if the result is ok.
     * @return if the result is ok.
     */
    boolean isOk();

    /**
     * Run the given runnable if the result is ok.
     * @param runnable to run. This only runs if the result is ok.
     * @return the result for fluent chaining.
     * @throws NullPointerException if the runnable is null and the result is ok.
     */
    T runIfOk(Runnable runnable);

    /**
     * Run the given runnable if the result is an error.
     * @param runnable to run. This only runs if the result is an error.
     * @return the result for fluent chaining.
     * @throws NullPointerException if the runnable is null and the result is an error.
     */
    T runIfError(Runnable runnable);

    /**
     * Consume the error if the result is an error.
     * @param consumer to consume the error. This only runs if the result is an error.
     * @return the result for fluent chaining.
     * @throws NullPointerException if the consumer is null and the result is an error.
     */
    T consumeError(Consumer<E> consumer);
}