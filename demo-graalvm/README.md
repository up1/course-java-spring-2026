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

