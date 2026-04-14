package dev.mhh.result;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record Empty<T, E>() implements OptionalResult<T, E> {
    @Override
    public Optional<T> optionalValue() {
        return Optional.empty();
    }

    @Override
    public VoidResult<E> toVoidResult() {
        return VoidResult.ok();
    }

    @Override
    public Result<T, E> toResult(Supplier<E> errorIfEmpty) {
        final var error = errorIfEmpty.get();
        return Result.err(error);
    }

    @Override
    public String toString() {
        return "Empty";
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
}
