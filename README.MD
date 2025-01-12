## seed lab

a lib for generate test data in unit-test

### install

```bash
mvn install
```

```pom
<dependency>
    <groupId>tech.shuihai.facai</groupId>
    <artifactId>seedlab</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### usage

```java
var priorityFullRandomProducer = SeedLab.fullyRandom(Priority.class);
var priorities = priorityFullRandomProducer.invoke(50);
for (Priority priority: priorities) {
    Assertions.assertTrue(
        Arrays.asList(Priority.values()).contains(priority)
    );
}
```
