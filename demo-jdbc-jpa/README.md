# Workshop with JDBC vs Spring Data JPA
* JDBC API
* Spring Data JPA
* H2 Database


## Requirements
```
POST /orders
GET  /orders/{id}
GET  /orders
```

Database
```
orders 1 ───> * order_items
```


## Testing

### Create a new Order
```
$curl -X POST http://localhost:8080/jdbc/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Somkiat",
    "items": [
      {
        "productName": "Mechanical Keyboard",
        "quantity": 1,
        "unitPrice": 3500.00
      },
      {
        "productName": "USB-C Cable",
        "quantity": 2,
        "unitPrice": 450.00
      }
    ]
  }'
```

### Get All Orders
```
$curl http://localhost:8080/jdbc/orders
```

### Get Order by ID
```
$curl http://localhost:8080/jdbc/orders/1
```

## Practical recommendation
* Spring JDBC
    * Use when you want to have full control over SQL queries and database interactions
    * Suitable for simple applications or when performance is critical
    * Requires more boilerplate code for CRUD operations
* Spring Data JPA
    * Use when you want to leverage the power of ORM and reduce boilerplate code
    * Suitable for complex applications with relationships and entity management
    * Provides built-in support for pagination, sorting, and query derivation

Note:
```
Order command/write operations → Spring Data JPA
Complex reports and dashboards → Spring JDBC
```