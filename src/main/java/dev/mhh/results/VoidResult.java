package dev.mhh.results;

import java.util.Objects;
import java.util.Optional;

public sealed interface VoidResult<E> {
    record Ok<E>() implements VoidResult<E> {
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

    record Err<E>(E err) implements VoidResult<E> {
        public Err {
            Objects.requireNonNull(err);
        }

        @Override
        public String toString() {
            return "Err[" + err + ']';
        }

        @Override
        public Optional<E> error() {
            return Optional.of(err);
        }

        @Override
        public boolean isOk() {
            return false;
        }

        @Override
        public boolean isError() {
            return true;
        }

        @Override
        public <T> Result<T, E> replace(T value) {
            return Result.err(err);
        }
    }

    static <E> VoidResult<E> ok() {
        return new Ok<>();
    }

    static <E> VoidResult<E> err(E err) {
        return new Err<>(err);
    }

    Optional<E> error();

    boolean isOk();
    boolean isError();

    <T> Result<T, E> replace(T value);
}
