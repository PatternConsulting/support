package nu.pattern.support

import _root_.java.io.IOException
import _root_.java.nio.file.attribute.BasicFileAttributes
import _root_.java.nio.file.{FileVisitResult, Files, Path, SimpleFileVisitor}

package object io {

  implicit class PathOps(p: Path) {
    private def recursiveDelete = Files.walkFileTree(p, new SimpleFileVisitor[Path] {
      override def postVisitDirectory(path: Path, e: IOException): FileVisitResult = {
        Files.deleteIfExists(path)
        super.postVisitDirectory(path, e)
      }

      override def visitFile(file: Path, attributes: BasicFileAttributes): FileVisitResult = {
        Files.deleteIfExists(file)
        super.visitFile(file, attributes)
      }
    })

    def delete(onShutdown: Boolean = false): Path = {
      if (onShutdown) {
        Runtime.getRuntime.addShutdownHook(new Thread {
          override def run() {
            recursiveDelete
          }
        })
      } else {
        recursiveDelete
      }

      p
    }
  }

}
