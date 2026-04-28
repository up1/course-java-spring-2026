import java.util.concurrent.CompletableFuture;

public class CompletableFutureExample {
    public static void main(String[] args) {
        // 1. Start the task and chain dependencies immediately
        CompletableFuture.supplyAsync(() -> fetchRawData())
                .thenApply(data -> data.toUpperCase()) // 2. Transform when ready
                .thenAccept(processed -> { // 3. Consume when ready
                    System.out.println("Result: " + processed);
                })
                .exceptionally(ex -> { // 4. Handle errors cleanly
                    System.out.println("Error: " + ex.getMessage());
                    return null;
                });

        // 5. Main thread NEVER blocks; it can keep working!
        System.out.println("Doing other things while data fetches...");

        // (Only used in main() to prevent the program from exiting early)
        System.err.println("Waiting for async tasks to complete...");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
    }

    private static String fetchRawData() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        return "Raw Data";
    }
}