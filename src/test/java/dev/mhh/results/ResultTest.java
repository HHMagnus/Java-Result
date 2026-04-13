package dev.mhh.results;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {
    Result<Long, Long> ok10 = Result.ok(10L);
    Result<Long, Long> error10 = Result.err(10L);

    @Test
    void of() {
        final var success = Result.ok(10L);

        assertTrue(success.isOk());
        assertEquals(Optional.of(10L), success.value());
        assertEquals(Optional.empty(), success.error());
    }

    @Test
    void error() {
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
    void mapWhenSuccess() {
        final var ok20 = ok10.map(x -> x * 2);

        assertTrue(ok20.isOk());
        assertEquals(Optional.of(20L), ok20.value());
        assertEquals(Optional.empty(), ok20.error());

        assertTrue(ok10.isOk());
        assertEquals(Optional.of(10L), ok10.value());
        assertEquals(Optional.empty(), ok10.error());

    }

    @Test
    void mapWhenError() {
        final var error20 = error10.map(x -> x * 2);

        assertTrue(error20.isError());
        assertEquals(Optional.of(10L), error20.error());
        assertEquals(Optional.empty(), error20.value());

        assertTrue(error10.isError());
        assertEquals(Optional.of(10L), error10.error());
        assertEquals(Optional.empty(), error10.value());
    }

    @Test
    void flatMapWhenSuccess() {
        final var ok20 = ok10.flatMap(x -> Result.ok(x * 2));

        assertTrue(ok20.isOk());
        assertEquals(Optional.of(20L), ok20.value());
        assertEquals(Optional.empty(), ok20.error());

        assertTrue(ok10.isOk());
        assertEquals(Optional.of(10L), ok10.value());
        assertEquals(Optional.empty(), ok10.error());

    }

    @Test
    void flatMapWhenError() {
        final var error20 = error10.flatMap(x -> Result.ok(x * 2));

        assertTrue(error20.isError());
        assertEquals(Optional.of(10L), error20.error());
        assertEquals(Optional.empty(), error20.value());

        assertTrue(error10.isError());
        assertEquals(Optional.of(10L), error10.error());
        assertEquals(Optional.empty(), error10.value());
    }

    @Test
    void consumeWhenSuccess() {
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
    void consumeWhenFailed() {
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
}
