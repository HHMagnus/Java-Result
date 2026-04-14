package dev.mhh.result;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record Ok<T, E>(T ok) implements Result<T, E>, Serializable {
    @Override
    public String toString() {
        return "Ok[" + ok + ']';
    }

    public Ok {
        Objects.requireNonNull(ok);
    }

    @Override
    public Optional<T> value() {
        return Optional.of(ok);
    }

    @Override
    public <N> Result<T, N> mapError(Function<E, N> function) {
        return Result.ok(ok);
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
        final var value = mapper.apply(ok);
        return Result.ok(value);
    }

    @Override
    public <R> Result<R, E> flatMap(Function<T, Result<R, E>> mapper) {
        return mapper.apply(ok);
    }

    @Override
    public Result<T, E> consume(Consumer<T> consumer) {
        consumer.accept(ok);
        return this;
    }

    @Override
    public Result<T, E> flatConsume(Function<T, VoidResult<E>> consumer) {
        final var voidResult = consumer.apply(ok);
        return voidResult.toResult(ok);
    }

    @Override
    public VoidResult<E> toVoidResult() {
        return VoidResult.ok();
    }

    @Override
    public OptionalResult<T, E> toOptionalResult() {
        return OptionalResult.ok(ok);
    }
}
