# Demo with [Bloom filter](https://en.wikipedia.org/wiki/Bloom_filter)
* Java
* Check username in a large file

## 1. Create a large file with Java
* Customer data 100,000,000 records
  * id
  * username
  * email
  * firstname
  * lastname

```
$java WriteFile.java
```

## 2. Create Bloom filter to check username
```
$java CheckData.java customer1 customer10000000 missing-user

```

