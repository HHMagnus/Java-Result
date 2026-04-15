package dev.mhh.result;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record Err<T, E>(E err) implements Result<T, E>, Serializable {
    public Err {
        Objects.requireNonNull(err);
    }

    @Override
    public Optional<T> optionalValue() {
        return Optional.empty();
    }

    @Override
    public <N> Result<T, N> mapError(Function<E, N> mapper) {
        Objects.requireNonNull(mapper);
        final var error = mapper.apply(err);
        return Result.err(error);
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
    public Result<T, E> runIfOk(Runnable runnable) {
        return this;
    }

    @Override
    public Result<T, E> runIfError(Runnable runnable) {
        Objects.requireNonNull(runnable);
        runnable.run();
        return this;
    }

    @Override
    public Result<T, E> consumeError(Consumer<E> consumer) {
        Objects.requireNonNull(consumer);
        consumer.accept(err);
        return this;
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public <R> Result<R, E> map(Function<T, R> mapper) {
        return Result.err(err);
    }

    @Override
    public <R> Result<R, E> flatMap(Function<T, Result<R, E>> mapper) {
        return Result.err(err);
    }

    @Override
    public Result<T, E> consume(Consumer<T> consumer) {
        return this;
    }

    @Override
    public Result<T, E> verify(Function<T, VoidResult<E>> verifier) {
        return this;
    }

    @Override
    public VoidResult<E> toVoidResult() {
        return VoidResult.err(err);
    }

    @Override
    public OptionalResult<T, E> toOptionalResult() {
        return OptionalResult.err(err);
    }

    @Override
    public <R> OptionalResult<R, E> mapToOptional(final Function<T, Optional<R>> mapper) {
        return OptionalResult.err(err);
    }

    @Override
    public <R> OptionalResult<R, E> flatMapWithOptionalResult(final Function<T, OptionalResult<R, E>> mapper) {
        return OptionalResult.err(err);
    }
}
