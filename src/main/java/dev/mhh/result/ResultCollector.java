package dev.mhh.result;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A {@link Collector} that partitions a stream of {@link Result} values into
 * two lists: one for successful values and one for errors.
 *
 * <p>If all elements are {@code Ok}, the resulting {@code Result} will be
 * {@code Ok} containing a list of all unwrapped values. If any element is
 * {@code Err}, the resulting {@code Result} will be {@code Err} containing
 * a list of all unwrapped errors.
 *
 * <p>Example usage:
 * <pre>{@code
 * Result<List<Integer>, List<String>> result = Stream.of(
 *         Result.ok(1),
 *         Result.err("oops"),
 *         Result.ok(3)
 *     )
 *     .collect(ResultCollector.collector());
 * }</pre>
 *
 * <p>This collector does not support {@link Characteristics#CONCURRENT} or
 * {@link Characteristics#UNORDERED}, so encounter order is preserved in both
 * output lists.
 *
 * @param <T> the type of success values
 * @param <E> the type of error values
 */
public final class ResultCollector<T, E> implements Collector<Result<T, E>, ResultCollector<T, E>, Result<List<T>, List<E>>>{
    public List<T> ok = new ArrayList<>();
    public List<E> err = new ArrayList<>();

    private ResultCollector() { }

    /**
     * Returns a collector that partitions a stream of {@link Result} values
     * into a single {@code Result} containing either a list of all success
     * values or a list of all errors.
     *
     * <p>The returned {@code Result} is {@code Err} if one or more elements
     * in the stream are {@code Err}; otherwise it is {@code Ok}. In both
     * cases, all values of that kind are collected — the stream is always
     * consumed in full.
     *
     * @param <T> the type of success values
     * @param <E> the type of error values
     * @return a collector over {@code Result<T, E>} elements
     */
    public static <T, E> Collector<Result<T, E>, ResultCollector<T, E>, Result<List<T>, List<E>>> collector() {
        return new ResultCollector<>();
    }

    @Override
    public Supplier<ResultCollector<T, E>> supplier() {
        return ResultCollector::new;
    }

    @Override
    public BiConsumer<ResultCollector<T, E>, Result<T, E>> accumulator() {
        return (collector, result) -> {
            switch (result) {
                case Ok(var value) -> collector.ok.add(value);
                case Err(var error) -> collector.err.add(error);
            }
        };
    }

    @Override
    public BinaryOperator<ResultCollector<T, E>> combiner() {
        return (left, right) -> {
            left.ok.addAll(right.ok);
            left.err.addAll(right.err);
            return left;
        };
    }

    @Override
    public Function<ResultCollector<T, E>, Result<List<T>, List<E>>> finisher() {
        return collector -> collector.err.isEmpty()
                ? Result.ok(collector.ok)
                : Result.err(collector.err);
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }
}
