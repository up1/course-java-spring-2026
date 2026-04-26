import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

public class SummarizeOrder {

    private static final String FILE = "orders.csv";

    // ----------------------------------------------------------------
    // Approach 1 : O(n) sequential — single-threaded baseline
    // Reads line by line with BufferedReader, filters completed orders,
    // and accumulates the sum.
    // ----------------------------------------------------------------
    static double sumCompletedSequential() throws IOException {
        double total = 0.0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE), 1 << 20)) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                // order_id,order_date,order_amount,order_status
                int c3 = line.lastIndexOf(',');
                String status = line.substring(c3 + 1);
                if ("completed".equals(status)) {
                    int c2 = line.lastIndexOf(',', c3 - 1);
                    total += Double.parseDouble(line.substring(c2 + 1, c3));
                }
            }
        }
        return total;
    }

    // ----------------------------------------------------------------
    // Approach 2 : O(n) parallel stream — multi-threaded via ForkJoinPool
    // Files.lines() returns a lazy Stream<String> backed by a BufferedReader.
    // .parallel() splits work across all available CPU cores automatically.
    // Each partial sum is computed independently, then combined — no shared
    // mutable state, so no locking needed.
    // ----------------------------------------------------------------
    static double sumCompletedParallel() throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(FILE))) {
            return lines
                    .skip(1) // skip header
                    .parallel() // fan out to ForkJoinPool.commonPool()
                    .filter(line -> line.endsWith(",completed"))
                    .mapToDouble(line -> {
                        int c3 = line.lastIndexOf(',');
                        int c2 = line.lastIndexOf(',', c3 - 1);
                        return Double.parseDouble(line.substring(c2 + 1, c3));
                    })
                    .sum(); // uses compensated summation for precision
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== Approach 1: Sequential O(n) ===");
        long t0 = System.currentTimeMillis();
        double seq = sumCompletedSequential();
        long seqTime = System.currentTimeMillis() - t0;
        System.out.printf("  Total completed amount : %,.2f%n", seq);
        System.out.printf("  Time                   : %d ms%n%n", seqTime);

        System.out.println("=== Approach 2: Parallel Stream O(n) ===");
        t0 = System.currentTimeMillis();
        double par = sumCompletedParallel();
        long parTime = System.currentTimeMillis() - t0;
        System.out.printf("  Total completed amount : %,.2f%n", par);
        System.out.printf("  Time                   : %d ms%n%n", parTime);

        System.out.printf("Speedup: %.2fx  (cores available: %d)%n",
                (double) seqTime / parTime,
                Runtime.getRuntime().availableProcessors());
    }
}
