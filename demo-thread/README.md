# Workshop with Data strcuture and Thread


## 1. Write order to file
* orders.csv
  * order_id
  * order_date
  * order_amount
  * order_status ()
    * pending
    * completed
    * cancelled

### Structure of process :: producer/consumer with threads:
* THREAD_COUNT producer threads (auto-sized to CPU cores) generate orders in batches of 10,000 concurrently
* 1 writer thread consumes batches from a BlockingQueue and writes to orders.csv sequentially — avoids locking/interleaving issues
* BufferedWriter with 1 MB buffer minimizes I/O syscalls

Run
```
// Generate 10 million orders and write to orders.csv
$java SimpleWrite.java

// Generate 10 million orders with multiple producer threads and write to orders.csv
$java WriteOrders.java
```

## 2. Read order by id
* Approach 1: O(n) linear scan
  * Read orders.csv line by line, parse order_id and order_amount, and check for matching order_id
  * Use BufferedReader for efficient file reading
* Approach 2: O(1) HashMap index
  * Read orders.csv line by line, parse order_id and order_amount, and store in a HashMap<Integer, Double> for O(1) retrieval by order_id
  * Use BufferedReader for efficient file reading


```
$java ReadOrderById.java 
=== Approach 1: O(n) linear scan ===
  order_id=         1  amount=   9348.62  time=12 ms
  order_id=   500,000  amount=   1593.69  time=61 ms
  order_id= 5,000,000  amount=   5444.14  time=412 ms
  order_id= 9,999,999  amount=   1039.36  time=575 ms

=== Approach 2: O(1) HashMap (building index…) ===
  Index built: 10,000,000 entries in 1509 ms
  order_id=         1  amount=   9348.62  time=0 ms
  order_id=   500,000  amount=   1593.69  time=0 ms
  order_id= 5,000,000  amount=   5444.14  time=0 ms
  order_id= 9,999,999  amount=   1039.36  time=0 ms
```

## 3. Read orders and calculate total amount
* Approach 1: O(n) linear scan
  * Read orders.csv line by line, parse order_amount, and sum for all orders with order_status = "completed"
  * Use BufferedReader for efficient file reading
* Approach 2: O(n) parallel stream (ForkJoinPool with available CPU cores)
  * Read orders.csv line by line, parse order_amount, and sum for all orders with order_status = "completed" using Java 8 parallel streams for concurrent processing
  * Use BufferedReader for efficient file reading

  ```
  $java SummarizeOrder.java
=== Approach 1: Sequential O(n) ===
  Total completed amount : 16,667,398,499.30
  Time                   : 1061 ms

=== Approach 2: Parallel Stream O(n) ===
  Total completed amount : 16,667,398,499.30
  Time                   : 832 ms

Speedup: 1.28x  (cores available: 12)
```

## 4. Demo with Threads
* Thread Pool
  * Efficiently manage a pool of worker threads to execute tasks concurrently without blocking the main thread
  * CPU-bound tasks can benefit from a fixed thread pool sized to the number of CPU cores, while I/O-bound tasks can use a cached thread pool for dynamic scaling
* Virtual Thread (Project Loom)
  * Memory-efficient threads that can handle millions of concurrent tasks without blocking OS threads

```
$java DemoThreadPool.java 

$java DemoVirtualThread.java
```

## 5. Virtual Thread in Soring Boot
* Use virtual threads to handle a large number of concurrent requests without blocking OS threads
* Configure Spring Boot to use virtual threads for request handling by setting the appropriate thread pool executor
* Demonstrate improved scalability and responsiveness under high load compared to traditional thread pools  


Configure max thread of tomcat to 100 in application.properties:
```
server.tomcat.max-threads=100
```

Config in application.properties to enable virtual threads:
```
spring.threads.virtual.enabled=true
```

Create DemoController.java
```
@RestController
public class DemoController {
    @GetMapping("/demo")
    public String demo() {
        // Simulate some work
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Hello from virtual thread!";
    }
}
```

Start server and send requests:
```
$./mvnw spring-boot:run


$curl http://localhost:8080/demo
```

Monitoring thread with jconsole or [visualvm](https://visualvm.github.io/):
```
$jconsole
```

