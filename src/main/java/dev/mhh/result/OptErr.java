package dev.mhh.result;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public record OptErr<T, E>(E err) implements OptionalResult<T, E>, Serializable {
    public OptErr {
        Objects.requireNonNull(err);
    }

    @Override
    public Optional<T> optionalValue() {
        return Optional.empty();
    }

    @Override
    public boolean isPresent() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public OptionalResult<T, E> runIfPresent(Runnable runnable) {
        return this;
    }

    @Override
    public OptionalResult<T, E> runIfEmpty(Runnable runnable) {
        return this;
    }

    @Override
    public <N> OptionalResult<T, N> mapError(Function<E, N> function) {
        Objects.requireNonNull(function);
        final var error = function.apply(err);
        return OptionalResult.err(error);
    }

    @Override
    public VoidResult<E> toVoidResult() {
        return VoidResult.err(err);
    }

    @Override
    public Result<T, E> toResult(Supplier<E> errorIfEmpty) {
        return Result.err(err);
    }

    @Override
    public <R> OptionalResult<R, E> map(Function<Optional<T>, Optional<R>> mapper) {
        return OptionalResult.err(err);
    }

    @Override
    public <R> OptionalResult<R, E> flatMap(Function<Optional<T>, OptionalResult<R, E>> mapper) {
        return OptionalResult.err(err);
    }

    @Override
    public <R> Result<R, E> flatMapWithResult(final Function<Optional<T>, Result<R, E>> mapper) {
        return Result.err(err);
    }

    @Override
    public OptionalResult<T, E> consume(final Consumer<Optional<T>> consumer) {
        return this;
    }

    @Override
    public OptionalResult<T, E> verify(final Function<Optional<T>, VoidResult<E>> verifier) {
        return OptionalResult.err(err);
    }

    @Override
    public OptionalResult<T, E> filter(final Predicate<T> filter) {
        return this;
    }

    @Override
    public <R> OptionalResult<R, E> mapValue(final Function<T, R> mapper) {
        return OptionalResult.err(err);
    }

    @Override
    public <R> OptionalResult<R, E> mapValueToOptional(final Function<T, Optional<R>> mapper) {
        return OptionalResult.err(err);
    }

    @Override
    public <R> OptionalResult<R, E> flatMapValue(final Function<T, OptionalResult<R, E>> mapper) {
        return OptionalResult.err(err);
    }

    @Override
    public OptionalResult<T, E> consumeValue(final Consumer<T> consumer) {
        return this;
    }

    @Override
    public OptionalResult<T, E> verifyValue(final Function<T, VoidResult<E>> verifier) {
        return this;
    }

    @Override
    public <X extends Throwable> Optional<T> orElseThrow(Function<E, X> exceptionSupplier) throws X {
        Objects.requireNonNull(exceptionSupplier);
        throw Objects.requireNonNull(exceptionSupplier.apply(err));
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<T> ifEmpty, Function<E, X> exceptionSupplier) throws X {
        Objects.requireNonNull(exceptionSupplier);
        throw Objects.requireNonNull(exceptionSupplier.apply(err));
    }

    @Override
    public Optional<E> error() {
        return Optional.of(err);
    }

    @Override
    public boolean isError() {
        return true;
    }

    @Override
    public boolean isOk() {
        return false;
    }

    @Override
    public OptionalResult<T, E> runIfOk(Runnable runnable) {
        return this;
    }

    @Override
    public OptionalResult<T, E> runIfError(Runnable runnable) {
        Objects.requireNonNull(runnable);
        runnable.run();
        return this;
    }

    @Override
    public String toString() {
        return "OptErr[" + err + ']';
    }

    @Override
    public OptionalResult<T, E> consumeError(Consumer<E> consumer) {
        Objects.requireNonNull(consumer);
        consumer.accept(err);
        return this;
    }
}
