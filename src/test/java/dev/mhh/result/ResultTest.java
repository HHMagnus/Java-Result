package dev.mhh.result;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {
    private final Result<Long, Long> ok10 = Result.ok(10L);
    private final Result<Long, Long> error10 = Result.err(10L);

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
        final var called = new AtomicBoolean(false);
        final var error20 = error10.map(x -> {
            called.set(true);
            return x * 2;
        });

        assertTrue(error20.isError());
        assertEquals(Optional.of(10L), error20.error());
        assertEquals(Optional.empty(), error20.optionalValue());

        assertUnchangedErr(error10);

        assertFalse(called.get());
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
        final var called = new AtomicBoolean(false);
        final var error20 = error10.flatMap(x -> {
            called.set(true);
            return Result.ok(x * 2);
        });

        assertUnchangedErr(error20);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }


    @Test
    void flatMapErrWhenOk() {
        final var ok20 = ok10.flatMap(x -> Result.err(x * 2));

        assertTrue(ok20.isError());
        assertEquals(Optional.empty(), ok20.optionalValue());
        assertEquals(Optional.of(20L), ok20.error());

        assertUnchangedOk(ok10);
    }

    @Test
    void flatMapErrWhenErr() {
        final var called = new AtomicBoolean(false);
        final var error20 = error10.flatMap(x -> {
            called.set(true);
            return Result.<Long, Long>err(x * 250);
        });

        assertUnchangedErr(error20);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void consumeWhenOk() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = ok10.consume(_x ->consumed.set(true));

        assertUnchangedOk(consumedResult);
        assertUnchangedOk(ok10);

        assertTrue(consumed.get());
    }

    @Test
    void consumeWhenErr() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = error10.consume(_x ->consumed.set(true));

        assertUnchangedErr(consumedResult);
        assertUnchangedErr(error10);

        assertFalse(consumed.get());
    }

    @Test
    void verifyWhenSuccess() {
        final var consumed = new AtomicBoolean(false);
        final var not20 = ok10.verify(x -> {
            consumed.set(x == 10);
            return VoidResult.ok();
        });

        assertUnchangedOk(not20);
        assertUnchangedOk(ok10);

        assertTrue(consumed.get());
    }

    @Test
    void verifyWhenError() {
        final var consumed = new AtomicBoolean(false);
        final var error20 = error10.verify(_x ->{
            consumed.set(true);
            return VoidResult.ok();
        });

        assertUnchangedErr(error20);
        assertUnchangedErr(error10);

        assertFalse(consumed.get());
    }

    @Test
    void verifyErrWhenSuccess() {
        final var consumed = new AtomicBoolean(false);
        final var err250 = ok10.verify(x -> {
            consumed.set(x == 10);
            return VoidResult.err(250L);
        });

        assertTrue(err250.isError());
        assertEquals(Optional.of(250L), err250.error());
        assertEquals(Optional.empty(), err250.optionalValue());

        assertUnchangedOk(ok10);

        assertTrue(consumed.get());
    }

    @Test
    void verifyErrWhenError() {
        final var consumed = new AtomicBoolean(false);
        final var error10Still = error10.verify(_x ->{
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
        final var consumedResult = ok10.consumeError(_x ->consumed.set(true));

        assertUnchangedOk(consumedResult);
        assertUnchangedOk(ok10);

        assertFalse(consumed.get());
    }

    @Test
    void consumeErrorWhenErr() {
        final var consumed = new AtomicBoolean(false);
        final var consumedResult = error10.consumeError(x -> consumed.set(x == 10));

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
        final var called = new AtomicBoolean(false);
        final var mappedResult = ok10.mapError(e -> {
            called.set(true);
            return e + 1;
        });

        assertUnchangedOk(mappedResult);
        assertUnchangedOk(ok10);

        assertFalse(called.get());
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

    @Test
    void mapToOptionalEmptyWhenOk() {
        final var mappedResult = ok10.mapToOptional(x -> 10L == x ? Optional.empty() : Optional.of(250L));

        assertTrue(mappedResult.isOk());
        assertEquals(Optional.empty(), mappedResult.optionalValue());
        assertUnchangedOk(ok10);
    }

    @Test
    void mapToOptionalEmptyWhenErr() {
        final var called = new AtomicBoolean(false);
        final var mappedResult = error10.mapToOptional(_x ->{
            called.set(true);
            return Optional.empty();
        });

        assertTrue(mappedResult.isError());
        assertFalse(mappedResult.isOk());
        assertEquals(Optional.of(10L), mappedResult.error());
        assertEquals(Optional.empty(), mappedResult.optionalValue());

        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void mapToOptionalPresentWhenOk() {
        final var mappedResult = ok10.mapToOptional(x -> Optional.of(x * 2));

        assertTrue(mappedResult.isOk());
        assertEquals(Optional.of(20L), mappedResult.optionalValue());
        assertEquals(Optional.empty(), mappedResult.error());

        assertUnchangedOk(ok10);
    }

    @Test
    void mapToOptionalPresentWhenErr() {
        final var called = new AtomicBoolean(false);
        final var mappedResult = error10.mapToOptional(x -> {
            called.set(true);
            return Optional.of(x * 2);
        });

        assertTrue(mappedResult.isError());
        assertEquals(Optional.of(10L), mappedResult.error());
        assertEquals(Optional.empty(), mappedResult.optionalValue());

        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void mapToOptionalResultPresentWhenOk() {
        final var mappedResult = ok10.flatMapWithOptionalResult(x -> OptionalResult.ok(x * 2));

        assertTrue(mappedResult.isOk());
        assertEquals(Optional.of(20L), mappedResult.optionalValue());
        assertEquals(Optional.empty(), mappedResult.error());

        assertUnchangedOk(ok10);
    }

    @Test
    void mapToOptionalResultEmptyWhenOk() {
        final var mappedResult = ok10.flatMapWithOptionalResult(x -> x == 10L ? OptionalResult.empty() : OptionalResult.ok(250L));

        assertTrue(mappedResult.isOk());
        assertEquals(Optional.empty(), mappedResult.optionalValue());
        assertEquals(Optional.empty(), mappedResult.error());

        assertUnchangedOk(ok10);
    }

    @Test
    void mapToOptionalResultErrWhenOk() {
        final var mappedResult = ok10.flatMapWithOptionalResult(x -> x == 10L ? OptionalResult.err(250L) : OptionalResult.empty());

        assertFalse(mappedResult.isOk());
        assertTrue(mappedResult.isError());
        assertEquals(Optional.empty(), mappedResult.optionalValue());
        assertEquals(Optional.of(250L), mappedResult.error());

        assertUnchangedOk(ok10);
    }

    @Test
    void mapToOptionalResultWhenErr() {
        final var called = new AtomicBoolean(false);
        final var mappedResult = error10.flatMapWithOptionalResult(x -> {
            called.set(true);
            return OptionalResult.ok(x * 2);
        });

        assertTrue(mappedResult.isError());
        assertEquals(Optional.of(10L), mappedResult.error());
        assertEquals(Optional.empty(), mappedResult.optionalValue());

        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void filterTrueWhenOk() {
        final var mapped = ok10.filter(x -> x == 10);

        assertTrue(mapped.isOk());
        assertEquals(Optional.of(10L), mapped.optionalValue());
        assertEquals(Optional.empty(), mapped.error());

        assertUnchangedOk(ok10);
    }

    @Test
    void filterFalseWhenOk() {
        final var mapped = ok10.filter(x -> x != 10);

        assertTrue(mapped.isOk());
        assertEquals(Optional.empty(), mapped.optionalValue());
        assertEquals(Optional.empty(), mapped.error());

        assertUnchangedOk(ok10);
    }

    @Test
    void filterWhenErr() {
        final var called = new AtomicBoolean(false);
        final var mapped = error10.filter(x -> {
            called.set(true);
            return x == 10;
        });

        assertTrue(mapped.isError());
        assertEquals(Optional.of(10L), mapped.error());
        assertEquals(Optional.empty(), mapped.optionalValue());

        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void orElseThrowOk() {
        final var called = new AtomicBoolean(false);
        final var mapped = ok10.orElseThrow(e -> {
            called.set(true);
            return new RuntimeException("Error");
        });

        assertEquals(10L, mapped);
        assertUnchangedOk(ok10);

        assertFalse(called.get());
    }

    @Test
    void orElseThrowErr() {
        final var thrown = assertThrows(RuntimeException.class, () -> error10.orElseThrow(e -> new RuntimeException("Error: " + e)));

        assertEquals("Error: 10", thrown.getMessage());
        assertUnchangedErr(error10);
    }

    @Test
    void verifyPredicateTrueWhenOk() {
        final var result = ok10.verify(x -> x == 10, 250L);

        assertUnchangedOk(result);
        assertUnchangedOk(ok10);
    }

    @Test
    void verifyPredicateFalseWhenOk() {
        final var result = ok10.verify(x -> x != 10, 10L);

        assertUnchangedErr(result);
        assertUnchangedOk(ok10);
    }

    @Test
    void verifyPredicateWhenErr() {
        final var called = new AtomicBoolean(false);
        final var result = error10.verify(x -> {
            called.set(true);
            return x == 10;
        }, 250L);

        assertUnchangedErr(result);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void verifyPredicateErrSupplierTrueWhenOk() {
        final var called = new AtomicBoolean(false);
        final var result = ok10.verify(x -> x == 10, () -> {
            called.set(true);
            return 250L;
        });

        assertUnchangedOk(result);
        assertUnchangedOk(ok10);

        assertFalse(called.get());
    }

    @Test
    void verifyPredicateErrSupplierFalseWhenOk() {
        final var called = new AtomicBoolean(false);
        final var result = ok10.verify(x -> x != 10, () -> {
            called.set(true);
            return 10L;
        });

        assertUnchangedErr(result);
        assertUnchangedOk(ok10);

        assertTrue(called.get());
    }

    @Test
    void verifyPredicateErrSupplierWhenErr() {
        final var predicateCalled = new AtomicBoolean(false);
        final var errSupplierCalled = new AtomicBoolean(false);
        final var result = error10.verify(x -> {
            predicateCalled.set(true);
            return x == 10;
        }, () -> {
            errSupplierCalled.set(true);
            return 250L;
        });

        assertUnchangedErr(result);
        assertUnchangedErr(error10);

        assertFalse(predicateCalled.get());
        assertFalse(errSupplierCalled.get());
    }

}
