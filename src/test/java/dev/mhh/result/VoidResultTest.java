package dev.mhh.result;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class VoidResultTest {
    private final VoidResult<Long> ok = VoidResult.ok();
    private final VoidResult<Long> error10 = VoidResult.err(10L);

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
        assertEquals(10L, err.err());

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
        assertEquals(Optional.of(10L), replaced.optionalValue());
        assertEquals(Optional.empty(), replaced.error());
    }

   @Test
   void toResultWhenErr() {
        final var replaced = error10.toResult(10L);

       assertUnchangedErr(error10);

        assertTrue(replaced.isError());
        assertEquals(Optional.empty(), replaced.optionalValue());
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
        final var consumedResult = ok.consumeError(_x ->consumed.set(true));

        assertUnchangedOk(consumedResult);
        assertUnchangedOk(ok);

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
    void toOptionalResultValueWhenOk() {
        final var optionalResult = ok.toOptionalResult(10L);

        assertUnchangedOk(ok);
        assertTrue(optionalResult.isOk());
        assertEquals(Optional.of(10L), optionalResult.optionalValue());
    }

    @Test
    void toOptionalResultValueWhenErr() {
        final var optionalResult = error10.toOptionalResult(250L);

        assertUnchangedErr(error10);
        assertTrue(optionalResult.isError());
        assertEquals(Optional.of(10L), optionalResult.error());
    }

    @Test
    void toOptionalResultEmptyWhenOk() {
        final var optionalResult = ok.toOptionalResult();

        assertUnchangedOk(ok);
        assertTrue(optionalResult.isOk());
        assertEquals(Optional.empty(), optionalResult.optionalValue());
    }

    @Test
    void toOptionalResultEmptyWhenErr() {
        final var optionalResult = error10.toOptionalResult();

        assertUnchangedErr(error10);
        assertTrue(optionalResult.isError());
        assertEquals(Optional.of(10L), optionalResult.error());
    }

    @Test
    void mapErrorWhenOk() {
        final var mappedResult = ok.mapError(x -> x * 2);

        assertUnchangedOk(mappedResult);
        assertUnchangedOk(ok);
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
    void verifySupplierOkWhenOk() {
        final var verified = ok.verify(VoidResult::ok);

        assertUnchangedOk(verified);
        assertUnchangedOk(ok);
    }

    @Test
    void verifySupplierOkWhenErr() {
        final var consumed = new AtomicBoolean(false);
        final var verified = error10.verify(() -> {
            consumed.set(true);
            return VoidResult.ok();
        });

        assertUnchangedErr(verified);
        assertUnchangedErr(error10);

        assertFalse(consumed.get());
    }

    @Test
    void verifySupplierErrWhenOk() {
        final var verified = ok.verify(() -> VoidResult.err(10L));

        assertUnchangedErr(verified);
        assertUnchangedOk(ok);
    }

    @Test
    void verifySupplierErrWhenErr() {
        final var called = new AtomicBoolean(false);
        final var verified = error10.verify(() -> {
            called.set(true);
            return VoidResult.err(250L);
        });

        assertUnchangedErr(verified);
        assertUnchangedErr(error10);

        assertFalse(called.get());
    }

    @Test
    void verifyOkWhenOk() {
        final var verified = ok.verify(VoidResult.ok());

        assertUnchangedOk(verified);
        assertUnchangedOk(ok);
    }

    @Test
    void verifyOkWhenErr() {
        final var verified = error10.verify(VoidResult.ok());

        assertUnchangedErr(verified);
        assertUnchangedErr(error10);
    }

    @Test
    void verifyErrWhenOk() {
        final var verified = ok.verify(error10);

        assertUnchangedErr(verified);
        assertUnchangedOk(ok);
    }

    @Test
    void verifyErrWhenErr() {
        final var verified = error10.verify(VoidResult.err(250L));

        assertUnchangedErr(verified);
        assertUnchangedErr(error10);
    }

    @Test
    void orElseThrowWhenOk() {
        final var called = new AtomicBoolean(false);
        ok.orElseThrow(_x ->{
            called.set(true);
            return new RuntimeException("Error");
        });

        assertUnchangedOk(ok);

        assertFalse(called.get());
    }

    @Test
    void orElseThrowWhenErr() {
        final var thrown = assertThrows(RuntimeException.class, () -> error10.orElseThrow(x-> new RuntimeException("Error: " + x)));

        assertEquals("Error: 10", thrown.getMessage());
        assertUnchangedErr(error10);
    }

    @Test
    void okIfWhenTrue() {
        final var ok = VoidResult.okIf(true, 250L);
        assertUnchangedOk(ok);
    }

    @Test
    void okIfWhenFalse() {
        final var ok = VoidResult.okIf(false, 10L);
        assertUnchangedErr(ok);
    }

    @Test
    void okIfSupplierWhenTrue() {
        final var called = new AtomicBoolean(false);
        final var ok = VoidResult.okIf(true, () -> {
            called.set(true);
            return 10L;
        });
        assertUnchangedOk(ok);
        assertFalse(called.get());
    }

    @Test
    void okIfSupplierWhenFalse() {
        final var called = new AtomicBoolean(false);
        final var error = VoidResult.okIf(false, () -> {
            called.set(true);
            return 10L;
        });
        assertUnchangedErr(error);
        assertTrue(called.get());
    }

    @Test
    void validateWhenTrue() {
        final var predicateCalled = new AtomicBoolean(false);

        final var validateFunc = VoidResult.<Long, Long>validate(x -> {
            predicateCalled.set(true);
            return x > 5;
        }, 10L);

        assertFalse(predicateCalled.get());

        final var result = validateFunc.apply(10L);

        assertTrue(predicateCalled.get());

        assertUnchangedOk(result);
    }

    @Test
    void validateWhenFalse() {
        final var predicateCalled = new AtomicBoolean(false);

        final var validateFunc = VoidResult.<Long, Long>validate(x -> {
            predicateCalled.set(true);
            return x < 5;
        }, 10L);

        assertFalse(predicateCalled.get());

        final var result = validateFunc.apply(10L);

        assertTrue(predicateCalled.get());

        assertUnchangedErr(result);
    }

    @Test
    void validateSupplierWhenTrue() {
        final var predicateCalled = new AtomicBoolean(false);
        final var errorSupplierCalled = new AtomicBoolean(false);

        final var validateFunc = VoidResult.<Long, Long>validate(x -> {
            predicateCalled.set(true);
            return x > 5;
        }, () -> {
            errorSupplierCalled.set(true);
            return 10L;
        });

        assertFalse(predicateCalled.get());
        assertFalse(errorSupplierCalled.get());

        final var result = validateFunc.apply(10L);

        assertTrue(predicateCalled.get());
        assertFalse(errorSupplierCalled.get());

        assertUnchangedOk(result);
    }

    @Test
    void validateSupplierWhenFalse() {
        final var predicateCalled = new AtomicBoolean(false);
        final var errorSupplierCalled = new AtomicBoolean(false);

        final var validateFunc = VoidResult.<Long, Long>validate(x -> {
            predicateCalled.set(true);
            return x < 5;
        }, () -> {
            errorSupplierCalled.set(true);
            return 10L;
        });

        assertFalse(predicateCalled.get());
        assertFalse(errorSupplierCalled.get());

        final var result = validateFunc.apply(10L);

        assertTrue(predicateCalled.get());
        assertTrue(errorSupplierCalled.get());

        assertUnchangedErr(result);
    }
}
