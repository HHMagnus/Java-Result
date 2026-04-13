package dev.mhh.results;

import java.util.Optional;

interface SharedResultMethods<E> {
    Optional<E> error();
    boolean isError();
    boolean isOk();
}
