# Trip Finite State Machine

Implementation of a Finite State Machine defining a rider's actions when taking a jeepney ride.

## Getting Started

### Prerequisites

Make sure the following softwares has been installed before proceeding

* [Java 1.8] (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Java Virtual Machine required to compile and run scala
* [Maven 3] (https://maven.apache.org/download.cgi) - dependency management, build and testing tool
* [Git] (https://git-scm.com/) - version control system used to commit to github

### Installation

Check that Java 1.8 is installed
```
java -version
```

Check that JAVA_HOME is set to java installation directory. JAVA_HOME will be used by Maven 3 to build the project
```
echo $JAVA_HOME
```

Check that Maven 3 is installed
```
mvn -version
```

Check that Git is installed
```
git --version
```

Download the project using git
```
git clone https://github.com/rpacatang/trip-fsm.git
```

## Build and Test

### Go to the project directory
```
cd trip-fsm
```

### Compile and execute the test
Maven will automatically download the dependency jars before compiling and running the tests
```
mvn clean package
```

## Built with
Technologies and tools used

* [Scala](https://www.scala-lang.org/)
* [Java](https://www.java.com/en/)
* [Maven](https://maven.apache.org/)
* [Git](https://git-scm.com/)
* [Akka](http://akka.io/)
* [LevelDB](http://leveldb.org/)
* [Eclipse](https://eclipse.org/)

## Author
* **Ralph Juncel Pacatang** - [Github](https://github.com/rpacatang) - [Linkedin](https://www.linkedin.com/in/ralph-juncel-pacatang-8711a76a/)
