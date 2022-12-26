jmh-kotlin 
[![official JetBrains project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
==========

A set of JMH benchmarks for various Kotlin language constructions and standard library functions.

Build:
```
mvn clean package
```

Note: need clean package every time, otherwise removed benchmarks will be hanging around.

Build with custom kotlin version:
```
mvn clean package -Dkotlin.version=<VERSION>
```

Run:
```
java -jar target/benchmarks.jar
```

Also see commands.txt for more pre-configured commands.
