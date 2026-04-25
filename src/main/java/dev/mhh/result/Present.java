package dev.mhh.result;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public record Present<T, E>(T value) implements OptionalResult<T, E>, Serializable {
    public Present {
        Objects.requireNonNull(value);
    }

    @Override
    public String toString() {
        return "Present[" + value + ']';
    }

    @Override
    public Optional<E> error() {
        return Optional.empty();
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public boolean isOk() {
        return true;
    }

    @Override
    public OptionalResult<T, E> runIfOk(Runnable runnable) {
        Objects.requireNonNull(runnable);
        runnable.run();
        return this;
    }

    @Override
    public OptionalResult<T, E> runIfError(Runnable runnable) {
        return this;
    }

    @Override
    public OptionalResult<T, E> consumeError(Consumer<E> consumer) {
        return this;
    }

    @Override
    public Optional<T> optionalValue() {
        return Optional.of(value);
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public OptionalResult<T, E> runIfPresent(Runnable runnable) {
        Objects.requireNonNull(runnable);
        runnable.run();
        return this;
    }

    @Override
    public OptionalResult<T, E> runIfEmpty(Runnable runnable) {
        return this;
    }

    @Override
    public <N> OptionalResult<T, N> mapError(Function<E, N> function) {
        return OptionalResult.ok(value);
    }

    @Override
    public VoidResult<E> toVoidResult() {
        return VoidResult.ok();
    }

    @Override
    public Result<T, E> toResult(Supplier<E> errorIfEmpty) {
        return Result.ok(value);
    }

    @Override
    public <R> OptionalResult<R, E> map(Function<Optional<T>, Optional<R>> mapper) {
        Objects.requireNonNull(mapper);
        final var optional = Objects.requireNonNull(mapper.apply(Optional.of(value)));
        return OptionalResult.okOptional(optional);
    }

    @Override
    public <R> OptionalResult<R, E> flatMap(Function<Optional<T>, OptionalResult<R, E>> mapper) {
        Objects.requireNonNull(mapper);
        return Objects.requireNonNull(mapper.apply(Optional.of(value)));
    }

    @Override
    public <R> Result<R, E> flatMapWithResult(final Function<Optional<T>, Result<R, E>> mapper) {
        Objects.requireNonNull(mapper);
        return Objects.requireNonNull(mapper.apply(Optional.of(value)));
    }

    @Override
    public OptionalResult<T, E> consume(final Consumer<Optional<T>> consumer) {
        Objects.requireNonNull(consumer);
        consumer.accept(Optional.of(value));
        return this;
    }

    @Override
    public OptionalResult<T, E> verify(final Function<Optional<T>, VoidResult<E>> verifier) {
        Objects.requireNonNull(verifier);
        final var voidResult = Objects.requireNonNull(verifier.apply(Optional.of(value)));
        return voidResult.toOptionalResult(value);
    }

    @Override
    public OptionalResult<T, E> verify(Predicate<Optional<T>> predicate, E error) {
        return verify(VoidResult.validate(predicate, error));
    }

    @Override
    public OptionalResult<T, E> verify(Predicate<Optional<T>> predicate, Supplier<E> errorSupplier) {
        return verify(VoidResult.validate(predicate, errorSupplier));
    }

    @Override
    public OptionalResult<T, E> filter(final Predicate<T> filter) {
        Objects.requireNonNull(filter);
        final var optional = Optional.of(value)
                .filter(filter);
        return OptionalResult.okOptional(optional);
    }

    @Override
    public OptionalResult<T, E> or(Supplier<Optional<T>> supplier) {
        return this;
    }

    @Override
    public <R> OptionalResult<R, E> mapValue(final Function<T, R> mapper) {
        Objects.requireNonNull(mapper);
        final var mappedValue = Objects.requireNonNull(mapper.apply(value));
        return OptionalResult.ok(mappedValue);
    }

    @Override
    public <R> OptionalResult<R, E> mapValueToOptional(final Function<T, Optional<R>> mapper) {
        Objects.requireNonNull(mapper);
        final var optional = Objects.requireNonNull(mapper.apply(value));
        return OptionalResult.okOptional(optional);
    }

    @Override
    public <R> OptionalResult<R, E> flatMapValue(final Function<T, OptionalResult<R, E>> mapper) {
        Objects.requireNonNull(mapper);
        return Objects.requireNonNull(mapper.apply(value));
    }

    @Override
    public OptionalResult<T, E> consumeValue(final Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        consumer.accept(value);
        return this;
    }

    @Override
    public OptionalResult<T, E> verifyValue(final Function<T, VoidResult<E>> verifier) {
        Objects.requireNonNull(verifier);
        final var voidResult = Objects.requireNonNull(verifier.apply(value));
        return voidResult.toOptionalResult(value);
    }

    @Override
    public OptionalResult<T, E> verifyValue(Predicate<T> predicate, E error) {
        return verifyValue(VoidResult.validate(predicate, error));
    }

    @Override
    public OptionalResult<T, E> verifyValue(Predicate<T> predicate, Supplier<E> errorSupplier) {
        return verifyValue(VoidResult.validate(predicate, errorSupplier));
    }

    @Override
    public <X extends Throwable> Optional<T> orElseThrow(Function<E, X> exceptionSupplier) throws X {
        return Optional.of(value);
    }

    @Override
    public <X extends Throwable> T orElseThrow(Supplier<T> ifEmpty, Function<E, X> exceptionSupplier) throws X {
        return value;
    }
}
