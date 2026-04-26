import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoThreadPool {

    public static void main(String[] args) throws InterruptedException {
        final int TOTAL_TASKS = 10_000_000;
        AtomicInteger completed = new AtomicInteger(0);

        ExecutorService executor = new ThreadPoolExecutor(
                20, // core threads
                50, // max threads
                60, TimeUnit.SECONDS, // idle timeout
                new ArrayBlockingQueue<>(1000), // bounded queue
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        long start = System.currentTimeMillis();

        for (int i = 0; i < TOTAL_TASKS; i++) {
            final int taskId = i;
            executor.submit(() -> {
                // Simulate lightweight work
                int result = taskId * 2;

                // Slow down processing to increase contention
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                completed.incrementAndGet();
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Completed tasks : " + completed.get());
        System.out.println("Elapsed time    : " + elapsed + " ms");
    }
}
