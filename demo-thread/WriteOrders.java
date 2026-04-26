import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class WriteOrders {

    private static final int TOTAL_ORDERS = 10_000_000;
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int BATCH_SIZE = 10_000;
    private static final String OUTPUT_FILE = "orders.csv";
    private static final String POISON_PILL = "__DONE__";
    private static final String[] STATUSES = { "pending", "completed", "cancelled" };
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) throws Exception {
        System.out.printf("Writing %,d orders using %d producer threads...%n", TOTAL_ORDERS, THREAD_COUNT);
        long start = System.currentTimeMillis();

        BlockingQueue<String> queue = new LinkedBlockingQueue<>(THREAD_COUNT * 2);
        AtomicInteger orderIdCounter = new AtomicInteger(1);

        // Writer thread — single writer ensures ordered, non-interleaved output
        Thread writerThread = new Thread(() -> {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE), 1 << 20)) {
                bw.write("order_id,order_date,order_amount,order_status");
                bw.newLine();
                while (true) {
                    String batch = queue.take();
                    if (batch == POISON_PILL)
                        break;
                    bw.write(batch);
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
        writerThread.start();

        // Producer threads — generate order batches concurrently
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);
        int batchCount = TOTAL_ORDERS / BATCH_SIZE;

        for (int b = 0; b < batchCount; b++) {
            pool.submit(() -> {
                Random rng = new Random();
                StringBuilder sb = new StringBuilder(BATCH_SIZE * 64);
                LocalDate baseDate = LocalDate.of(2020, 1, 1);

                for (int i = 0; i < BATCH_SIZE; i++) {
                    int id = orderIdCounter.getAndIncrement();
                    String date = baseDate.plusDays(rng.nextInt(365 * 6)).format(DATE_FMT);
                    double amount = Math.round(rng.nextDouble() * 9999_99) / 100.0;
                    String status = STATUSES[rng.nextInt(STATUSES.length)];
                    sb.append(id).append(',')
                            .append(date).append(',')
                            .append(String.format("%.2f", amount)).append(',')
                            .append(status).append('\n');
                }
                try {
                    queue.put(sb.toString());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.MINUTES);
        queue.put(POISON_PILL);
        writerThread.join();

        long elapsed = System.currentTimeMillis() - start;
        System.out.printf("Done. %,d orders written to %s in %.2f seconds.%n",
                TOTAL_ORDERS, OUTPUT_FILE, elapsed / 1000.0);
    }
}
