package dev.mhh.result;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {
    Result<Long, Long> ok10 = Result.ok(10L);
    Result<Long, Long> error10 = Result.err(10L);

    private void assertUnchangedOk(Result<Long, Long> ok10) {
        assertTrue(ok10.isOk());
        assertFalse(ok10.isError());
        assertEquals(Optional.of(10L), ok10.optionalValue());
        assertEquals(Optional.empty(), ok10.error());
    }

    private void assertUnchangedErr(Result<Long, Long> error10) {
        assertTrue(error10.isError());
        assertFalse(error10.isOk());
        assertEquals(Optional.of(10L), error10.error());
        assertEquals(Optional.empty(), error10.optionalValue());
    }

    @Test
    void manualCreation() {
        final var ok = new Ok<Long, Long>(10L);
        assertUnchangedOk(ok);
        assertEquals(10L, ok.value());

        final var err = new Err<Long, Long>(10L);
        assertUnchangedErr(err);
        assertEquals(10L, err.err());

        assertThrows(NullPointerException.class, () -> new Ok<>(null));
        assertThrows(NullPointerException.class, () -> new Err<>(null));
    }

    @Test
    void ok() {
        final var success = Result.ok(10L);

        assertTrue(success.isOk());
        assertEquals(Optional.of(10L), success.optionalValue());
        assertEquals(Optional.empty(), success.error());
    }

    @Test
    void err() {
        final var error = Result.err(10L);

        assertTrue(error.isError());
        assertEquals(Optional.of(10L), error.error());
        assertEquals(Optional.empty(), error.optionalValue());
    }

    @Test
    void toStringTest() {
        assertEquals("Ok[10]", ok10.toString());
        assertEquals("Err[10]", error10.toString());
    }

    @Test
    void mapWhenOk() {
        final var ok20 = ok10.map(x -> x * 2);

        assertTrue(ok20.isOk());
        assertEquals(Optional.of(20L), ok20.optionalValue());
        assertEquals(Optional.empty(), ok20.error());

        assertUnchangedOk(ok10);
    }

    @Test
    void mapWhenErr() {
        final var error20 = error10.map(x -> x * 2);

        assertTrue(error20.isError());
        assertEquals(Optional.of(10L), error20.error());
        assertEquals(Optional.empty(), error20.optionalValue());

        assertUnchangedErr(error10);
    }

    @Test
    void flatMapWhenOk() {
        final var ok20 = ok10.flatMap(x -> Result.ok(x * 2));

        assertTrue(ok20.isOk());
        assertEquals(Optional.of(20L), ok20.optionalValue());
        assertEquals(Optional.empty(), ok20.error());

        assertUnchangedOk(ok10);
    }

    @Test
    void flatMapWhenErr() {
        final var error20 = error10.flatMap(x -> Result.ok(x * 2));

        assertUnchangedErr(error20);
        assertUnchangedErr(error10);
    }


    @Test
    void flatMapErrWhenOk() {
        final var ok20 = ok10.flatMap(x -> Result.err(20L));

        assertTrue(ok20.isError());
        assertEquals(Optional.empty(), ok20.optionalValue());
        assertEquals(Optional.of(20L), ok20.error());

        assertUnchangedOk(ok10);
    }

    @Test
    void flatMapErrWhenErr() {
        final var error20 = error10.flatMap(x -> Result.<Long, Long>err(x * 250));

        assertUnchangedErr(error20);
        assertUnchangedErr(error10);
    }

    @Test
    void consumeWhenOk() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = ok10.consume(_ -> consumed.set(true));

        assertUnchangedOk(consumedResult);
        assertUnchangedOk(ok10);

        assertTrue(consumed.get());
    }

    @Test
    void consumeWhenErr() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = error10.consume(_ -> consumed.set(true));

        assertUnchangedErr(consumedResult);
        assertUnchangedErr(error10);

        assertFalse(consumed.get());
    }

    @Test
    void flatConsumeWhenSuccess() {
        final var consumed = new AtomicBoolean(false);
        final var not20 = ok10.flatConsume(x -> {
            consumed.set(true);
            return VoidResult.ok();
        });

        assertUnchangedOk(not20);
        assertUnchangedOk(ok10);

        assertTrue(consumed.get());
    }

    @Test
    void flatConsumeWhenError() {
        final var consumed = new AtomicBoolean(false);
        final var error20 = error10.flatConsume(x -> {
            consumed.set(true);
            return VoidResult.ok();
        });

        assertUnchangedErr(error20);
        assertUnchangedErr(error10);

        assertFalse(consumed.get());
    }

    @Test
    void flatConsumeErrWhenSuccess() {
        final var consumed = new AtomicBoolean(false);
        final var err250 = ok10.flatConsume(x -> {
            consumed.set(true);
            return VoidResult.err(250L);
        });

        assertTrue(err250.isError());
        assertEquals(Optional.of(250L), err250.error());
        assertEquals(Optional.empty(), err250.optionalValue());

        assertUnchangedOk(ok10);

        assertTrue(consumed.get());
    }

    @Test
    void flatConsumeErrWhenError() {
        final var consumed = new AtomicBoolean(false);
        final var error10Still = error10.flatConsume(x -> {
            consumed.set(true);
            return VoidResult.err(250L);
        });

        assertUnchangedErr(error10Still);
        assertUnchangedErr(error10);

        assertFalse(consumed.get());
    }

    @Test
    void runIfOkWhenOk() {
        final var consumed = new AtomicBoolean(false);

        final var okRan = ok10.runIfOk(() -> consumed.set(true));

        assertUnchangedOk(okRan);
        assertUnchangedOk(ok10);

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

        final var ok10Ran = ok10.runIfError(() -> consumed.set(true));

        assertUnchangedOk(ok10Ran);
        assertUnchangedOk(ok10);

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
    void toVoidResultWhenOk() {
        final var voidResult = ok10.toVoidResult();

        assertTrue(voidResult.isOk());

        assertUnchangedOk(ok10);
    }

    @Test
    void toVoidResultWhenErr() {
        final var voidResult = error10.toVoidResult();

        assertTrue(voidResult.isError());
        assertFalse(voidResult.isOk());
        assertEquals(Optional.of(10L), voidResult.error());

        assertUnchangedErr(error10);
    }

    @Test
    void consumeErrorWhenOk() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = ok10.consumeError(_ -> consumed.set(true));

        assertUnchangedOk(consumedResult);
        assertUnchangedOk(ok10);

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
    void toOptionalResultWhenOk() {
        final var optionalResult = ok10.toOptionalResult();

        assertTrue(optionalResult.isOk());
        assertEquals(Optional.of(10L), optionalResult.optionalValue());
        assertUnchangedOk(ok10);
    }

    @Test
    void toOptionalResultWhenErr() {
        final var optionalResult = error10.toOptionalResult();

        assertTrue(optionalResult.isError());
        assertEquals(Optional.empty(), optionalResult.optionalValue());
        assertEquals(Optional.of(10L), optionalResult.error());
        assertUnchangedErr(error10);
    }

    @Test
    void mapErrorWhenOk() {
        final var mappedResult = ok10.mapError(e -> e + 1);

        assertUnchangedOk(mappedResult);
        assertUnchangedOk(ok10);
    }

    @Test
    void mapErrorWhenErr() {
        final var mappedResult = error10.mapError(e -> e + 1);

        assertTrue(mappedResult.isError());
        assertFalse(mappedResult.isOk());
        assertEquals(Optional.of(11L), mappedResult.error());
        assertEquals(Optional.empty(), mappedResult.optionalValue());

        assertUnchangedErr(error10);
    }
}
