package dev.mhh.result;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class OptionalResultTest {
    OptionalResult<Long, Long> ok10 = OptionalResult.ok(10L);
    OptionalResult<Long, Long> okEmpty = OptionalResult.empty();
    OptionalResult<Long, Long> error10 = OptionalResult.err(10L);

    private void assertUnchangedPresent(OptionalResult<Long, Long> result) {
        assertTrue(result.isOk());
        assertFalse(result.isError());
        assertEquals(Optional.of(10L), result.optionalValue());
        assertEquals(Optional.empty(), result.error());
    }

    private void assertUnchangedEmpty(OptionalResult<Long, Long> result) {
        assertTrue(result.isOk());
        assertFalse(result.isError());
        assertEquals(Optional.empty(), result.optionalValue());
        assertEquals(Optional.empty(), result.error());
    }

    private void assertUnchangedErr(OptionalResult<Long, Long> result) {
        assertFalse(result.isOk());
        assertTrue(result.isError());
        assertEquals(Optional.empty(), result.optionalValue());
        assertEquals(Optional.of(10L), result.error());
    }

    @Test
    public void manualInitiation() {
        final var present = new Present<Long, Long>(10L);
        assertUnchangedPresent(present);

        final var empty = new Empty<Long, Long>();
        assertUnchangedEmpty(empty);

        final var err = new OptErr<Long, Long>(10L);
        assertUnchangedErr(err);

        assertThrows(NullPointerException.class, () -> new Present<>(null));
        assertThrows(NullPointerException.class, () -> new OptErr<>(null));
    }

    @Test
    void present() {
        final var present = OptionalResult.<Long, Long>ok(10L);
        assertUnchangedPresent(present);
    }

    @Test
    void empty() {
        final var empty = OptionalResult.<Long, Long>empty();
        assertUnchangedEmpty(empty);
    }

    @Test
    void err() {
        final var error = OptionalResult.<Long, Long>err(10L);
        assertUnchangedErr(error);
    }

    @Test
    void toStringTest() {
        assertEquals("Present[10]", ok10.toString());
        assertEquals("Empty", okEmpty.toString());
        assertEquals("OptErr[10]", error10.toString());
    }

    @Test
    void runIfOkWhenPresent() {
        final var consumed = new AtomicBoolean(false);

        final var okRan = ok10.runIfOk(() -> consumed.set(true));

        assertUnchangedPresent(okRan);
        assertUnchangedPresent(ok10);

        assertTrue(consumed.get());
    }

    @Test
    void runIfOkWhenEmpty() {
        final var consumed = new AtomicBoolean(false);

        final var okEmptyRan = okEmpty.runIfOk(() -> consumed.set(true));

        assertUnchangedEmpty(okEmptyRan);
        assertUnchangedEmpty(okEmpty);

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
    void runIfErrorWhenPresent() {
        final var consumed = new AtomicBoolean(false);

        final var ok10Ran = ok10.runIfError(() -> consumed.set(true));

        assertUnchangedPresent(ok10Ran);
        assertUnchangedPresent(ok10);

        assertFalse(consumed.get());
    }

    @Test
    void runIfErrorWhenEmpty() {
        final var consumed = new AtomicBoolean(false);

        final var okEmptyRan = okEmpty.runIfError(() -> consumed.set(true));

        assertUnchangedEmpty(okEmptyRan);
        assertUnchangedEmpty(okEmpty);

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
    void consumeErrorWhenPresent() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = ok10.consumeError(_ -> consumed.set(true));

        assertUnchangedPresent(consumedResult);
        assertUnchangedPresent(ok10);

        assertFalse(consumed.get());
    }

    @Test
    void consumeErrorWhenEmpty() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = okEmpty.consumeError(_ -> consumed.set(true));

        assertUnchangedEmpty(consumedResult);
        assertUnchangedEmpty(okEmpty);

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

    @Test
    void toVoidResultWhenPresent() {
        final var voidResult = ok10.toVoidResult();

        assertTrue(voidResult.isOk());
        assertUnchangedPresent(ok10);
    }

    @Test
    void toVoidResultWhenEmpty() {
        final var voidResult = okEmpty.toVoidResult();

        assertTrue(voidResult.isOk());
        assertUnchangedEmpty(okEmpty);
    }

    @Test
    void toVoidResultWhenErr() {
        final var voidResult = error10.toVoidResult();

        assertTrue(voidResult.isError());
        assertUnchangedErr(error10);
    }

    @Test
    void toResultWhenPresent() {
        final var called = new AtomicBoolean(false);
        final var result = ok10.toResult(() -> {
            called.set(true);
            return 250L;
        });

        assertTrue(result.isOk());
        assertEquals(Optional.of(10L), result.value());
        assertEquals(Optional.empty(), result.error());
        assertUnchangedPresent(ok10);

        assertFalse(called.get());
    }

    @Test
    void toResultWhenEmpty() {
        final var called = new AtomicBoolean(false);
        final var result = okEmpty.toResult(() -> {
            called.set(true);
            return 250L;
        });

        assertTrue(result.isError());
        assertEquals(Optional.empty(), result.value());
        assertEquals(Optional.of(250L), result.error());
        assertUnchangedEmpty(okEmpty);

        assertTrue(called.get());
    }

    @Test
    void toResultWhenErr() {
        final var called = new AtomicBoolean(false);
        final var result = error10.toResult(() -> {
            called.set(true);
            return 250L;
        });

        assertTrue(result.isError());
        assertEquals(Optional.empty(), result.value());
        assertEquals(Optional.of(10L), result.error());
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }
}
