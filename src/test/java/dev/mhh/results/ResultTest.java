package dev.mhh.results;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {
    Result<Long, Long> ok10 = Result.ok(10L);
    Result<Long, Long> error10 = Result.err(10L);

    @Test
    void ok() {
        final var success = Result.ok(10L);

        assertTrue(success.isOk());
        assertEquals(Optional.of(10L), success.value());
        assertEquals(Optional.empty(), success.error());
    }

    @Test
    void err() {
        final var error = Result.err(10L);

        assertTrue(error.isError());
        assertEquals(Optional.of(10L), error.error());
        assertEquals(Optional.empty(), error.value());
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
        assertEquals(Optional.of(20L), ok20.value());
        assertEquals(Optional.empty(), ok20.error());

        assertTrue(ok10.isOk());
        assertEquals(Optional.of(10L), ok10.value());
        assertEquals(Optional.empty(), ok10.error());

    }

    @Test
    void mapWhenErr() {
        final var error20 = error10.map(x -> x * 2);

        assertTrue(error20.isError());
        assertEquals(Optional.of(10L), error20.error());
        assertEquals(Optional.empty(), error20.value());

        assertTrue(error10.isError());
        assertEquals(Optional.of(10L), error10.error());
        assertEquals(Optional.empty(), error10.value());
    }

    @Test
    void flatMapWhenOk() {
        final var ok20 = ok10.flatMap(x -> Result.ok(x * 2));

        assertTrue(ok20.isOk());
        assertEquals(Optional.of(20L), ok20.value());
        assertEquals(Optional.empty(), ok20.error());

        assertTrue(ok10.isOk());
        assertEquals(Optional.of(10L), ok10.value());
        assertEquals(Optional.empty(), ok10.error());

    }

    @Test
    void flatMapWhenErr() {
        final var error20 = error10.flatMap(x -> Result.ok(x * 2));

        assertTrue(error20.isError());
        assertEquals(Optional.of(10L), error20.error());
        assertEquals(Optional.empty(), error20.value());

        assertTrue(error10.isError());
        assertEquals(Optional.of(10L), error10.error());
        assertEquals(Optional.empty(), error10.value());
    }


    @Test
    void flatMapErrWhenOk() {
        final var ok20 = ok10.flatMap(x -> Result.ok(x * 2));

        assertTrue(ok20.isOk());
        assertEquals(Optional.of(20L), ok20.value());
        assertEquals(Optional.empty(), ok20.error());

        assertTrue(ok10.isOk());
        assertEquals(Optional.of(10L), ok10.value());
        assertEquals(Optional.empty(), ok10.error());

    }

    @Test
    void flatMapErrWhenErr() {
        final var error20 = error10.flatMap(x -> Result.ok(x * 2));

        assertTrue(error20.isError());
        assertEquals(Optional.of(10L), error20.error());
        assertEquals(Optional.empty(), error20.value());

        assertTrue(error10.isError());
        assertEquals(Optional.of(10L), error10.error());
        assertEquals(Optional.empty(), error10.value());
    }

    @Test
    void consumeWhenOk() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = ok10.consume(_ -> consumed.set(true));

        assertTrue(ok10.isOk());
        assertEquals(Optional.of(10L), ok10.value());
        assertEquals(Optional.empty(), ok10.error());

        assertTrue(consumedResult.isOk());
        assertEquals(Optional.of(10L), consumedResult.value());
        assertEquals(Optional.empty(), consumedResult.error());

        assertTrue(consumed.get());
    }

    @Test
    void consumeWhenErr() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = error10.consume(_ -> consumed.set(true));

        assertTrue(error10.isError());
        assertEquals(Optional.empty(), error10.value());
        assertEquals(Optional.of(10L), error10.error());

        assertTrue(consumedResult.isError());
        assertEquals(Optional.empty(), consumedResult.value());
        assertEquals(Optional.of(10L), consumedResult.error());

        assertFalse(consumed.get());
    }

    @Test
    void flatConsumeWhenSuccess() {
        final var consumed = new AtomicBoolean(false);
        final var not20 = ok10.flatConsume(x -> {
            consumed.set(true);
            return VoidResult.ok();
        });

        assertTrue(not20.isOk());
        assertEquals(Optional.of(10L), not20.value());
        assertEquals(Optional.empty(), not20.error());

        assertTrue(ok10.isOk());
        assertEquals(Optional.of(10L), ok10.value());
        assertEquals(Optional.empty(), ok10.error());

        assertTrue(consumed.get());
    }

    @Test
    void flatConsumeWhenError() {
        final var consumed = new AtomicBoolean(false);
        final var error20 = error10.flatConsume(x -> {
            consumed.set(true);
            return VoidResult.ok();
        });

        assertTrue(error20.isError());
        assertEquals(Optional.of(10L), error20.error());
        assertEquals(Optional.empty(), error20.value());

        assertTrue(error10.isError());
        assertEquals(Optional.of(10L), error10.error());
        assertEquals(Optional.empty(), error10.value());

        assertFalse(consumed.get());
    }

    @Test
    void flatConsumeErrWhenSuccess() {
        final var consumed = new AtomicBoolean(false);
        final var not10 = ok10.flatConsume(x -> {
            consumed.set(true);
            return VoidResult.err(250L);
        });

        assertTrue(not10.isError());
        assertEquals(Optional.empty(), not10.value());
        assertEquals(Optional.of(250L), not10.error());

        assertTrue(ok10.isOk());
        assertEquals(Optional.of(10L), ok10.value());
        assertEquals(Optional.empty(), ok10.error());

        assertTrue(consumed.get());
    }

    @Test
    void flatConsumeErrWhenError() {
        final var consumed = new AtomicBoolean(false);
        final var error10Still = error10.flatConsume(x -> {
            consumed.set(true);
            return VoidResult.err(250L);
        });

        assertTrue(error10Still.isError());
        assertEquals(Optional.of(10L), error10Still.error());
        assertEquals(Optional.empty(), error10Still.value());

        assertTrue(error10.isError());
        assertEquals(Optional.of(10L), error10.error());
        assertEquals(Optional.empty(), error10.value());

        assertFalse(consumed.get());
    }
}
