# Demo with GraalVM

## Simple java process
```
$javac -version
$java -version

$javac HelloWorld.java
$java HelloWorld

or 

$java HelloWorld.java
```

## Working with [GraalVM](https://www.graalvm.org/)

Check JRE with GraalVM
```
$java -version                     

java version "25.0.3" 2026-04-21 LTS
Java(TM) SE Runtime Environment Oracle GraalVM 25.0.3+9.1 (build 25.0.3+9-LTS-jvmci-b01)
Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 25.0.3+9.1 (build 25.0.3+9-LTS-jvmci-b01, mixed mode, sharing)
```

Compile and run with GraalVM
```
$javac HelloWorld.java
$java HelloWorld

$native-image --help

$native-image HelloWorld
$./helloworld

$native-image HelloWorld -o demo
$./demo
```

## Working with Spring Boot and GraalVM
* Create project with Spring Initializr
* Add `Spring Web` dependency
* Add `GraalVM Native Support ` dependency
* [Packaging of Spring Boot](https://docs.spring.io/spring-boot/reference/packaging/index.html)


Create HelloController.java
```
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, GraalVM!";
    }
}
```

Build and run
```
$./mvnw spring-boot:run

$./gradlew bootRun
```

Access to http://localhost:8080/hello


Build native image
```
$./mvnw -Pnative native:compile
$./target/demonative


$./gradlew nativeCompile
```

Access to http://localhost:8080/hello again !!

## Working with Container Image
* https://docs.spring.io/spring-boot/how-to/native-image/developing-your-first-application.html
