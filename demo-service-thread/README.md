# Demo service with thread pool in Spring Boot + Java 21
* Tomcat Thread
* Virtual Thread


## Flow of api
```
Client -> Controller -> Service -> Get User from API
                                -> Get Orders from API
                                -> Get Payment from API
    
```


## Testing service with default thread pool (Tomcat Thread)
* server.tomcat.threads.max=10
```
$curl -X GET http://localhost:8080/aggregate
```

Logging output:
```
[demo-service-thread] [nio-8080-exec-1] c.e.thread.demo.AggregatorController     : Controller running on Thread[#44,http-nio-8080-exec-1,5,main]
[demo-service-thread] [nio-8080-exec-1] c.example.thread.demo.AggregatorService  : Service aggregate() running on Thread[#44,http-nio-8080-exec-1,5,main]
[demo-service-thread] [     virtual-61] c.example.thread.demo.AggregatorService  : fetchOrders running on VirtualThread[#61]/runnable@ForkJoinPool-1-worker-2
[demo-service-thread] [     virtual-63] c.example.thread.demo.AggregatorService  : fetchPayments running on VirtualThread[#63]/runnable@ForkJoinPool-1-worker-3
[demo-service-thread] [     virtual-59] c.example.thread.demo.AggregatorService  : fetchUser running on VirtualThread[#59]/runnable@ForkJoinPool-1-worker-1
```

## Load testing with k6
```
$cd k6
$k6 run load_test.js
``` 

## Enabling virtual thread
```
spring.threads.virtual.enabled=true
```
Testing service with virtual thread
```
$curl -X GET http://localhost:8080/aggregate
```

Logging output:
```
[demo-service-thread] [omcat-handler-0] c.e.thread.demo.AggregatorController     : Controller running on VirtualThread[#49,tomcat-handler-0]/runnable@ForkJoinPool-1-worker-1
[demo-service-thread] [omcat-handler-0] c.example.thread.demo.AggregatorService  : Service aggregate() running on VirtualThread[#49,tomcat-handler-0]/runnable@ForkJoinPool-1-worker-1
[demo-service-thread] [     virtual-53] c.example.thread.demo.AggregatorService  : fetchOrders running on VirtualThread[#53]/runnable@ForkJoinPool-1-worker-3
[demo-service-thread] [     virtual-52] c.example.thread.demo.AggregatorService  : fetchUser running on VirtualThread[#52]/runnable@ForkJoinPool-1-worker-2
[demo-service-thread] [     virtual-55] c.example.thread.demo.AggregatorService  : fetchPayments running on VirtualThread[#55]/runnable@ForkJoinPool-1-worker-4
```

Load test with K6 again !!

## Working with Docker compose

### Building with Docker compose
```
$docker compose build demo-service-thread
$docker compose up -d demo-service-thread
$docker compose ps
```

### Load testing with k6
```
$docker compose up k6
``` 
