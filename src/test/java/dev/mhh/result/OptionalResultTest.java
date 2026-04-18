package dev.mhh.result;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class OptionalResultTest {
    private final OptionalResult<Long, Long> ok10 = OptionalResult.ok(10L);
    private final OptionalResult<Long, Long> okEmpty = OptionalResult.empty();
    private final OptionalResult<Long, Long> error10 = OptionalResult.err(10L);

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
        assertEquals(10L, present.value());

        final var empty = new Empty<Long, Long>();
        assertUnchangedEmpty(empty);

        final var err = new OptErr<Long, Long>(10L);
        assertUnchangedErr(err);
        assertEquals(10L, err.err());

        assertThrows(NullPointerException.class, () -> new Present<>(null));
        assertThrows(NullPointerException.class, () -> new OptErr<>(null));
    }

    @Test
    void present() {
        final var present = OptionalResult.<Long, Long>ok(10L);
        assertUnchangedPresent(present);

        assertTrue(present.isPresent());
        assertFalse(present.isEmpty());
    }

    @Test
    void empty() {
        final var empty = OptionalResult.<Long, Long>empty();
        assertUnchangedEmpty(empty);

        assertFalse(empty.isPresent());
        assertTrue(empty.isEmpty());
    }

    @Test
    void err() {
        final var error = OptionalResult.<Long, Long>err(10L);
        assertUnchangedErr(error);

        assertFalse(error.isPresent());
        assertFalse(error.isEmpty());
    }

    @Test
    void okOptionalWhenPresent() {
        final var ok10 = OptionalResult.<Long, Long>okOptional(Optional.of(10L));
        assertUnchangedPresent(ok10);
    }

    @Test
    void okOptionalWhenEmpty() {
        final var okEmpty = OptionalResult.<Long, Long>okOptional(Optional.empty());
        assertUnchangedEmpty(okEmpty);
    }

    @Test
    void okNullableWhenNotNull() {
        final var ok10 = OptionalResult.<Long, Long>okNullable(10L);
        assertUnchangedPresent(ok10);
    }

    @Test
    void okNullableWhenNull() {
        final var okNull = OptionalResult.<Long, Long>okNullable(null);
        assertUnchangedEmpty(okNull);
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
        assertEquals(Optional.of(10L), result.optionalValue());
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
        assertEquals(Optional.empty(), result.optionalValue());
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
        assertEquals(Optional.empty(), result.optionalValue());
        assertEquals(Optional.of(10L), result.error());
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void mapErrorWhenPresent() {
        final var called = new AtomicBoolean(false);
        final var mappedResult = ok10.mapError(x -> {
            called.set(true);
            return x * 2;
        });

        assertUnchangedPresent(mappedResult);
        assertUnchangedPresent(ok10);

        assertFalse(called.get());
    }

    @Test
    void mapErrorWhenEmpty() {
        final var called = new AtomicBoolean(false);
        final var mappedResult = okEmpty.mapError(x -> {
            called.set(true);
            return x * 2;
        });

        assertUnchangedEmpty(mappedResult);
        assertUnchangedEmpty(okEmpty);

        assertFalse(called.get());
    }

    @Test
    void mapErrorWhenErr() {
        final var mappedResult = error10.mapError(x -> x * 2);

        assertTrue(mappedResult.isError());
        assertFalse(mappedResult.isOk());
        assertEquals(Optional.of(20L), mappedResult.error());

        assertUnchangedErr(error10);
    }

    @Test
    void mapToPresentWhenPresent() {
        final var mappedResult = ok10.map(x -> x.map(y -> y * 2));

        assertTrue(mappedResult.isOk());
        assertEquals(Optional.of(20L), mappedResult.optionalValue());
        assertUnchangedPresent(ok10);
    }

    @Test
    void mapToPresentWhenEmpty() {
        final var mappedResult = okEmpty.map(x -> Optional.of(x.map(_ -> 250L).orElse(10L)));

        assertUnchangedPresent(mappedResult);
        assertUnchangedEmpty(okEmpty);
    }

    @Test
    void mapToEmptyWhenPresent() {
        final var mappedResult = ok10.map(x -> Optional.of(10L).equals(x) ? Optional.empty() : Optional.of(250L));

        assertTrue(mappedResult.isOk());
        assertEquals(Optional.empty(), mappedResult.optionalValue());
        assertUnchangedPresent(ok10);
    }

    @Test
    void mapToEmptyWhenEmpty() {
        final var mappedResult = okEmpty.map(x -> x.map(y -> y * 2));

        assertUnchangedEmpty(mappedResult);
        assertUnchangedEmpty(okEmpty);
    }

    @Test
    void mapWhenErr() {
        final var called = new AtomicBoolean(false);
        final var mappedResult = error10.map(x -> {
            called.set(true);
            return x.map(y -> y * 2);
        });

        assertUnchangedErr(mappedResult);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void flatMapToPresentWhenPresent() {
        final var mappedResult = ok10.flatMap(x -> OptionalResult.okOptional(x.map(y -> y * 2)));

        assertTrue(mappedResult.isOk());
        assertEquals(Optional.of(20L), mappedResult.optionalValue());
        assertEquals(Optional.empty(), mappedResult.error());

        assertUnchangedPresent(ok10);
    }

    @Test
    void flatMapToEmptyWhenPresent() {
        final var mappedResult = ok10.flatMap(x -> Optional.of(10L).equals(x) ? OptionalResult.empty() : OptionalResult.ok(250L));

        assertUnchangedEmpty(mappedResult);
        assertUnchangedPresent(ok10);
    }

    @Test
    void flatMapToErrorWhenPresent() {
        final var mappedResult = ok10.flatMap(x -> OptionalResult.<Long, Long>err(x.orElse(250L)));

        assertUnchangedErr(mappedResult);
        assertUnchangedPresent(ok10);
    }

    @Test
    void flatMapToPresentWhenEmpty() {
        final var mappedResult = okEmpty.flatMap(x -> OptionalResult.ok(x.orElse(20L)));

        assertTrue(mappedResult.isOk());
        assertEquals(Optional.of(20L), mappedResult.optionalValue());
        assertEquals(Optional.empty(), mappedResult.error());

        assertUnchangedEmpty(okEmpty);
    }

    @Test
    void flatMapToEmptyWhenEmpty() {
        final var mappedResult = okEmpty.flatMap(x -> Optional.empty().equals(x) ? OptionalResult.empty() : OptionalResult.ok(250L));

        assertUnchangedEmpty(mappedResult);
        assertUnchangedEmpty(okEmpty);
    }

    @Test
    void flatMapToErrorWhenEmpty() {
        final var mappedResult = okEmpty.flatMap(x -> Optional.empty().equals(x) ? OptionalResult.err(10L) : OptionalResult.ok(250L));

        assertUnchangedErr(mappedResult);
        assertUnchangedEmpty(okEmpty);
    }

    @Test
    void flatMapToPresentWhenErr() {
        final var called = new AtomicBoolean(false);
        final var mappedResult = error10.flatMap(x -> {
            called.set(true);
            return OptionalResult.ok(x.orElse(250L) * 2);
        });

        assertUnchangedErr(mappedResult);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void flatMapToEmptyWhenErr() {
        final var called = new AtomicBoolean(false);
        final var mappedResult = error10.flatMap(_ -> {
            called.set(true);
            return OptionalResult.<Long, Long>empty();
        });

        assertUnchangedErr(mappedResult);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void flatMapToErrorWhenErr() {
        final var called = new AtomicBoolean(false);
        final var mappedResult = error10.flatMap(x -> {
            called.set(true);
            return OptionalResult.<Long, Long>err(x.orElse(250L));
        });

        assertUnchangedErr(mappedResult);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void flatMapWithResultOkWhenPresent() {
        final var result = ok10.flatMapWithResult(x -> Result.ok(x.orElse(0L)));

        assertTrue(result.isOk());
        assertEquals(Optional.of(10L), result.optionalValue());
        assertEquals(Optional.empty(), result.error());

        assertUnchangedPresent(ok10);
    }

    @Test
    void flatMapWithResultOkWhenEmpty() {
        final var result = okEmpty.flatMapWithResult(x -> Result.ok(x.orElse(250L)));

        assertTrue(result.isOk());
        assertEquals(Optional.of(250L), result.optionalValue());
        assertEquals(Optional.empty(), result.error());

        assertUnchangedEmpty(okEmpty);
    }

    @Test
    void flatMapWithResultWhenErr() {
        final var called = new AtomicBoolean(false);
        final var result = error10.flatMapWithResult(x -> {
            called.set(true);
            return Result.ok(x.orElse(250L));
        });

        assertTrue(result.isError());
        assertEquals(Optional.empty(), result.optionalValue());
        assertEquals(Optional.of(10L), result.error());

        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void flatMapWithResultErrWhenPresent() {
        final var result = ok10.flatMapWithResult(x -> Result.err(x.orElse(250L)));

        assertTrue(result.isError());
        assertEquals(Optional.empty(), result.optionalValue());
        assertEquals(Optional.of(10L), result.error());

        assertUnchangedPresent(ok10);
    }

    @Test
    void flatMapWithResultErrWhenEmpty() {
        final var result = okEmpty.flatMapWithResult(x -> Result.err(x.orElse(250L)));

        assertTrue(result.isError());
        assertEquals(Optional.empty(), result.optionalValue());
        assertEquals(Optional.of(250L), result.error());

        assertUnchangedEmpty(okEmpty);
    }

    @Test
    void consumeWhenPresent() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = ok10.consume(x -> consumed.set(Optional.of(10L).equals(x)));

        assertUnchangedPresent(consumedResult);
        assertUnchangedPresent(ok10);

        assertTrue(consumed.get());
    }

    @Test
    void consumeWhenEmpty() {
        final var consumed = new AtomicBoolean(false);

        final var consumedResult = okEmpty.consume(x -> consumed.set(Optional.empty().equals(x)));
        assertUnchangedEmpty(consumedResult);
        assertUnchangedEmpty(okEmpty);

        assertTrue(consumed.get());
    }

    @Test
    void consumeWhenErr() {
        final var called = new AtomicBoolean(false);
        final var consumedResult = error10.consume(_ -> called.set(true));

        assertUnchangedErr(consumedResult);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void verifyOkWhenPresent() {
        final var called = new AtomicBoolean(false);
        final var mapped = ok10.verify(x -> {
            called.set(Optional.of(10L).equals(x));
            return VoidResult.ok();
        });

        assertUnchangedPresent(mapped);
        assertUnchangedPresent(ok10);

        assertTrue(called.get());
    }

    @Test
    void verifyOkWhenEmpty() {
        final var called = new AtomicBoolean(false);
        final var mapped = okEmpty.verify(x -> {
            called.set(Optional.empty().equals(x));
            return VoidResult.ok();
        });

        assertUnchangedEmpty(mapped);
        assertUnchangedEmpty(okEmpty);

        assertTrue(called.get());
    }

    @Test
    void verifyErrWhenPresent() {
        final var mapped = ok10.verify(x -> VoidResult.err(x.orElse(250L)));

        assertUnchangedErr(mapped);
        assertUnchangedPresent(ok10);
    }

    @Test
    void verifyErrWhenEmpty() {
        final var mapped = okEmpty.verify(x -> VoidResult.err(x.orElse(10L)));

        assertUnchangedErr(mapped);
        assertUnchangedEmpty(okEmpty);
    }

    @Test
    void verifyWhenErr() {
        final var called = new AtomicBoolean(false);

        final var mapped = error10.verify(_ -> {
            called.set(true);
            return VoidResult.ok();
        });

        assertUnchangedErr(mapped);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void mapValueWhenPresent() {
        final var mapped = ok10.mapValue(x -> x * 2);

        assertTrue(mapped.isOk());
        assertEquals(Optional.of(20L), mapped.optionalValue());
        assertEquals(Optional.empty(), mapped.error());

        assertUnchangedPresent(ok10);
    }

    @Test
    void mapValueWhenEmpty() {
        final var called = new AtomicBoolean(false);
        final var mapped = okEmpty.mapValue(x -> {
            called.set(true);
            return x * 2;
        });

        assertUnchangedEmpty(mapped);
        assertUnchangedEmpty(okEmpty);

        assertFalse(called.get());
    }

    @Test
    void mapValueWhenErr() {
        final var called = new AtomicBoolean(false);
        final var mapped = error10.mapValue(x -> {
            called.set(true);
            return x * 2;
        });

        assertUnchangedErr(mapped);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void mapValueToOptionalPresentWhenPresent() {
        final var mapped = ok10.mapValueToOptional(x -> Optional.of(x * 2));

        assertTrue(mapped.isOk());
        assertEquals(Optional.of(20L), mapped.optionalValue());
        assertEquals(Optional.empty(), mapped.error());

        assertUnchangedPresent(ok10);
    }

    @Test
    void mapValueToOptionalEmptyWhenPresent() {
        final var mapped = ok10.mapValueToOptional(x -> x == 10 ? Optional.empty() : Optional.of(250L));

        assertTrue(mapped.isOk());
        assertEquals(Optional.empty(), mapped.optionalValue());
        assertEquals(Optional.empty(), mapped.error());

        assertUnchangedPresent(ok10);
    }

    @Test
    void mapValueToOptionalWhenEmpty() {
        final var called = new AtomicBoolean(false);
        final var mapped = okEmpty.mapValueToOptional(x -> {
            called.set(true);
            return Optional.of(x);
        });

        assertUnchangedEmpty(mapped);
        assertUnchangedEmpty(okEmpty);

        assertFalse(called.get());
    }

    @Test
    void mapValueToOptionalWhenErr() {
        final var called = new AtomicBoolean(false);
        final var mapped = error10.mapValueToOptional(x -> {
            called.set(true);
            return Optional.of(x);
        });

        assertUnchangedErr(mapped);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void flatMapValuePresentWhenPresent() {
        final var mapped = ok10.flatMapValue(x -> OptionalResult.ok(x * 2));

        assertTrue(mapped.isOk());
        assertEquals(Optional.of(20L), mapped.optionalValue());
        assertEquals(Optional.empty(), mapped.error());

        assertUnchangedPresent(ok10);
    }

    @Test
    void flatMapValueEmptyWhenPresent() {
        final var mapped = ok10.flatMapValue(x -> x == 10 ? OptionalResult.empty() : OptionalResult.ok(250L));

        assertTrue(mapped.isOk());
        assertEquals(Optional.empty(), mapped.optionalValue());
        assertEquals(Optional.empty(), mapped.error());

        assertUnchangedPresent(ok10);
    }

    @Test
    void flatMapValueErrWhenPresent() {
        final var mapped = ok10.flatMapValue(x -> x == 10 ? OptionalResult.err(10L) : OptionalResult.ok(250L));

        assertUnchangedErr(mapped);
        assertUnchangedPresent(ok10);
    }

    @Test
    void flatMapValueWhenEmpty() {
        final var called = new AtomicBoolean(false);
        final var mapped = okEmpty.flatMapValue(x -> {
            called.set(true);
            return OptionalResult.ok(x);
        });

        assertUnchangedEmpty(mapped);
        assertUnchangedEmpty(okEmpty);

        assertFalse(called.get());
    }

    @Test
    void flatMapValueWhenErr() {
        final var called = new AtomicBoolean(false);
        final var mapped = error10.flatMapValue(x -> {
            called.set(true);
            return OptionalResult.ok(x);
        });

        assertUnchangedErr(mapped);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void consumeValueWhenPresent() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = ok10.consumeValue(x -> consumed.set(10L == x));

        assertUnchangedPresent(consumedResult);
        assertUnchangedPresent(ok10);

        assertTrue(consumed.get());
    }

    @Test
    void consumeValueWhenEmpty() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = okEmpty.consumeValue(_ -> consumed.set(false));

        assertUnchangedEmpty(consumedResult);
        assertUnchangedEmpty(okEmpty);

        assertFalse(consumed.get());
    }

    @Test
    void consumeValueWhenErr() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = error10.consumeValue(_ -> consumed.set(false));

        assertUnchangedErr(consumedResult);
        assertUnchangedErr(error10);

        assertFalse(consumed.get());
    }

    @Test
    void verifyValueOkWhenPresent() {
        final var called = new AtomicBoolean(false);
        final var mapped = ok10.verifyValue(x -> {
            called.set(10L == x);
            return VoidResult.ok();
        });

        assertUnchangedPresent(mapped);
        assertUnchangedPresent(ok10);

        assertTrue(called.get());
    }

    @Test
    void verifyValueErrWhenPresent() {
        final var called = new AtomicBoolean(false);
        final var mapped = ok10.verifyValue(x -> {
            called.set(10L == x);
            return VoidResult.err(10L);
        });

        assertUnchangedErr(mapped);
        assertUnchangedPresent(ok10);

        assertTrue(called.get());
    }

    @Test
    void verifyValueWhenEmpty() {
        final var called = new AtomicBoolean(false);
        final var consumedResult = okEmpty.verifyValue(x -> {
            called.set(true);
            return VoidResult.err(x);
        });

        assertUnchangedEmpty(consumedResult);
        assertUnchangedEmpty(okEmpty);

        assertFalse(called.get());
    }

    @Test
    void verifyValueWhenErr() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = error10.verifyValue(_ -> {
            consumed.set(false);
            return VoidResult.ok();
        });

        assertUnchangedErr(consumedResult);
        assertUnchangedErr(error10);

        assertFalse(consumed.get());
    }

    @Test
    void filterTrueWhenPresent() {
        final var mapped = ok10.filter(x -> x == 10);

        assertUnchangedPresent(mapped);
        assertUnchangedPresent(ok10);
    }

    @Test
    void filterFalseWhenPresent() {
        final var mapped = ok10.filter(x -> x != 10);

        assertUnchangedEmpty(mapped);
        assertUnchangedPresent(ok10);
    }

    @Test
    void filterWhenEmpty() {
        final var called = new AtomicBoolean(false);
        final var mapped = okEmpty.filter(_ -> {
            called.set(true);
            return false;
        });

        assertUnchangedEmpty(mapped);
        assertUnchangedEmpty(okEmpty);

        assertFalse(called.get());
    }

    @Test
    void filterWhenErr() {
        final var called = new AtomicBoolean(false);
        final var mapped = error10.filter(_ -> {
            called.set(true);
            return false;
        });

        assertUnchangedErr(mapped);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void runIfPresentWhenPresent() {
        final var called = new AtomicBoolean(false);
        final var ran = ok10.runIfPresent(() -> called.set(true));

        assertUnchangedPresent(ran);
        assertUnchangedPresent(ok10);

        assertTrue(called.get());
    }

    @Test
    void runIfPresentWhenEmpty() {
        final var called = new AtomicBoolean(false);
        final var ran = okEmpty.runIfPresent(() -> called.set(true));

        assertUnchangedEmpty(ran);
        assertUnchangedEmpty(okEmpty);

        assertFalse(called.get());
    }

    @Test
    void runIfPresentWhenErr() {
        final var called = new AtomicBoolean(false);
        final var ran = error10.runIfPresent(() -> called.set(true));

        assertUnchangedErr(ran);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void runIfEmptyWhenPresent() {
        final var called = new AtomicBoolean(false);
        final var ran = ok10.runIfEmpty(() -> called.set(true));

        assertUnchangedPresent(ran);
        assertUnchangedPresent(ok10);

        assertFalse(called.get());
    }

    @Test
    void runIfEmptyWhenEmpty() {
        final var called = new AtomicBoolean(false);
        final var ran = okEmpty.runIfEmpty(() -> called.set(true));

        assertUnchangedEmpty(ran);
        assertUnchangedEmpty(okEmpty);

        assertTrue(called.get());
    }

    @Test
    void runIfEmptyWhenErr() {
        final var called = new AtomicBoolean(false);
        final var ran = error10.runIfEmpty(() -> called.set(true));

        assertUnchangedErr(ran);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void orElseThrowWhenPresent() {
        final var called = new AtomicBoolean(false);
        final var result = ok10.orElseThrow(_ -> {
            called.set(true);
            return new RuntimeException("Error");
        });

        assertEquals(Optional.of(10L), result);
        assertUnchangedPresent(ok10);

        assertFalse(called.get());
    }

    @Test
    void orElseThrowWhenEmpty() {
        final var called = new AtomicBoolean(false);
        final var result = okEmpty.orElseThrow(_ -> {
            called.set(true);
            return new RuntimeException("Error");
        });

        assertEquals(Optional.empty(), result);
        assertUnchangedEmpty(okEmpty);

        assertFalse(called.get());
    }

    @Test
    void orElseThrowWhenErr() {
        final var thrown = assertThrows(RuntimeException.class, () -> error10.orElseThrow(x -> new RuntimeException("Error: " + x)));

        assertEquals("Error: 10", thrown.getMessage());
        assertUnchangedErr(error10);
    }

    @Test
    void orElseThrowIfEmptyWhenPresent() {
        final var ifEmptyCalled = new AtomicBoolean(false);
        final var exceptionSupplierCalled = new AtomicBoolean(false);
        final var result = ok10.orElseThrow(() -> {
            ifEmptyCalled.set(true);
            return 250L;
        }, x -> {
            exceptionSupplierCalled.set(true);
            return new RuntimeException("Error: " + x);
        });

        assertEquals(10L, result);
        assertUnchangedPresent(ok10);

        assertFalse(ifEmptyCalled.get());
        assertFalse(exceptionSupplierCalled.get());
    }

    @Test
    void orElseThrowIfEmptyWhenEmpty() {
        final var ifEmptyCalled = new AtomicBoolean(false);
        final var exceptionSupplierCalled = new AtomicBoolean(false);
        final var result = okEmpty.orElseThrow(() -> {
            ifEmptyCalled.set(true);
            return 250L;
        }, x -> {
            exceptionSupplierCalled.set(true);
            return new RuntimeException("Error: " + x);
        });

        assertEquals(250L, result);
        assertUnchangedEmpty(okEmpty);

        assertTrue(ifEmptyCalled.get());
        assertFalse(exceptionSupplierCalled.get());
    }

    @Test
    void orElseThrowIfEmptyWhenErr() {
        final var ifEmptyCalled = new AtomicBoolean(false);
        final var exceptionSupplierCalled = new AtomicBoolean(false);
        final var thrown = assertThrows(
                RuntimeException.class,
                () -> error10.orElseThrow(() -> {
                    ifEmptyCalled.set(true);
                    return 250L;
                }, x -> {
                    exceptionSupplierCalled.set(true);
                    return new RuntimeException("Error: " + x);
                })
        );

        assertEquals("Error: 10", thrown.getMessage());
        assertUnchangedErr(error10);

        assertFalse(ifEmptyCalled.get());
        assertTrue(exceptionSupplierCalled.get());
    }

    @Test
    void orWhenPresent() {
        final var called = new AtomicBoolean(false);
        final var result = ok10.or(() -> {
            called.set(true);
            return Optional.of(250L);
        });

        assertUnchangedPresent(ok10);
        assertUnchangedPresent(result);

        assertFalse(called.get());
    }

    @Test
    void orWhenEmpty() {
        final var called = new AtomicBoolean(false);
        final var result = okEmpty.or(() -> {
            called.set(true);
            return Optional.of(10L);
        });

        assertUnchangedPresent(result);
        assertUnchangedEmpty(okEmpty);

        assertTrue(called.get());
    }

    @Test
    void orWhenErr() {
        final var called = new AtomicBoolean(false);
        final var result = error10.or(() -> {
            called.set(true);
            return Optional.of(250L);
        });

        assertUnchangedErr(result);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void verifyPredicateTrueWhenPresent() {
        final var result = ok10.verify(x -> Optional.of(10L).equals(x), 250L);

        assertUnchangedPresent(result);
        assertUnchangedPresent(ok10);
    }

    @Test
    void verifyPredicateFalseWhenPresent() {
        final var result = ok10.verify(x -> Optional.of(250L).equals(x), 10L);

        assertUnchangedErr(result);
        assertUnchangedPresent(ok10);
    }

    @Test
    void verifyPredicateTrueWhenEmpty() {
        final var result = okEmpty.verify(x -> Optional.empty().equals(x), 250L);

        assertUnchangedEmpty(result);
        assertUnchangedEmpty(okEmpty);
    }

    @Test
    void verifyPredicateFalseWhenEmpty() {
        final var result = okEmpty.verify(x -> Optional.of(250L).equals(x), 10L);

        assertUnchangedErr(result);
        assertUnchangedEmpty(okEmpty);
    }

    @Test
    void verifyPredicateWhenErr() {
        final var called = new AtomicBoolean(false);
        final var result = error10.verify(x -> {
            called.set(true);
            return Optional.of(10L).equals(x);
        }, 250L);

        assertUnchangedErr(result);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void verifyPredicateTrueErrSupplierWhenPresent() {
        final var errSupplierCalled = new AtomicBoolean(false);
        final var result = ok10.verify(x -> Optional.of(10L).equals(x), () -> {
            errSupplierCalled.set(true);
            return 250L;
        });

        assertUnchangedPresent(result);
        assertUnchangedPresent(ok10);

        assertFalse(errSupplierCalled.get());
    }

    @Test
    void verifyPredicateFalseErrSupplierWhenPresent() {
        final var errSupplierCalled = new AtomicBoolean(false);
        final var result = ok10.verify(x -> Optional.of(250L).equals(x), () -> {
            errSupplierCalled.set(true);
            return 10L;
        });

        assertUnchangedErr(result);
        assertUnchangedPresent(ok10);

        assertTrue(errSupplierCalled.get());
    }

    @Test
    void verifyPredicateTrueErrSupplierWhenEmpty() {
        final var errSupplierCalled = new AtomicBoolean(false);
        final var result = okEmpty.verify(x -> Optional.empty().equals(x), () -> {
            errSupplierCalled.set(true);
            return 250L;
        });

        assertUnchangedEmpty(result);
        assertUnchangedEmpty(okEmpty);

        assertFalse(errSupplierCalled.get());
    }

    @Test
    void verifyPredicateFalseErrSupplierWhenEmpty() {
        final var errSupplierCalled = new AtomicBoolean(false);
        final var result = okEmpty.verify(x -> Optional.of(250L).equals(x), () -> {
            errSupplierCalled.set(true);
            return 10L;
        });

        assertUnchangedErr(result);
        assertUnchangedEmpty(okEmpty);

        assertTrue(errSupplierCalled.get());
    }

    @Test
    void verifyPredicateErrSupplierWhenErr() {
        final var predicateCalled = new AtomicBoolean(false);
        final var errSupplierCalled = new AtomicBoolean(false);
        final var result = error10.verify(x -> {
            predicateCalled.set(true);
            return Optional.of(250L).equals(x);
        }, () -> {
            errSupplierCalled.set(true);
            return 10L;
        });

        assertUnchangedErr(result);
        assertUnchangedErr(error10);

        assertFalse(predicateCalled.get());
        assertFalse(errSupplierCalled.get());
    }
}
