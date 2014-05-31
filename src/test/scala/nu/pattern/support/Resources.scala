package nu.pattern.support

import scala.io.Source

object Resources {

  trait Parser[S, O] {
    def apply(v: S): O
  }

  def read[T](resource: String, delimiter: Char = ',')(implicit fromRaw: Parser[Seq[String], T]): Iterator[T] = {
    Source
      .fromInputStream(getClass.getResourceAsStream(resource))
      .getLines()
      .map(_.split(delimiter).toSeq)
      .map(fromRaw(_))
  }

}
