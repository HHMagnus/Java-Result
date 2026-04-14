package dev.mhh.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public record Present<T, E>(T value) implements OptionalResult<T, E> {
    public Present {
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return "Present[" + value + ']';
    }

    @Override
    public Optional<E> error() {
        return Optional.empty();
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public OptionalResult<T, E> runIfOk(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    public OptionalResult<T, E> runIfError(Runnable runnable) {
        return this;
    }

    @Override
    public OptionalResult<T, E> consumeError(Consumer<E> consumer) {
        return this;
    }

    @Override
    public Optional<T> optionalValue() {
        return Optional.of(value);
    }

    @Override
    public <N> OptionalResult<T, N> mapError(Function<E, N> function) {
        return OptionalResult.ok(value);
    }

    @Override
    public VoidResult<E> toVoidResult() {
        return VoidResult.ok();
    }

    @Override
    public Result<T, E> toResult(Supplier<E> errorIfEmpty) {
        return Result.ok(value);
    }
}
