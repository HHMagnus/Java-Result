package dev.mhh.result;

public sealed interface VoidResult<E>
        extends Shared<E, VoidResult<E>>
        permits VoidErr, VoidOk {
    static <E> VoidResult<E> ok() {
        return new VoidOk<>();
    }

    static <E> VoidResult<E> err(E err) {
        return new VoidErr<>(err);
    }

    <R> Result<R, E> toResult(R value);
}
