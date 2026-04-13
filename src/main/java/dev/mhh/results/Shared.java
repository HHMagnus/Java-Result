package dev.mhh.results;

import java.util.Optional;

interface Shared<E, T extends Shared<E, T>> {
    Optional<E> error();

    boolean isError();
    boolean isOk();

    T runIfOk(Runnable runnable);
    T runIfError(Runnable runnable);
}
