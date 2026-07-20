# Demo with Resilience Features in [Spring Framework 7](https://docs.spring.io/spring-framework/reference/core/resilience.html)
* Retryable
* ConcurrencyLimit



## Testing with Retryable
```
$curl -X GET http://localhost:8080/testing/retry
```

## Testing with ConcurrencyLimit
```
$curl -X GET http://localhost:8080/testing/limit
```