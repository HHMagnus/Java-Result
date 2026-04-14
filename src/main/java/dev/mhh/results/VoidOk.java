package dev.mhh.results;

import java.util.Optional;
import java.util.function.Consumer;

public record VoidOk<E>() implements VoidResult<E> {
    @Override
    public String toString() {
        return "Ok";
    }

    @Override
    public Optional<E> error() {
        return Optional.empty();
    }

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public VoidResult<E> runIfOk(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    public VoidResult<E> runIfError(Runnable runnable) {
        return this;
    }

    @Override
    public VoidResult<E> consumeError(Consumer<E> consumer) {
        return this;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public <T> Result<T, E> toResult(T value) {
        return Result.ok(value);
    }
}
