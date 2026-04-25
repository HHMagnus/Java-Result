package dev.mhh.result;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultCollectorTest {
    @Test
    void emptyStream() {
        Result<List<Integer>, List<String>> result = Stream.<Result<Integer, String>>of()
                .collect(ResultCollector.collector());

        assertTrue(result.isOk());
        assertEquals(Optional.of(List.of()), result.optionalValue());
    }

    @Test
    void ok3() {
        Result<List<Integer>, List<String>> result = Stream.of(
                Result.<Integer, String>ok(1),
                Result.<Integer, String>ok(3),
                Result.<Integer, String>ok(2)
        ).collect(ResultCollector.collector());

        assertTrue(result.isOk());
        assertEquals(Optional.of(List.of(1, 3, 2)), result.optionalValue());
    }

    @Test
    void okSingle() {
        Result<List<Integer>, List<String>> result = Stream.of(
                Result.<Integer, String>ok(42)
        ).collect(ResultCollector.collector());

        assertTrue(result.isOk());
        assertEquals(Optional.of(List.of(42)), result.optionalValue());
    }

    @Test
    void err3() {
        Result<List<Integer>, List<String>> result = Stream.of(
                Result.<Integer, String>err("a"),
                Result.<Integer, String>err("c"),
                Result.<Integer, String>err("b")
        ).collect(ResultCollector.collector());

        assertTrue(result.isError());
        assertEquals(Optional.of(List.of("a", "c", "b")), result.error());
    }

    @Test
    void errSingle() {
        Result<List<Integer>, List<String>> result = Stream.of(
                Result.<Integer, String>err("oops")
        ).collect(ResultCollector.collector());

        assertTrue(result.isError());
        assertEquals(Optional.of(List.of("oops")), result.error());
    }

    @Test
    void ok2err1() {
        Result<List<Integer>, List<String>> result = Stream.of(
                Result.<Integer, String>ok(1),
                Result.<Integer, String>err("oops"),
                Result.<Integer, String>ok(3)
        ).collect(ResultCollector.collector());

        assertTrue(result.isError());
        assertEquals(Optional.of(List.of("oops")), result.error());
    }

    @Test
    void mixed_multipleErrors_allErrorsCollected() {
        Result<List<Integer>, List<String>> result = Stream.of(
                Result.<Integer, String>ok(1),
                Result.<Integer, String>err("first"),
                Result.<Integer, String>ok(2),
                Result.<Integer, String>err("second")
        ).collect(ResultCollector.collector());

        assertTrue(result.isError());
        assertEquals(Optional.of(List.of("first", "second")), result.error());
    }

    @Test
    void parallelOk4() {
        Result<List<Integer>, List<String>> result = Stream.of(
                Result.<Integer, String>ok(1),
                Result.<Integer, String>ok(2),
                Result.<Integer, String>ok(3),
                Result.<Integer, String>ok(4)
        ).parallel().collect(ResultCollector.collector());

        assertTrue(result.isOk());
        assertEquals(4, result.optionalValue().orElseThrow().size());
        assertTrue(result.optionalValue().orElseThrow().containsAll(List.of(1, 2, 3, 4)));
    }

    @Test
    void parallelErr2() {
        Result<List<Integer>, List<String>> result = Stream.of(
                Result.<Integer, String>ok(1),
                Result.<Integer, String>err("a"),
                Result.<Integer, String>ok(2),
                Result.<Integer, String>err("b")
        ).parallel().collect(ResultCollector.collector());

        assertTrue(result.isError());
        assertEquals(2, result.error().orElseThrow().size());
        assertTrue(result.error().orElseThrow().containsAll(List.of("a", "b")));
    }

    @Test
    void characteristics() {
        assertTrue(ResultCollector.collector().characteristics().isEmpty());
    }

    @Test
    void optional5() {
        Result<List<Optional<Integer>>, List<String>> result = Stream.of(
                OptionalResult.<Integer, String>ok(1),
                OptionalResult.<Integer, String>empty(),
                OptionalResult.<Integer, String>ok(3),
                OptionalResult.<Integer, String>empty(),
                OptionalResult.<Integer, String>ok(2)
        ).collect(ResultCollector.collector());

        assertTrue(result.isOk());
        assertEquals(Optional.of(List.of(Optional.of(1), Optional.empty(), Optional.of(3), Optional.empty(), Optional.of(2))), result.optionalValue());
    }

    @Test
    void optionalError() {
        Result<List<Optional<Integer>>, List<String>> result = Stream.of(
                OptionalResult.<Integer, String>ok(1),
                OptionalResult.<Integer, String>empty(),
                OptionalResult.<Integer, String>ok(3),
                OptionalResult.<Integer, String>err("oops"),
                OptionalResult.<Integer, String>ok(2)
        ).collect(ResultCollector.collector());
        assertTrue(result.isError());
        assertEquals(Optional.of(List.of("oops")), result.error());
    }
}
