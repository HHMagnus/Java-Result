package dev.mhh.result;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record Ok<T, E>(T value) implements Result<T, E>, Serializable {
    @Override
    public String toString() {
        return "Ok[" + value + ']';
    }

    public Ok {
        Objects.requireNonNull(value);
    }

    @Override
    public Optional<T> optionalValue() {
        return Optional.of(value);
    }

    @Override
    public <N> Result<T, N> mapError(Function<E, N> function) {
        return Result.ok(value);
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
    public Result<T, E> runIfOk(Runnable runnable) {
        runnable.run();
        return this;
    }

    @Override
    public Result<T, E> runIfError(Runnable runnable) {
        return this;
    }

    @Override
    public Result<T, E> consumeError(Consumer<E> consumer) {
        return this;
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public <R> Result<R, E> map(Function<T, R> mapper) {
        final var mapped = mapper.apply(value);
        return Result.ok(mapped);
    }

    @Override
    public <R> Result<R, E> flatMap(Function<T, Result<R, E>> mapper) {
        return mapper.apply(value);
    }

    @Override
    public Result<T, E> consume(Consumer<T> consumer) {
        consumer.accept(value);
        return this;
    }

    @Override
    public Result<T, E> verify(Function<T, VoidResult<E>> verifier) {
        final var voidResult = verifier.apply(value);
        return voidResult.toResult(value);
    }

    @Override
    public VoidResult<E> toVoidResult() {
        return VoidResult.ok();
    }

    @Override
    public OptionalResult<T, E> toOptionalResult() {
        return OptionalResult.ok(value);
    }

    @Override
    public <R> OptionalResult<R, E> mapToOptional(final Function<T, Optional<R>> mapper) {
        final var optional = mapper.apply(value);
        return OptionalResult.okOptional(optional);
    }

    @Override
    public <R> OptionalResult<R, E> flatMapWithOptionalResult(final Function<T, OptionalResult<R, E>> mapper) {
        return mapper.apply(value);
    }
}
