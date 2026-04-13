package dev.mhh.results;

public sealed interface VoidResult<E>
        extends SharedResultMethods<E>
        permits Err, VoidOk {
    static <E> VoidResult<E> ok() {
        return new VoidOk<>();
    }

    static <E> VoidResult<E> err(E err) {
        return new Err<>(err);
    }

    <R> Result<R, E> replace(R value);
}
