package dev.mhh.results;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public record Err<T, E>(E err) implements Result<T, E>, VoidResult<E> {
    public Err {
        Objects.requireNonNull(err);
    }

    @Override
    public Optional<T> value() {
        return Optional.empty();
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
        runnable.run();
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
    public Result<T, E> flatConsume(Function<T, VoidResult<E>> consumer) {
        return this;
    }

    @Override
    public <R> Result<R, E> replace(R value) {
        return Result.err(err);
    }
}
