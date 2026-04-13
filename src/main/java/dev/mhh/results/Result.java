package dev.mhh.results;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public sealed interface Result<T, E> {
    record Ok<T, E>(T ok) implements Result<T, E>{
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
    }

    record Err<T, E>(E err) implements Result<T, E>{
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
    }

    static <T, E> Result<T, E> ok(T value) {
        return new Ok<>(value);
    }

    static <T, E> Result<T, E> err(E value) {
        return new Err<>(value);
    }

    Optional<T> value();
    Optional<E> error();

    boolean isOk();
    boolean isError();

    <R> Result<R, E> map(Function<T, R> mapper);
    <R> Result<R, E> flatMap(Function<T, Result<R, E>> mapper);
    Result<T, E> consume(Consumer<T> consumer);
}
