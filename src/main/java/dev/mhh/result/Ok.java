package dev.mhh.result;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
    public <N> Result<T, N> mapError(Function<E, N> mapper) {
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
        Objects.requireNonNull(runnable);
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
        Objects.requireNonNull(mapper);
        final var mapped = Objects.requireNonNull(mapper.apply(value));
        return Result.ok(mapped);
    }

    @Override
    public <R> Result<R, E> flatMap(Function<T, Result<R, E>> mapper) {
        Objects.requireNonNull(mapper);
        return Objects.requireNonNull(mapper.apply(value));
    }

    @Override
    public Result<T, E> consume(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        consumer.accept(value);
        return this;
    }

    @Override
    public Result<T, E> verify(Function<T, VoidResult<E>> verifier) {
        Objects.requireNonNull(verifier);
        final var voidResult = Objects.requireNonNull(verifier.apply(value));
        return voidResult.toResult(value);
    }

    @Override
    public Result<T, E> verify(Predicate<T> predicate, Supplier<E> errorSupplier) {
        return verify(VoidResult.validate(predicate, errorSupplier));
    }

    @Override
    public Result<T, E> verify(Predicate<T> predicate, E error) {
        return verify(VoidResult.validate(predicate, error));
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
        Objects.requireNonNull(mapper);
        final var optional = Objects.requireNonNull(mapper.apply(value));
        return OptionalResult.okOptional(optional);
    }

    @Override
    public <R> OptionalResult<R, E> flatMapWithOptionalResult(final Function<T, OptionalResult<R, E>> mapper) {
        Objects.requireNonNull(mapper);
        return Objects.requireNonNull(mapper.apply(value));
    }

    @Override
    public OptionalResult<T, E> filter(Predicate<T> filter) {
        Objects.requireNonNull(filter);
        final var optional = Optional.of(value)
                .filter(filter);
        return OptionalResult.okOptional(optional);
    }

    @Override
    public <X extends Throwable> T orElseThrow(Function<E, X> exceptionSupplier) throws X {
        return value;
    }
}
