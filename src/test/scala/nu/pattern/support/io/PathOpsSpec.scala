package nu.pattern.support.io

import org.specs2.mutable._
import java.nio.file.Files

class PathOpsSpec extends Specification {
  private def generateFiles = {
    val path = Files.createTempDirectory(getClass.getName)

    Files.createDirectory(path.resolve("subpath"))
    Files.createFile(path.resolve("subfile"))

    path
  }

  val path0 = generateFiles

  s"""Path "$path0"""" should {
    "exist and delete eventually when onShutdown is true" in {
      Files.exists(path1) must beTrue
      path0.delete(onShutdown = true)
      Files.exists(path0) must beTrue
    }
  }

  val path1 = generateFiles

  s"""Path "$path1"""" should {
    "exist and delete immediately when onShutdown is false" in {
      Files.exists(path1) must beTrue
      path1.delete()
      Files.exists(path1) must beFalse
    }
  }
}
