package dev.mhh.result;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
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
        final var optional = mapper.apply(Optional.of(value));
        return OptionalResult.okOptional(optional);
    }

    @Override
    public <R> OptionalResult<R, E> flatMap(Function<Optional<T>, OptionalResult<R, E>> mapper) {
        return mapper.apply(Optional.of(value));
    }

    @Override
    public <R> Result<R, E> flatMapWithResult(final Function<Optional<T>, Result<R, E>> mapper) {
        return mapper.apply(Optional.of(value));
    }

    @Override
    public OptionalResult<T, E> consume(final Consumer<Optional<T>> consumer) {
        consumer.accept(Optional.of(value));
        return this;
    }

    @Override
    public OptionalResult<T, E> verify(final Function<Optional<T>, VoidResult<E>> verifier) {
        final var voidResult = verifier.apply(Optional.of(value));
        return voidResult.toOptionalResult(value);
    }

    @Override
    public <R> OptionalResult<R, E> mapValue(final Function<T, R> mapper) {
        final var mappedValue = mapper.apply(value);
        return OptionalResult.ok(mappedValue);
    }

    @Override
    public <R> OptionalResult<R, E> mapValueToOptional(final Function<T, Optional<R>> mapper) {
        final var optional = mapper.apply(value);
        return OptionalResult.okOptional(optional);
    }

    @Override
    public <R> OptionalResult<R, E> flatMapValue(final Function<T, OptionalResult<R, E>> mapper) {
        return mapper.apply(value);
    }
}
