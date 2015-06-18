import com.typesafe.tools.mima.core._
import com.typesafe.tools.mima.plugin.MimaKeys._

scodecModule := "scodec-spire"

scodecPrimaryModule
scodecPrimaryModuleJvm

contributors += Contributor("mpilquist", "Michael Pilquist")

rootPackage := "scodec.interop.spire"

libraryDependencies ++= Seq(
  "org.scodec" %% "scodec-core" % "1.8.0",
  "org.spire-math" %% "spire" % "0.10.1",
  "org.scalatest" %% "scalatest" % "2.2.0" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.0" % "test"
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
