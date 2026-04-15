package dev.mhh.result;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public record Empty<T, E>() implements OptionalResult<T, E>, Serializable {
    @Override
    public Optional<T> optionalValue() {
        return Optional.empty();
    }

    @Override
    public <N> OptionalResult<T, N> mapError(Function<E, N> function) {
        return OptionalResult.empty();
    }

    @Override
    public VoidResult<E> toVoidResult() {
        return VoidResult.ok();
    }

    @Override
    public Result<T, E> toResult(Supplier<E> errorIfEmpty) {
        Objects.requireNonNull(errorIfEmpty);
        final var error = errorIfEmpty.get();
        return Result.err(error);
    }

    @Override
    public <R> OptionalResult<R, E> map(Function<Optional<T>, Optional<R>> mapper) {
        Objects.requireNonNull(mapper);
        final var optional = mapper.apply(Optional.empty());
        return optional.map(OptionalResult::<R, E>ok)
                .orElse(OptionalResult.empty());
    }

    @Override
    public <R> OptionalResult<R, E> flatMap(Function<Optional<T>, OptionalResult<R, E>> mapper) {
        Objects.requireNonNull(mapper);
        return mapper.apply(Optional.empty());
    }

    @Override
    public <R> Result<R, E> flatMapWithResult(final Function<Optional<T>, Result<R, E>> mapper) {
        Objects.requireNonNull(mapper);
        return mapper.apply(Optional.empty());
    }

    @Override
    public OptionalResult<T, E> consume(final Consumer<Optional<T>> consumer) {
        Objects.requireNonNull(consumer);
        consumer.accept(Optional.empty());
        return this;
    }

    @Override
    public OptionalResult<T, E> verify(final Function<Optional<T>, VoidResult<E>> verifier) {
        Objects.requireNonNull(verifier);
        final var voidResult = verifier.apply(Optional.empty());
        return voidResult.toOptionalResult();
    }

    @Override
    public <R> OptionalResult<R, E> mapValue(final Function<T, R> mapper) {
        return OptionalResult.empty();
    }

    @Override
    public <R> OptionalResult<R, E> mapValueToOptional(final Function<T, Optional<R>> mapper) {
        return OptionalResult.empty();
    }

    @Override
    public <R> OptionalResult<R, E> flatMapValue(final Function<T, OptionalResult<R, E>> mapper) {
        return OptionalResult.empty();
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
    public String toString() {
        return "Empty";
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
}
