package bigintersect

import scala.io.Source
import java.io.{BufferedWriter, FileWriter}
import java.io.File.separator
import java.nio.file.{Paths, Files}

class FileManager(sourceFilePath: String, fileIndex: Int) {
    val numOfLongs: Long = Files.lines(Paths.get(sourceFilePath)).count
    val runtime = Runtime.getRuntime
    val maxFreeMemory: Long = (runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory())
    val blockSize: Int = Math.ceil((maxFreeMemory / 64) / 2).toInt // Divide by 2 to leave some memory available

    val numberOfBlocks: Int = Math.ceil(numOfLongs.toDouble / blockSize).toInt
    println(s"Source file contains ${numOfLongs} Longs. They will fit into ${numberOfBlocks} blocks.")

    val blocksliceSize: Int = Math.ceil(blockSize / numberOfBlocks).toInt

    Files.createDirectory(Paths.get(s".${separator}tmp${separator}${fileIndex}${separator}"))

    val blockIterator = Source.fromFile(sourceFilePath).getLines.map(_.toLong).grouped(blockSize).map(_.toArray)

    def write(lines: Iterable[Long], fileName: String, append: Boolean = false): Unit = {
        val filepath = s".${separator}tmp${separator}${fileName}"
        val writer: BufferedWriter = new BufferedWriter(new FileWriter(filepath, append))
        lines.foreach(line => { writer.write(line.toString()); writer.newLine() })
        writer.close()
    }

    def writeBlock(blockNumbers: Seq[Long], blockIndex: Int) = {
        write(blockNumbers, s"${fileIndex}${separator}block_${blockIndex}")
    }

    def loadBlock(blockIndex: Int) = Source.fromFile(s".${separator}tmp${separator}${fileIndex}${separator}block_${blockIndex}")
                                           .getLines.map(_.toLong).grouped(blocksliceSize).map(_.toIndexedSeq)

    def writeToSortedFile(lines: Iterable[Long]) = {
        if (!lines.isEmpty) write(lines, s"${fileIndex}${separator}sorted", true)
    }

    def getFileIndex: Int = fileIndex
}