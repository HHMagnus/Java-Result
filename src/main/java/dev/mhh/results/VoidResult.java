package dev.mhh.results;

import java.util.Optional;

public sealed interface VoidResult<E>
        permits Err, VoidOk {
    static <E> VoidResult<E> ok() {
        return new VoidOk<>();
    }

    static <E> VoidResult<E> err(E err) {
        return new Err<>(err);
    }

    Optional<E> error();

    boolean isError();
    boolean isOk();

    <R> Result<R, E> replace(R value);
}
