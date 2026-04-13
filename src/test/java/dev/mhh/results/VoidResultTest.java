package dev.mhh.results;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class VoidResultTest {
    VoidResult<Long> error10 = VoidResult.err(10L);

    private void assertUnchangedErr(VoidResult<Long> error10) {
        assertTrue(error10.isError());
        assertFalse(error10.isOk());
        assertEquals(Optional.of(10L), error10.error());
    }

    @Test
    void ok() {
        final var success = VoidResult.ok();

        assertTrue(success.isOk());
        assertEquals(Optional.empty(), success.error());
    }

    @Test
    void err() {
        final var error = VoidResult.err(10L);

        assertTrue(error.isError());
        assertEquals(Optional.of(10L), error.error());
    }

    @Test
    void errNonNull() {
        Assertions.assertThrows(NullPointerException.class, () -> VoidResult.err(null));
    }

    @Test
    void toStringTest() {
        assertEquals("Ok", VoidResult.ok().toString());
        assertEquals("Err[10]", VoidResult.err(10L).toString());
    }

    @Test
    void replaceWhenOk() {
        final var success = VoidResult.ok();

        final var replaced = success.replace(10L);

        assertTrue(success.isOk());
        assertTrue(replaced.isOk());
        assertEquals(Optional.of(10L), replaced.value());
        assertEquals(Optional.empty(), replaced.error());
    }

   @Test
   void replaceWhenErr() {
        final var replaced = error10.replace(10L);

       assertUnchangedErr(error10);

        assertTrue(replaced.isError());
        assertEquals(Optional.empty(), replaced.value());
        assertEquals(Optional.of(10L), replaced.error());
   }

    @Test
    void runIfOkWhenOk() {
        final var consumed = new AtomicBoolean(false);

        final var okRan = VoidResult.ok().runIfOk(() -> consumed.set(true));

        assertTrue(okRan.isOk());

        assertTrue(consumed.get());
    }

    @Test
    void runIfOkWhenErr() {
        final var consumed = new AtomicBoolean(false);

        final var error10Ran = error10.runIfOk(() -> consumed.set(true));

        assertUnchangedErr(error10Ran);
        assertUnchangedErr(error10);

        assertFalse(consumed.get());
    }

    @Test
    void runIfErrorWhenOk() {
        final var consumed = new AtomicBoolean(false);

        final var ok10Ran = VoidResult.ok().runIfError(() -> consumed.set(true));

        assertTrue(ok10Ran.isOk());

        assertFalse(consumed.get());
    }
    @Test
    void runIfErrorWhenErr() {
        final var consumed = new AtomicBoolean(false);

        final var error10Ran = error10.runIfError(() -> consumed.set(true));

        assertUnchangedErr(error10Ran);
        assertUnchangedErr(error10);

        assertTrue(consumed.get());
    }

    @Test
    void consumeErrorWhenOk() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = VoidResult.ok().consumeError(_ -> consumed.set(true));

        assertTrue(consumedResult.isOk());

        assertFalse(consumed.get());
    }

    @Test
    void consumeErrorWhenErr() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = error10.consumeError(_ -> consumed.set(true));

        assertUnchangedErr(consumedResult);
        assertUnchangedErr(error10);

        assertTrue(consumed.get());
    }
}
