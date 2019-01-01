## Synopsis

Tests using [Scala](https://www.scala-lang.org) and [Spark](https://spark.apache.org) for Big Data projects.

An article was published explaining:

 * [https://github.com/edersoncorbari](https://github.com/edersoncorbari)

#### Provided Mechanisms ####

 * The template of a project using SBT;
 * A top-down graph to find a company's employee hierarchy.
 
#### Build and Running ####
 
To compile the project, you must have the following tools installed:

* [SBT-1.2.0](https://www.scala-sbt.org/download.html) or greater.

Now run the commands below to compile the project:

```shell
$ git clone https://github.com/edersoncorbari/scala-lab.git
$ cd scala-lab
$ sbt update; sbt compile; sbt run
```

#### Testing ####
 
To perform all the tests please run the command:
 
```shell
$ sbt test
```

To run a specific test run the command:

```shell
$ sbt
```

Within the SBT console perform a specific test:

```shell
> testOnly io.github.edersoncorbari.graph.HierarchyEmployeeModelTest
```

