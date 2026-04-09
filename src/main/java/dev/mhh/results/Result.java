package dev.mhh.results;

import java.util.Optional;
import java.util.function.Function;

public class Result<T, E> {
    private final T value;
    private final E error;

    private Result(T value, E error) {
        this.value = value;
        this.error = error;
    }

    public static <T, E> Result<T, E> of(T value) {
        return new Result<>(value, null);
    }

    public static <T, E> Result<T, E> error(E error) {
        return new Result<>(null, error);
    }

    public boolean isError() {
        return error != null;
    }

    public boolean isSuccess() {
        return !isError();
    }

    @Override
    public String toString() {
        if (isError()) {
            return "Result error (" + error + ")";
        }
        return "Result success (" + value + ")";
    }

    public Optional<T> value() {
        return Optional.ofNullable(value);
    }

    public Optional<E> error() {
        return Optional.ofNullable(error);
    }

    public <R> Result<R, E> map(Function<T, R> mapper) {
        return isSuccess()
                ? of(mapper.apply(value))
                : error(error);
    }

    public <R> Result<R, E> flatMap(Function<T, Result<R, E>> mapper) {
        return isSuccess()
                ? mapper.apply(value)
                : error(error);
    }
}
