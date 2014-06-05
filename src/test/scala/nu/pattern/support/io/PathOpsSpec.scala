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

  "Paths" should {
    "delete immediately when onShutdown is false" in {
      val path = generateFiles
      path.delete()
      Files.exists(path) must beFalse
    }

    "delete eventually when onShutdown is true" in {
      val path = generateFiles
      path.delete(onShutdown = true)
      Files.exists(path) must beTrue
    }
  }
}
