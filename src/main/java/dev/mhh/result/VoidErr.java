package dev.mhh.result;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public record VoidErr<E>(E err) implements VoidResult<E>, Serializable {
    public VoidErr {
        Objects.requireNonNull(err);
    }

    @Override
    public Optional<E> error() {
        return Optional.ofNullable(err);
    }

    @Override
    public String toString() {
        return "Err[" + err + ']';
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public VoidResult<E> runIfOk(Runnable runnable) {
        return this;
    }

    @Override
    public VoidResult<E> runIfError(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    public VoidResult<E> consumeError(Consumer<E> consumer) {
        consumer.accept(err);
        return this;
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public <N> VoidResult<N> mapError(Function<E, N> function) {
        final var error = function.apply(err);
        return VoidResult.err(error);
    }

    @Override
    public <R> Result<R, E> toResult(R value) {
        return Result.err(err);
    }

    @Override
    public <R> OptionalResult<R, E> toOptionalResult(R optionalValue) {
        return OptionalResult.err(err);
    }

    @Override
    public <R> OptionalResult<R, E> toOptionalResult() {
        return OptionalResult.err(err);
    }

    @Override
    public VoidResult<E> verify(Supplier<VoidResult<E>> consumer) {
        return this;
    }
}
