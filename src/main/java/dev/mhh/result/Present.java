package dev.mhh.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

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
}
