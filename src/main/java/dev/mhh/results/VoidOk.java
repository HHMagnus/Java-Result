package dev.mhh.results;

import java.util.Optional;

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
    public boolean isError() {
        return false;
    }

    @Override
    public <T> Result<T, E> replace(T value) {
        return Result.ok(value);
    }
}
