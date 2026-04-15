package dev.mhh.result;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public record VoidOk<E>() implements VoidResult<E>, Serializable {
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
        Objects.requireNonNull(runnable);
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
    public <N> VoidResult<N> mapError(Function<E, N> function) {
        return VoidResult.ok();
    }

    @Override
    public <T> Result<T, E> toResult(T value) {
        return Result.ok(value);
    }

    @Override
    public <R> OptionalResult<R, E> toOptionalResult(R optionalValue) {
        return OptionalResult.ok(optionalValue);
    }

    @Override
    public <R> OptionalResult<R, E> toOptionalResult() {
        return OptionalResult.empty();
    }

    @Override
    public VoidResult<E> verify(VoidResult<E> verified) {
        return verified;
    }

    @Override
    public VoidResult<E> verify(Supplier<VoidResult<E>> consumer) {
        return consumer.get();
    }
}
