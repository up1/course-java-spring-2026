# Working with Database
* Manage transaction with `@Transactional`
* Manage multiple data sources


## Demo Code with Transactional
* Create `Product` entity
* Create `ProductRepository` interface
* Create `ProductService` class with `@Transactional` annotation
* Create `ProductController` class with REST endpoints
* Test with curl

Enable log of sql in file `application.properties`
```
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

Enable transaction log in file `application.properties` 
```
logging.level.org.springframework.transaction=DEBUG
logging.level.org.hibernate.engine.transaction.internal.TransactionImpl=DEBUG
```

Testing with curl 1
```
$curl -X POST -H "Content-Type: application/json" -d '{"name": "Product A", "description": "Description of Product A"}' http://localhost:8080/api/process1
``` 

Testing with curl 2
```
$curl -X POST -H "Content-Type: application/json" -d '{"name": "Product A", "description": "Description of Product A"}' http://localhost:8080/api/process2
``` 