import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class HelloThread {

    public static void main(String[] args) {
        simpleThreadExample();
        simpleThreadWithFutureExample();
        simpleThreadWithPoolExample();
    }

    private static void simpleThreadExample() {
        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            new Thread(() -> {
                System.out.println("Hello from simpleThreadExample " + threadId);
            }).start();
        }
    }

    private static void simpleThreadWithFutureExample() {
        // Create 10 threads with result using FutureTask
        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            FutureTask<String> futureTask = new FutureTask<>(() -> {
                return "Hello from simpleThreadWithFutureExample " + threadId;
            });
            new Thread(futureTask).start();

            try {
                String result = futureTask.get();
                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    private static void simpleThreadWithPoolExample() {
        // Create a thread pool and submit 10 tasks
        try (ExecutorService executor = Executors.newFixedThreadPool(5)) {
            for (int i = 0; i < 10; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    System.out.println("Hello from simpleThreadWithPoolExample " + threadId);
                });
            }
        }
    }

    


    
}
