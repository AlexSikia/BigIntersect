package bigintersect

import scala.annotation.tailrec
import java.io.BufferedWriter

object Intersecter {
    def interesect(sortedNumberStreams: (Iterator[Long], Iterator[Long]), outputWriter: BufferedWriter): Unit = {
        val (stream1, stream2): (Iterator[Long], Iterator[Long]) = (sortedNumberStreams._1, sortedNumberStreams._2)

        @tailrec
        def interesectRec(value1: Long, value2: Long): Unit = {
            if (value1 == value2) { outputWriter.write(value1.toString()); outputWriter.newLine() }

            if ((!stream1.hasNext && (value1 <= value2)) ||
                (!stream2.hasNext && (value2 <= value1))) return
            else if (value1 < value2) interesectRec(stream1.next(), value2)
            else if (value1 > value2) interesectRec(value1, stream2.next())
            else interesectRec(stream1.next(), stream2.next())
        }

        interesectRec(stream1.next(), stream2.next())
        outputWriter.close()
        println(s"Wrote output to `./intersection`")
    }
}
