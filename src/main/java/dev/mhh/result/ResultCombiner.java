package dev.mhh.result;

import dev.mhh.result.functions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for combining multiple {@link Result} instances into a single {@code Result}.
 *
 * <p>Each {@code combine} overload accepts a function and 2–8 {@code Result} arguments. If all
 * results are successful, the function is applied to their unwrapped values and the output is
 * wrapped in {@code Result.ok}. If one or more results are errors, all errors are collected into
 * a {@link List} and returned as {@code Result.err} — the function is never invoked.
 */
public final class ResultCombiner {
    private ResultCombiner() { }

    interface VarFunction<T> {
        T apply(Object... args);
    }

    @SafeVarargs
    static <T, E> Result<T, List<E>> combine(
            VarFunction<T> function,
            ResultWithValue<Object, E>... resultArgs
    ) {
        final var args = new Object[resultArgs.length];
        final var errors =  new ArrayList<E>();
        for (int i = 0; i < resultArgs.length; i++) {
            final int finalI = i;
            final var arg = resultArgs[finalI];
            arg.consume(object -> args[finalI] = object);
            arg.consumeError(errors::add);
        }
        if (!errors.isEmpty()) {
            return Result.err(errors);
        }
        return Result.ok(function.apply(args));
    }

    /**
     * Combines two {@code Result} instances using a 2-argument function.
     *
     * @param <T1>     success type of {@code result1}
     * @param <T2>     success type of {@code result2}
     * @param <T>      success type of the returned {@code Result}
     * @param <E>      error type shared by all results
     * @param function applied to {@code (t1, t2)} when both results are successful
     * @param result1  first {@code Result}
     * @param result2  second {@code Result}
     * @return {@code Result.ok(function.apply(t1, t2))} if all inputs succeed;
     *         {@code Result.err(errors)} otherwise
     */
    @SuppressWarnings("unchecked")
    public static <T1, T2, T, E> Result<T, List<E>> combine(
            Function2<T1, T2, T> function,
            ResultWithValue<T1, E> result1,
            ResultWithValue<T2, E> result2
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1]);
        return combine(varFunction, (ResultWithValue<Object, E>) result1, (ResultWithValue<Object, E>) result2);
    }

    /**
     * Combines three {@code Result} instances using a 3-argument function.
     *
     * @param <T1>     success type of {@code result1}
     * @param <T2>     success type of {@code result2}
     * @param <T3>     success type of {@code result3}
     * @param <T>      success type of the returned {@code Result}
     * @param <E>      error type shared by all results
     * @param function applied to {@code (t1, t2, t3)} when all results are successful
     * @param result1  first {@code Result}
     * @param result2  second {@code Result}
     * @param result3  third {@code Result}
     * @return {@code Result.ok(function.apply(...))} if all inputs succeed;
     *         {@code Result.err(errors)} otherwise
     */
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T, E> Result<T, List<E>> combine(
            Function3<T1, T2, T3, T> function,
            ResultWithValue<T1, E> result1,
            ResultWithValue<T2, E> result2,
            ResultWithValue<T3, E> result3
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1], (T3)args[2]);
        return combine(varFunction, (ResultWithValue<Object, E>) result1, (ResultWithValue<Object, E>) result2, (ResultWithValue<Object, E>) result3);
    }

    /**
     * Combines four {@code Result} instances using a 4-argument function.
     *
     * @param <T1>     success type of {@code result1}
     * @param <T2>     success type of {@code result2}
     * @param <T3>     success type of {@code result3}
     * @param <T4>     success type of {@code result4}
     * @param <T>      success type of the returned {@code Result}
     * @param <E>      error type shared by all results
     * @param function applied to {@code (t1, t2, t3, t4)} when all results are successful
     * @param result1  first {@code Result}
     * @param result2  second {@code Result}
     * @param result3  third {@code Result}
     * @param result4  fourth {@code Result}
     * @return {@code Result.ok(function.apply(t1, t2, t3, t4))} if all inputs succeed;
     *         {@code Result.err(errors)} otherwise
     */
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T, E> Result<T, List<E>> combine(
            Function4<T1, T2, T3, T4, T> function,
            ResultWithValue<T1, E> result1,
            ResultWithValue<T2, E> result2,
            ResultWithValue<T3, E> result3,
            ResultWithValue<T4, E> result4
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1], (T3)args[2], (T4)args[3]);
        return combine(varFunction, (ResultWithValue<Object, E>) result1, (ResultWithValue<Object, E>) result2, (ResultWithValue<Object, E>) result3, (ResultWithValue<Object, E>) result4);
    }

    /**
     * Combines five {@code Result} instances using a 5-argument function.
     *
     * @param <T1>     success type of {@code result1}
     * @param <T2>     success type of {@code result2}
     * @param <T3>     success type of {@code result3}
     * @param <T4>     success type of {@code result4}
     * @param <T5>     success type of {@code result5}
     * @param <T>      success type of the returned {@code Result}
     * @param <E>      error type shared by all results
     * @param function applied to {@code (t1, t2, t3, t4, t5)} when all results are successful
     * @param result1  first {@code Result}
     * @param result2  second {@code Result}
     * @param result3  third {@code Result}
     * @param result4  fourth {@code Result}
     * @param result5  fifth {@code Result}
     * @return {@code Result.ok(function.apply(t1, t2, t3, t4, t5))} if all inputs succeed;
     *         {@code Result.err(errors)} otherwise
     */
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T, E> Result<T, List<E>> combine(
            Function5<T1, T2, T3, T4, T5, T> function,
            ResultWithValue<T1, E> result1,
            ResultWithValue<T2, E> result2,
            ResultWithValue<T3, E> result3,
            ResultWithValue<T4, E> result4,
            ResultWithValue<T5, E> result5
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1], (T3)args[2], (T4)args[3], (T5)args[4]);
        return combine(varFunction, (ResultWithValue<Object, E>) result1, (ResultWithValue<Object, E>) result2, (ResultWithValue<Object, E>) result3, (ResultWithValue<Object, E>) result4, (ResultWithValue<Object, E>) result5);
    }

    /**
     * Combines six {@code Result} instances using a 6-argument function.
     *
     * @param <T1>     success type of {@code result1}
     * @param <T2>     success type of {@code result2}
     * @param <T3>     success type of {@code result3}
     * @param <T4>     success type of {@code result4}
     * @param <T5>     success type of {@code result5}
     * @param <T6>     success type of {@code result6}
     * @param <T>      success type of the returned {@code Result}
     * @param <E>      error type shared by all results
     * @param function applied to {@code (t1, t2, t3, t4, t5, t6)} when all results are successful
     * @param result1  first {@code Result}
     * @param result2  second {@code Result}
     * @param result3  third {@code Result}
     * @param result4  fourth {@code Result}
     * @param result5  fifth {@code Result}
     * @param result6  sixth {@code Result}
     * @return {@code Result.ok(function.apply(t1, t2, t3, t4, t5, t6))} if all inputs succeed;
     *         {@code Result.err(errors)} otherwise
     */
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T, E> Result<T, List<E>> combine(
            Function6<T1, T2, T3, T4, T5, T6, T> function,
            ResultWithValue<T1, E> result1,
            ResultWithValue<T2, E> result2,
            ResultWithValue<T3, E> result3,
            ResultWithValue<T4, E> result4,
            ResultWithValue<T5, E> result5,
            ResultWithValue<T6, E> result6
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1], (T3)args[2], (T4)args[3], (T5)args[4], (T6)args[5]);
        return combine(varFunction, (ResultWithValue<Object, E>) result1, (ResultWithValue<Object, E>) result2, (ResultWithValue<Object, E>) result3, (ResultWithValue<Object, E>) result4, (ResultWithValue<Object, E>) result5, (ResultWithValue<Object, E>) result6);
    }

    /**
     * Combines seven {@code Result} instances using a 7-argument function.
     *
     * @param <T1>     success type of {@code result1}
     * @param <T2>     success type of {@code result2}
     * @param <T3>     success type of {@code result3}
     * @param <T4>     success type of {@code result4}
     * @param <T5>     success type of {@code result5}
     * @param <T6>     success type of {@code result6}
     * @param <T7>     success type of {@code result7}
     * @param <T>      success type of the returned {@code Result}
     * @param <E>      error type shared by all results
     * @param function applied to {@code (t1, t2, t3, t4, t5, t6, t7)} when all results are successful
     * @param result1  first {@code Result}
     * @param result2  second {@code Result}
     * @param result3  third {@code Result}
     * @param result4  fourth {@code Result}
     * @param result5  fifth {@code Result}
     * @param result6  sixth {@code Result}
     * @param result7  seventh {@code Result}
     * @return {@code Result.ok(function.apply(t1, t2, t3, t4, t5, t6, t7))} if all inputs succeed;
     *         {@code Result.err(errors)} otherwise
     */
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T, E> Result<T, List<E>> combine(
            Function7<T1, T2, T3, T4, T5, T6, T7, T> function,
            ResultWithValue<T1, E> result1,
            ResultWithValue<T2, E> result2,
            ResultWithValue<T3, E> result3,
            ResultWithValue<T4, E> result4,
            ResultWithValue<T5, E> result5,
            ResultWithValue<T6, E> result6,
            ResultWithValue<T7, E> result7
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1], (T3)args[2], (T4)args[3], (T5)args[4], (T6)args[5], (T7)args[6]);
        return combine(varFunction, (ResultWithValue<Object, E>) result1, (ResultWithValue<Object, E>) result2, (ResultWithValue<Object, E>) result3, (ResultWithValue<Object, E>) result4, (ResultWithValue<Object, E>) result5, (ResultWithValue<Object, E>) result6, (ResultWithValue<Object, E>) result7);
    }

    /**
     * Combines eight {@code Result} instances using an 8-argument function.
     *
     * @param <T1>     success type of {@code result1}
     * @param <T2>     success type of {@code result2}
     * @param <T3>     success type of {@code result3}
     * @param <T4>     success type of {@code result4}
     * @param <T5>     success type of {@code result5}
     * @param <T6>     success type of {@code result6}
     * @param <T7>     success type of {@code result7}
     * @param <T8>     success type of {@code result8}
     * @param <T>      success type of the returned {@code Result}
     * @param <E>      error type shared by all results
     * @param function applied to {@code (t1, t2, t3, t4, t5, t6, t7, t8)} when all results are successful
     * @param result1  first {@code Result}
     * @param result2  second {@code Result}
     * @param result3  third {@code Result}
     * @param result4  fourth {@code Result}
     * @param result5  fifth {@code Result}
     * @param result6  sixth {@code Result}
     * @param result7  seventh {@code Result}
     * @param result8  eighth {@code Result}
     * @return {@code Result.ok(function.apply(t1, t2, t3, t4, t5, t6, t7, t8))} if all inputs succeed;
     *         {@code Result.err(errors)} otherwise
     */
    @SuppressWarnings("unchecked")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T, E> Result<T, List<E>> combine(
            Function8<T1, T2, T3, T4, T5, T6, T7, T8, T> function,
            ResultWithValue<T1, E> result1,
            ResultWithValue<T2, E> result2,
            ResultWithValue<T3, E> result3,
            ResultWithValue<T4, E> result4,
            ResultWithValue<T5, E> result5,
            ResultWithValue<T6, E> result6,
            ResultWithValue<T7, E> result7,
            ResultWithValue<T8, E> result8
    ) {
        final VarFunction<T> varFunction = (args) -> function.apply((T1)args[0], (T2)args[1], (T3)args[2], (T4)args[3], (T5)args[4], (T6)args[5], (T7)args[6], (T8)args[7]);
        return combine(varFunction, (ResultWithValue<Object, E>) result1, (ResultWithValue<Object, E>) result2, (ResultWithValue<Object, E>) result3, (ResultWithValue<Object, E>) result4, (ResultWithValue<Object, E>) result5, (ResultWithValue<Object, E>) result6, (ResultWithValue<Object, E>) result7, (ResultWithValue<Object, E>) result8);
    }
}
