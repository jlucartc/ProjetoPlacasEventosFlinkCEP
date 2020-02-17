name := "ProjetoPlacasEventosFlinkCEP"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.3.0"

libraryDependencies += "org.apache.flink" %% "flink-connector-kafka" % "1.9.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime

libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.2" % Test

libraryDependencies += "org.apache.flink" %% "flink-streaming-scala" % "1.9.0"

libraryDependencies += "org.apache.flink" %% "flink-scala" % "1.9.0"

libraryDependencies += "org.apache.flink" % "flink-core" % "1.9.0"

// https://mvnrepository.com/artifact/org.apache.flink/flink-runtime
libraryDependencies += "org.apache.flink" %% "flink-runtime" % "1.9.0" % Test

libraryDependencies += "org.apache.flink" %% "flink-runtime-web" % "1.9.0" % Test

libraryDependencies += "org.apache.flink" %% "flink-clients" % "1.9.0"

libraryDependencies += "log4j" % "log4j" % "1.2.17"

libraryDependencies += "org.apache.flink" %% "flink-cep" % "1.9.0"

// https://mvnrepository.com/artifact/org.apache.flink/flink-jdbc
libraryDependencies += "org.apache.flink" %% "flink-jdbc" % "1.9.0"

// https://mvnrepository.com/artifact/org.postgresql/postgresql
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.9"

// --- Flink Table API ---

// https://mvnrepository.com/artifact/org.apache.flink/flink-table-common
libraryDependencies += "org.apache.flink" % "flink-table-common" % "1.9.0" % "provided"

// https://mvnrepository.com/artifact/org.apache.flink/flink-table-api-scala
libraryDependencies += "org.apache.flink" %% "flink-table-api-scala" % "1.9.0"

// https://mvnrepository.com/artifact/org.apache.flink/flink-table-api-scala-bridge
libraryDependencies += "org.apache.flink" %% "flink-table-api-scala-bridge" % "1.9.0"

// https://mvnrepository.com/artifact/org.apache.flink/flink-table-planner-blink
libraryDependencies += "org.apache.flink" %% "flink-table-planner-blink" % "1.9.0"

// https://mvnrepository.com/artifact/org.apache.flink/flink-table-runtime-blink
libraryDependencies += "org.apache.flink" %% "flink-table-runtime-blink" % "1.9.0"

// --- END ---

// https://mvnrepository.com/artifact/com.github.mauricio/postgresql-async
libraryDependencies += "com.github.mauricio" %% "postgresql-async" % "0.2.21"
