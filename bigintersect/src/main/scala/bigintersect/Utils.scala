package bigintersect

import scala.io.Source
import java.io.File
import java.nio.file.{Paths, Files}
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.File.separator

object Utils {
    def setupFilesystem(): Unit = {
        deleteRecursively(s".${separator}tmp${separator}")
        deleteRecursively(s".${separator}intersection")
        Files.createDirectory(Paths.get(s".${separator}tmp"))
    }

    def cleanFilesystem(): Unit = {
        deleteRecursively(s".${separator}tmp${separator}")
    }

    private def deleteRecursively(path: String): Unit = {
        def deleteRecursivelyRec(file: File): Unit = {
            if (file.isDirectory) {
                file.listFiles.foreach(deleteRecursivelyRec)
            }
            if (file.exists && !file.delete) {
                throw new Exception(s"Unable to delete ${file.getAbsolutePath}")
            }
        }
        deleteRecursivelyRec(new File(path))
    }

    def getSortedStreams = (Source.fromFile(s".${separator}tmp${separator}0${separator}sorted").getLines.map(_.toLong),
                            Source.fromFile(s".${separator}tmp${separator}1${separator}sorted").getLines.map(_.toLong))

    def getIntersectionWriter: BufferedWriter = new BufferedWriter(new FileWriter(s".${separator}intersection", true))

    def isEmpty(filePath: String): Boolean = Files.lines(Paths.get(filePath)).count == 0
    def fileExists(filePath: String): Boolean = Files.exists(Paths.get(filePath))
}