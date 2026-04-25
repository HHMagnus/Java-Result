package dev.mhh.result;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultCombinerTest {
    @Test
    void testCombineResult2() {
        final var result = ResultCombiner.combine(
                Long::sum,
                Result.ok(123L),
                Result.ok(321L)
        );

        assertTrue(result.isOk());
        assertEquals(444L, result.optionalValue().orElseThrow());
    }

    @Test
    void testCombineResultFailedSingle() {
        final var result = ResultCombiner.combine(
                Long::sum,
                Result.ok(123L),
                Result.err("test123")
        );

        assertTrue(result.isError());
        assertEquals(List.of("test123"), result.error().orElseThrow());
    }

    @Test
    void testCombineResultFailedList() {
        final var result = ResultCombiner.combine(
                (t1, t2, t3) -> t1 + t2 + t3,
                Result.ok(123L),
                Result.<Long, String>err("test123"),
                Result.<Long, String>err("123test")
        );

        assertTrue(result.isError());
        assertEquals(List.of("test123", "123test"), result.error().orElseThrow());
    }

    @Test
    void testCombineResult3() {
        final var result = ResultCombiner.combine(
                (x, y, z) -> x + y + z,
                Result.ok(123L),
                Result.ok(321L),
                Result.ok(111L)
        );

        assertTrue(result.isOk());
        assertEquals(555L, result.optionalValue().orElseThrow());
    }

    @Test
    void testCombineResult4() {
        final var result = ResultCombiner.combine(
                (t1, t2, t3, t4) -> t1 + t2 + t3 + t4,
                Result.ok(123L),
                Result.ok(321L),
                Result.ok(111L),
                Result.ok(523)
        );

        assertTrue(result.isOk());
        assertEquals(1078L, result.optionalValue().orElseThrow());
    }

    @Test
    void testCombineResult5() {
        final var result = ResultCombiner.combine(
                (t1, t2, t3, t4, t5) -> t1 + t2 + t3 + t4 + t5,
                Result.ok(123L),
                Result.ok(321L),
                Result.ok(111L),
                Result.ok(523L),
                Result.ok(963L)
        );

        assertTrue(result.isOk());
        assertEquals(2041L, result.optionalValue().get());
    }

    @Test
    void testCombineResult6() {
        final var result = ResultCombiner.combine(
                (t1, t2, t3, t4, t5, t6) -> t1 + t2 + t3 + t4 + t5 + t6,
                Result.ok(123L),
                Result.ok(321L),
                Result.ok(111L),
                Result.ok(523L),
                Result.ok(963L),
                Result.ok(261L)
        );

        assertTrue(result.isOk());
        assertEquals(2302L, result.optionalValue().get());
    }

    @Test
    void testCombineResult7() {
        final var result = ResultCombiner.combine(
                (t1, t2, t3, t4, t5, t6, t7) -> t1 + t2 + t3 + t4 + t5 + t6 + t7,
                Result.ok(123L),
                Result.ok(321L),
                Result.ok(111L),
                Result.ok(523L),
                Result.ok(963L),
                Result.ok(261L),
                Result.ok(612L)
        );

        assertTrue(result.isOk());
        assertEquals(2914L, result.optionalValue().get());
    }

    @Test
    void testCombineResult8() {
        final var result = ResultCombiner.combine(
                (t1, t2, t3, t4, t5, t6, t7, T8) -> t1 + t2 + t3 + t4 + t5 + t6 + t7 + T8,
                Result.ok(123L),
                Result.ok(321L),
                Result.ok(111L),
                Result.ok(523L),
                Result.ok(963L),
                Result.ok(261L),
                Result.ok(612L),
                Result.ok(289L)
        );

        assertTrue(result.isOk());
        assertEquals(3203L, result.optionalValue().get());
    }

    @Test
    void combineWithOptionalToo() {
        final var result = ResultCombiner.combine(
                (t1, t2, t3, t4, t5, t6, t7, T8) -> t1 + t2.orElseThrow() + t3 + t4 + t5 + t6 + t7 + T8.orElse(289L),
                Result.ok(123L),
                OptionalResult.ok(321L),
                Result.ok(111L),
                Result.ok(523L),
                Result.ok(963L),
                Result.ok(261L),
                Result.ok(612L),
                OptionalResult.<Long, Object>empty()
        );

        assertTrue(result.isOk());
        assertEquals(3203L, result.optionalValue().get());
    }

    @Test
    void testCombineOptionalResultFailedList() {
        final var result = ResultCombiner.combine(
                (t1, t2, t3) -> t1 + t2.orElseThrow() + t3,
                Result.ok(123L),
                OptionalResult.<Long, String>err("test123"),
                Result.<Long, String>err("123test")
        );

        assertTrue(result.isError());
        assertEquals(List.of("test123", "123test"), result.error().orElseThrow());
    }
}
