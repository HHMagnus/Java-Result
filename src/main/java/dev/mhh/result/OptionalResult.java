package dev.mhh.result;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface OptionalResult<T, E> extends Shared<E, OptionalResult<T, E>> {
    Optional<T> optionalValue();

    static <T, E> OptionalResult<T, E> ok(T value) {
        return new Present<>(value);
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
}
