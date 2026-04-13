package dev.mhh.results;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VoidResultTest {
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
    void replaceWhenOk() {
        final var success = VoidResult.ok();

        final var replaced = success.replace(10L);

        assertTrue(success.isOk());
        assertTrue(replaced.isOk());
        assertEquals(Optional.of(10L), replaced.value());
        assertEquals(Optional.empty(), replaced.error());
    }

   @Test
   void replaceWhenErr() {
        final var error = VoidResult.err(10L);

        final var replaced = error.replace(10L);

        assertTrue(error.isError());
        assertTrue(replaced.isError());
        assertEquals(Optional.empty(), replaced.value());
        assertEquals(Optional.of(10L), replaced.error());
   }
}
