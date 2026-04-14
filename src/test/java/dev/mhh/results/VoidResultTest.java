package dev.mhh.results;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

class VoidResultTest {
    VoidResult<Long> ok = VoidResult.ok();
    VoidResult<Long> error10 = VoidResult.err(10L);

    private void assertUnchangedOk(VoidResult<Long> ok) {
        assertTrue(ok.isOk());
        assertFalse(ok.isError());
        assertEquals(Optional.empty(), ok.error());
    }

    private void assertUnchangedErr(VoidResult<Long> error10) {
        assertTrue(error10.isError());
        assertFalse(error10.isOk());
        assertEquals(Optional.of(10L), error10.error());
    }

    @Test
    void manualCreation() {
        final var ok = new VoidOk<Long>();
        assertUnchangedOk(ok);

        final var err = new VoidErr<>(10L);
        assertUnchangedErr(err);
        assertThrows(NullPointerException.class, () -> new VoidErr<>(null));
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
    void toResultWhenOk() {
        final var replaced = ok.toResult(10L);

        assertUnchangedOk(ok);

        assertTrue(replaced.isOk());
        assertEquals(Optional.of(10L), replaced.value());
        assertEquals(Optional.empty(), replaced.error());
    }

   @Test
   void toResultWhenErr() {
        final var replaced = error10.toResult(10L);

       assertUnchangedErr(error10);

        assertTrue(replaced.isError());
        assertEquals(Optional.empty(), replaced.value());
        assertEquals(Optional.of(10L), replaced.error());
   }

    @Test
    void runIfOkWhenOk() {
        final var consumed = new AtomicBoolean(false);

        final var okRan = ok.runIfOk(() -> consumed.set(true));

        assertUnchangedOk(okRan);
        assertUnchangedOk(ok);

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

        final var okRan = ok.runIfError(() -> consumed.set(true));

        assertUnchangedOk(okRan);
        assertUnchangedOk(ok);

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
        final var consumedResult = ok.consumeError(_ -> consumed.set(true));

        assertUnchangedOk(consumedResult);
        assertUnchangedOk(ok);

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
