package de.berlin.benchmark;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * based on https://apexapps.oracle.com/pls/apex/f?p=44785:145:2808890149081::NO:RP,145:P145_EVENT_ID,P145_PREV_PAGE:5067,2
 */
public class Benchmark {

    private static final int RUN_COUNT = 5;

    /**
     * Used by the measure method to determine how long a Supplier takes to
     * return a result.
     *
     * @param <T>      The type of the result provided by the Supplier
     * @param label    Description of what's being measured
     * @param supplier The Supplier to measure execution time of
     * @return
     */
    static <T> void measureOneRun(String label, Supplier<T> supplier) {

        long start = System.nanoTime();
        supplier.get();
        long end = System.nanoTime();
        long run = TimeUnit.NANOSECONDS.toMillis(end - start);

        String msg = String.format(" %s runtime: %d ms", label, run);
        System.out.println(msg);
    }

    /**
     * Repeatedly generate results using a Supplier to eliminate some of the
     * issues of running a micro-benchmark.
     * So how do we implement this method from within our  class? From the library point of view,
     * we can just use the builtin Supplier functional interface, which has a single get method.
     *
     * @param <T>      The type of result generated by the Supplier
     * @param label    Description of what's being measured
     * @param supplier The Supplier to measure execution time of
     * @return The last execution time of the Supplier code
     */
    static <T> void measure(String label, Supplier<T> supplier) {

        for (int i = 0; i < RUN_COUNT; i++)
            measureOneRun(label, supplier);
    }

    /**
     * Computes the Levenshtein distance between every pair of words in the
     * subset, and returns a matrix of distances. This actually computes twice as
     * much as it needs to, since for every word a, b it should be the case that
     * lev(a,b) == lev(b,a) i.e., Levenshtein distance is commutative.
     *
     * @param wordList The subset of words whose distances to compute
     * @param parallel Whether to run in parallel
     * @return Matrix of Levenshtein distances
     */
    static int[][] computeSimilarity(List<String> wordList, boolean parallel) {

        final int LIST_SIZE = wordList.size();
        int[][] distances = new int[LIST_SIZE][LIST_SIZE];

        IntStream range = IntStream.range(0, LIST_SIZE);

        IntStream indexI = parallel ? range.parallel() : range;

        indexI.forEach(i -> {
            IntStream.range(0, LIST_SIZE).forEach(j -> {
                distances[i][j] = Levenshtein.lev(wordList.get(i), wordList.get(j));
            });
        });
        return distances;
    }


    /**
     * Process a list of random strings and return a modified list
     *
     * @param wordList The subset of words whose distances to compute
     * @param parallel Whether to run in parallel
     * @return The list processed in whatever way you want
     */
    static List<String> processWords(List<String> wordList, boolean parallel) {

        Stream<String> words = parallel ? wordList.parallelStream() : wordList.stream();
        List<String> result = words
                .filter(s -> s.length() % 2 == 0)
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());
        return result;
    }

    /**
     * Main entry point for application
     *
     * @param args the command line arguments
     * @throws IOException If word file cannot be read
     */
    public static void main(String[] args) throws IOException {
        //System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

        RandomWords fullWordList = new RandomWords();
        List<String> wordList = fullWordList.select(1000);

        measure("similarity sequentiell ", () -> computeSimilarity(wordList, false));
        measure("similarity parallel ", () -> computeSimilarity(wordList, true));

        measure("processWords sequentiell ", () -> processWords(wordList, false));
        measure("processWords parallel ", () -> processWords(wordList, true));

    }
}


