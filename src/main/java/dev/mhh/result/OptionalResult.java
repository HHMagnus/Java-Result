package dev.mhh.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface OptionalResult<T, E> extends Shared<E, OptionalResult<T, E>> {
    Optional<T> optionalValue();

    static <T, E> OptionalResult<T, E> ok(T value) {
        return new Present<>(value);
    }

    // This helps fluently transform a returned Optional into an OptionalResult
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static <T, E> OptionalResult<T, E> okOptional(Optional<T> optionalValue) {
        return optionalValue.map(OptionalResult::<T, E>ok)
                .orElse(OptionalResult.empty());
    }

    static <T, E> OptionalResult<T, E> empty() {
        return new Empty<>();
    }

    static <T, E> OptionalResult<T, E> err(E error) {
        return new OptErr<>(error);
    }

    <N> OptionalResult<T, N> mapError(Function<E, N> function);

    VoidResult<E> toVoidResult();
    Result<T, E> toResult(Supplier<E> errorIfEmpty);

    <R> OptionalResult<R, E> map(Function<Optional<T>, Optional<R>> mapper);
    <R> OptionalResult<R, E> flatMap(Function<Optional<T>, OptionalResult<R, E>> mapper);
    <R> Result<R, E> flatMapWithResult(Function<Optional<T>, Result<R, E>> mapper);
    OptionalResult<T, E> consume(Consumer<Optional<T>> consumer);
    OptionalResult<T, E> verify(Function<Optional<T>, VoidResult<E>> verifier);

    <R> OptionalResult<R, E> mapValue(Function<T, R> mapper);
}
