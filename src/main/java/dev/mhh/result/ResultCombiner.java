package dev.mhh.result;

import dev.mhh.result.functions.*;

import java.util.ArrayList;
import java.util.List;

public final class ResultCombiner {
    private ResultCombiner() { }

    interface VarFunction<T> {
        T apply(Object... args);
    }

    @SafeVarargs
    static <T, E> Result<T, List<E>> combine(
            VarFunction<T> function,
            Result<Object, E>... resultArgs
    ) {
        final var args = new Object[resultArgs.length];
        final var errors =  new ArrayList<E>();
        for (int i = 0; i < resultArgs.length; i++) {
            final var arg = resultArgs[i];
            int finalI = i;
            arg.consume(object -> args[finalI] = object)
                    .consumeError(errors::add);
        }
        if (!errors.isEmpty()) {
            return Result.err(errors);
        }
        return Result.ok(function.apply(args));
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T, E> Result<T, List<E>> combine(
            Function2<T1, T2, T> function,
            Result<T1, E> result1,
            Result<T2, E> result2
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1]);
        return combine(varFunction, (Result<Object, E>) result1, (Result<Object, E>) result2);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T, E> Result<T, List<E>> combine(
            Function3<T1, T2, T3, T> function,
            Result<T1, E> result1,
            Result<T2, E> result2,
            Result<T3, E> result3
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1], (T3)args[2]);
        return combine(varFunction, (Result<Object, E>) result1, (Result<Object, E>) result2, (Result<Object, E>) result3);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T, E> Result<T, List<E>> combine(
            Function4<T1, T2, T3, T4, T> function,
            Result<T1, E> result1,
            Result<T2, E> result2,
            Result<T3, E> result3,
            Result<T4, E> result4
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1], (T3)args[2], (T4)args[3]);
        return combine(varFunction, (Result<Object, E>) result1, (Result<Object, E>) result2, (Result<Object, E>) result3, (Result<Object, E>) result4);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T, E> Result<T, List<E>> combine(
            Function5<T1, T2, T3, T4, T5, T> function,
            Result<T1, E> result1,
            Result<T2, E> result2,
            Result<T3, E> result3,
            Result<T4, E> result4,
            Result<T5, E> result5
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1], (T3)args[2], (T4)args[3], (T5)args[4]);
        return combine(varFunction, (Result<Object, E>) result1, (Result<Object, E>) result2, (Result<Object, E>) result3, (Result<Object, E>) result4, (Result<Object, E>) result5);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T, E> Result<T, List<E>> combine(
            Function6<T1, T2, T3, T4, T5, T6, T> function,
            Result<T1, E> result1,
            Result<T2, E> result2,
            Result<T3, E> result3,
            Result<T4, E> result4,
            Result<T5, E> result5,
            Result<T6, E> result6
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1], (T3)args[2], (T4)args[3], (T5)args[4], (T6)args[5]);
        return combine(varFunction, (Result<Object, E>) result1, (Result<Object, E>) result2, (Result<Object, E>) result3, (Result<Object, E>) result4, (Result<Object, E>) result5, (Result<Object, E>) result6);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T, E> Result<T, List<E>> combine(
            Function7<T1, T2, T3, T4, T5, T6, T7, T> function,
            Result<T1, E> result1,
            Result<T2, E> result2,
            Result<T3, E> result3,
            Result<T4, E> result4,
            Result<T5, E> result5,
            Result<T6, E> result6,
            Result<T7, E> result7
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1], (T3)args[2], (T4)args[3], (T5)args[4], (T6)args[5], (T7)args[6]);
        return combine(varFunction, (Result<Object, E>) result1, (Result<Object, E>) result2, (Result<Object, E>) result3, (Result<Object, E>) result4, (Result<Object, E>) result5, (Result<Object, E>) result6, (Result<Object, E>) result7);
    }

    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T, E> Result<T, List<E>> combine(
            Function8<T1, T2, T3, T4, T5, T6, T7, T8, T> function,
            Result<T1, E> result1,
            Result<T2, E> result2,
            Result<T3, E> result3,
            Result<T4, E> result4,
            Result<T5, E> result5,
            Result<T6, E> result6,
            Result<T7, E> result7,
            Result<T8, E> result8
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1], (T3)args[2], (T4)args[3], (T5)args[4], (T6)args[5], (T7)args[6], (T8)args[7]);
        return combine(varFunction, (Result<Object, E>) result1, (Result<Object, E>) result2, (Result<Object, E>) result3, (Result<Object, E>) result4, (Result<Object, E>) result5, (Result<Object, E>) result6, (Result<Object, E>) result7, (Result<Object, E>) result8);
    }
}
