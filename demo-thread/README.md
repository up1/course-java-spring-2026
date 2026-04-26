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
