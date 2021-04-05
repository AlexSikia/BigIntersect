package bigintersect

object BigIntersect extends App {
    if (args.length != 2 || args.exists(file => !Utils.fileExists(file))) {
        println("[ERROR] Please pass the relative paths to the two source file as parameters to the App call.")
        println("Example: 'scala BigIntersect ./numbers.txt ../some_diretory/other_numbers.txt'")
        throw new WrongInputError("Arguments did not match expected format")
    }
    else {
        Utils.setupFilesystem()
        if (args.exists(file => Utils.isEmpty(file))) {
            Utils.getIntersectionWriter.write("")
        }
        else {
            args.zipWithIndex.foreach{case (file: String, index: Int) => {
                val fileManager = new FileManager(file, index)
                ExternalSorter.makeSortedFile(fileManager)
            }}
            Intersecter.interesect(Utils.getSortedStreams, Utils.getIntersectionWriter)
        }
        Utils.cleanFilesystem()
    }
}

case class WrongInputError(msg: String) extends Error