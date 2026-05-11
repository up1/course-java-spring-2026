import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoSimpleThreadWithoutPool {

    public static void main(String[] args) throws InterruptedException {

        // Print process id
        String processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        System.out.println("Process ID: " + processId);

        final int TOTAL_TASKS = 1_000_000;
        AtomicInteger completed = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);
        List<Thread> threads = new ArrayList<>();

        long start = System.currentTimeMillis();

        System.out.println("Creating " + TOTAL_TASKS + " threads without a pool...");

        for (int i = 0; i < TOTAL_TASKS; i++) {
            final int taskId = i;
            try {
                Thread t = new Thread(() -> {
                    // Simulate lightweight work
                    int result = taskId * 2;
                    completed.incrementAndGet();

                    // Slow process to increase chance of OOM
                    try {Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                });
                t.start();
                threads.add(t);
            } catch (OutOfMemoryError e) {
                failed.incrementAndGet();
            }
        }

        for (Thread t : threads) {
            t.join();
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("Completed tasks : " + completed.get());
        System.out.println("Failed (OOM)    : " + failed.get());
        System.out.println("Elapsed time    : " + elapsed + " ms");
    }
}

