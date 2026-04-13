package dev.mhh.results;

import java.util.Objects;
import java.util.Optional;

public record VoidErr<E>(E err) implements VoidResult<E> {
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
    public boolean isError() {
        return true;
    }

    @Override
    public <R> Result<R, E> replace(R value) {
        return Result.err(err);
    }
}
