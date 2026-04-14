package dev.mhh.result;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public record OptErr<T, E>(E err) implements OptionalResult<T, E>{
    public OptErr {
        Objects.requireNonNull(err);
    }

    @Override
    public Optional<T> optionalValue() {
        return Optional.empty();
    }

    @Override
    public VoidResult<E> toVoidResult() {
        return VoidResult.err(err);
    }

    @Override
    public Result<T, E> toResult(Supplier<E> errorIfEmpty) {
        return Result.err(err);
    }

    @Override
    public Optional<E> error() {
        return Optional.of(err);
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public OptionalResult<T, E> runIfOk(Runnable runnable) {
        return this;
    }

    @Override
    public OptionalResult<T, E> runIfError(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    public String toString() {
        return "OptErr[" + err + ']';
    }

    @Override
    public OptionalResult<T, E> consumeError(Consumer<E> consumer) {
        consumer.accept(err);
        return this;
    }
}
