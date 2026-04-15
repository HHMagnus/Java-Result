package dev.mhh.result;

import java.util.function.Function;
import java.util.function.Supplier;

public sealed interface VoidResult<E>
        extends Shared<E, VoidResult<E>>
        permits VoidErr, VoidOk {
    static <E> VoidResult<E> ok() {
        return new VoidOk<>();
    }

    static <E> VoidResult<E> err(E err) {
        return new VoidErr<>(err);
    }

    <N> VoidResult<N> mapError(Function<E, N> function);

    <R> Result<R, E> toResult(R value);
    <R> OptionalResult<R, E> toOptionalResult(R optionalValue);
    <R> OptionalResult<R, E> toOptionalResult();

    VoidResult<E> verify(VoidResult<E> verified);
    VoidResult<E> verify(Supplier<VoidResult<E>> consumer);
}
