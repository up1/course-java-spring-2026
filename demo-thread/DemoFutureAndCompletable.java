
// Demo with Future and CompletableFuture
import java.util.concurrent.*;

public class DemoFutureAndCompletable {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        demoFuture();
        demoCompletableFuture();
    }

    private static void demoFuture() throws InterruptedException, ExecutionException {
        // Using Future
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(2000); // Simulate long-running task
            return 42;
        });

        System.out.println("Doing other work while waiting for the result...");
        Integer result = future.get(); // This will block until the result is available
        System.out.println("Result from Future: " + result);
        executor.shutdown();
    }

    private static void demoCompletableFuture() throws InterruptedException {
        // Using CompletableFuture
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000); // Simulate long-running task
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 42;
        });

        System.out.println("Doing other work while waiting for the CompletableFuture...");
        completableFuture.thenAccept(
            res -> System.out.println("Result from CompletableFuture: " + res));

        // Wait for the CompletableFuture to complete before exiting
        completableFuture.join();
    }
}