# Working with Database
* Manage transaction 
  * Default
  * Working with `@Transactional`
  * Working with `@Transactional`, `Propagation` and `Isolation`
  * [Transaction propagation](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/tx-propagation.html)

* [Multiple DataSource](https://github.com/up1/workshop-springboot-20241217/tree/main/multidb)
  * [Reference](https://docs.spring.io/spring-boot/how-to/data-access.html#howto.data-access.configure-two-datasources)

## Demo Code with Transactional
* Create `Product` entity
* Create `ProductRepository` interface
* Create `ProductService` class with `@Transactional` annotation
* Create `ProductController` class with REST endpoints
* Test with curl

Enable log of sql in file `application.properties`
```
# Enable log of database
spring.jpa.show-sql=true
#spring.jpa.properties.hibernate.format_sql=true
#logging.level.org.hibernate.SQL=DEBUG
#logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Enable virtual thread in java 21
spring.threads.virtual.enabled=true
```

Enable transaction log in file `application.properties` 
```
# Enable transaction log
logging.level.org.hibernate.engine.transaction.internal.TransactionImpl=DEBUG
logging.level.org.springframework.transaction=DEBUG
logging.level.org.springframework.orm.jpa=DEBUG
logging.level.org.hibernate.transaction=DEBUG
```

Testing with curl 1
```
$curl -X POST -H "Content-Type: application/json" -d '{"name": "Product A", "description": "Description of Product A"}' http://localhost:8080/api/process1
``` 

Testing with curl 2
```
$curl -X POST -H "Content-Type: application/json" -d '{"name": "Product A", "description": "Description of Product A"}' http://localhost:8080/api/process2
``` 

## Demo Code with Transactional, Propagation and Isolation
* Create `Order` entity
* Create `OrderRepository` interface
* Create `OrderService` class with `@Transactional` annotation, `Propagation` and `Isolation`
* Create `OrderController` class with REST endpoints
* Test with curl

Process with create a new order and update product stock
* Create `OrderService` class with method `createOrder` with `@Transactional` annotation, `Propagation.REQUIRED` and `Isolation.READ_COMMITTED`
* Create `ProductService` class with method `updateStock` with `@Transactional` annotation, `Propagation.REQUIRES_NEW` and `Isolation.READ_COMMITTED`
* Create `OrderController` class with REST endpoint to create order
* Test with curl

Project
```
POST /api/orders
  └─ OrderService.createOrder()          [REQUIRED, READ_COMMITTED]  ← outer TX
       ├─ save Order (status=PENDING)
       ├─ ProductService.updateStock()   [REQUIRES_NEW, READ_COMMITTED] ← inner TX (independent)
       │    └─ deduct stock, save Product
       └─ update Order (status=COMPLETED)
```

Step 1 — create a product with stock:
```
curl -X POST -H "Content-Type: application/json" \
  -d '{"name": "Product A", "description": "Description of Product A", "stock": 10}' \
  http://localhost:8080/api/process2
```

Step 2 — create an order (use the product `id` returned above, e.g. `1`):
```
curl -X POST -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 3}' \
  http://localhost:8080/api/orders
```

Step 3 — test insufficient stock (should throw error):
```
curl -X POST -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 100}' \
  http://localhost:8080/api/orders
```
```
