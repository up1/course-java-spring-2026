# Demo with Spring Webflux
* H2
* R2DBC


## Create a new product
```
$curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Webcam","price":1990.00}'
```