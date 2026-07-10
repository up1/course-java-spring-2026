# Demo with Spring Modulith
* https://spring.io/projects/spring-modulith

## Structure of modules
* Store event in H2 database
```
REST API => order module === <OrderPlacedEvent>===> report module
```

## Show modules with PlantUML
Run test
```
$mvnw clean test
```

```plantuml
@startuml
title <size:24>ModulithApplication</size>

set separator none
top to bottom direction

<style>
  root {
    BackgroundColor: #ffffff
    FontColor: #444444
  }
</style>

!include <C4/C4>
!include <C4/C4_Context>
!include <C4/C4_Component>

System_Boundary("ModulithApplication_boundary", "ModulithApplication", $tags="") {
  Container_Boundary("ModulithApplication.ModulithApplication_boundary", "ModulithApplication", $tags="") {
    Component(ModulithApplication.ModulithApplication.Order, "Order", $techn="Module", $descr="", $tags="", $link="")
    Component(ModulithApplication.ModulithApplication.Report, "Report", $techn="Module", $descr="", $tags="", $link="")
  }

}

Rel(ModulithApplication.ModulithApplication.Report, ModulithApplication.ModulithApplication.Order, "listens to", $techn="", $tags="", $link="")

SHOW_LEGEND(true)
hide stereotypes
@enduml
```