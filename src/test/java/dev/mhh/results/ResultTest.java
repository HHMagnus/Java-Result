package dev.mhh.results;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {
    Result<Long, Long> success10 = Result.of(10L);
    Result<Long, Long> error10 = Result.error(10L);

    @Test
    void of() {
        final var success = Result.of(10L);

        assertTrue(success.isSuccess());
        assertEquals(Optional.of(10L), success.value());
        assertEquals(Optional.empty(), success.error());
    }

    @Test
    void error() {
        final var error = Result.error(10L);

        assertTrue(error.isError());
        assertEquals(Optional.of(10L), error.error());
        assertEquals(Optional.empty(), error.value());
    }

    @Test
    void toStringTest() {
        assertEquals("Result success (10)", success10.toString());
        assertEquals("Result error (10)", error10.toString());
    }

    @Test
    void mapWhenSuccess() {
        final var success20 = success10.map(x -> x * 2);

        assertTrue(success20.isSuccess());
        assertEquals(Optional.of(20L), success20.value());
        assertEquals(Optional.empty(), success20.error());

        assertTrue(success10.isSuccess());
        assertEquals(Optional.of(10L), success10.value());
        assertEquals(Optional.empty(), success10.error());

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
        final var success20 = success10.flatMap(x -> Result.of(x * 2));

        assertTrue(success20.isSuccess());
        assertEquals(Optional.of(20L), success20.value());
        assertEquals(Optional.empty(), success20.error());

        assertTrue(success10.isSuccess());
        assertEquals(Optional.of(10L), success10.value());
        assertEquals(Optional.empty(), success10.error());

    }

    @Test
    void flatMapWhenError() {
        final var error20 = error10.flatMap(x -> Result.of(x * 2));

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
        final var consumedResult = success10.consume(_ -> consumed.set(true));

        assertTrue(success10.isSuccess());
        assertEquals(Optional.of(10L), success10.value());
        assertEquals(Optional.empty(), success10.error());

        assertTrue(consumedResult.isSuccess());
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
