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