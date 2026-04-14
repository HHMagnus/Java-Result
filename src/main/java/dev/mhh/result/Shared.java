package dev.mhh.result;

import java.util.Optional;
import java.util.function.Consumer;

interface Shared<E, T extends Shared<E, T>> {
    Optional<E> error();

    boolean isError();
    boolean isOk();

    T runIfOk(Runnable runnable);
    T runIfError(Runnable runnable);

    T consumeError(Consumer<E> consumer);
}
