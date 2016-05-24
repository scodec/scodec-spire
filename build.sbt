import com.typesafe.tools.mima.core._
import com.typesafe.tools.mima.plugin.MimaKeys._

scodecModule := "scodec-spire"

scodecPrimaryModule
scodecPrimaryModuleJvm

crossScalaVersions := crossScalaVersions.value.filter { _.startsWith("2.11.") }

contributors += Contributor("mpilquist", "Michael Pilquist")

rootPackage := "scodec.interop.spire"

libraryDependencies ++= Seq(
  "org.scodec" %% "scodec-core" % "1.10.0",
  "org.spire-math" %% "spire" % "0.11.0",
  "org.scalatest" %% "scalatest" % "3.0.0-M16-SNAP4" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.1" % "test"
)

OsgiKeys.exportPackage := Seq("scodec.interop.spire.*;version=${Bundle-Version}")
OsgiKeys.importPackage := Seq(
  """scodec.*;version="$<range;[==,=+);$<@>>"""",
  """spire.*;version="$<range;[==,=+);$<@>>"""",
  """scala.*;version="$<range;[==,=+);$<@>>"""",
  "*"
)

binaryIssueFilters := Seq(
)
