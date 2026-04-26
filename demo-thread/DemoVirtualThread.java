import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoVirtualThread {

    static void process(int taskId, AtomicInteger completed) {
        // Simulate lightweight work
        int result = taskId * 2;

        // Slow down processing to increase contention
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        completed.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
        final int TOTAL_TASKS = 10_000_000;
        AtomicInteger completed = new AtomicInteger(0);

        long start = System.currentTimeMillis();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < TOTAL_TASKS; i++) {
                final int taskId = i;
                executor.submit(() -> process(taskId, completed));
            }
        } // auto-shutdown and await termination on close()

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Completed tasks : " + completed.get());
        System.out.println("Elapsed time    : " + elapsed + " ms");
    }
}
