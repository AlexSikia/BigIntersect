package bigintersect

import scala.collection.mutable

object ExternalSorter {
    def makeSortedFile(fileManager: FileManager) = {
        print("Writing sorted blocks to disk:")
        breakIntoSortedBlocks()
        println()
        print("Merging blocks: ")
        mergeBlocks()
        println()

        def breakIntoSortedBlocks() = {
            fileManager.blockIterator
                       .map{ block => java.util.Arrays.parallelSort(block); block }
                       .zipWithIndex
                       .foreach{
                            case (sortedBlock: Array[Long], index: Int) => 
                                print(s" ${index}")
                                fileManager.writeBlock(sortedBlock, index)
                        }
        }
        def mergeBlocks() = {
            val blockHandlers: mutable.PriorityQueue[BlockHandler] =
                mutable.PriorityQueue.from((0 to fileManager.numberOfBlocks - 1).map(blockIndex => new BlockHandler(fileManager, blockIndex)))(BlockOrdering)
            val sortedBuffer: mutable.Queue[Long] = mutable.Queue()

            while (!blockHandlers.isEmpty) {
                val minBlockHandler: BlockHandler = blockHandlers.dequeue()
                
                sortedBuffer.enqueue(minBlockHandler.currentLong)

                minBlockHandler.pointToNextLong()
                if (minBlockHandler.hasNext) { blockHandlers.enqueue(minBlockHandler) }

                if (sortedBuffer.length >= fileManager.blockSize) {
                    print(".")
                    fileManager.writeToSortedFile(sortedBuffer)
                    sortedBuffer.clear()
                }
            }
            if (!sortedBuffer.isEmpty) fileManager.writeToSortedFile(sortedBuffer)
        }
    }
}
