name := "akka-smart-scheduler"
version := "1.0.0"
scalaVersion := "2.11.7"
topLevelDirectory := Some(name.value)

lazy val root = (project in file(".")).enablePlugins(PlayJava, BuildInfoPlugin).settings(
  buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
  buildInfoPackage := "utils"
)

libraryDependencies ++= Seq(
  
)

routesGenerator := InjectedRoutesGenerator

// Don't include API documentation when packaging:
doc in Compile <<= target.map(_ / "none")