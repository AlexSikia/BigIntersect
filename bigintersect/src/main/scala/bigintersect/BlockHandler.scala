package bigintersect


class BlockHandler(fileManager: FileManager, blockIndex: Int) {
    val blocksliceIterator: Iterator[IndexedSeq[Long]] = fileManager.loadBlock(blockIndex)
    var numbersPointer: Int = 0
    var blockslice: IndexedSeq[Long] = blocksliceIterator.next()
    def pointToNextLong(): Unit = {
        numbersPointer += 1
        if (!hasNext) {
            if (blocksliceIterator.hasNext) blockslice = blocksliceIterator.next()
            else blockslice = IndexedSeq()
            numbersPointer = 0
        }
    }
    def getblockIndex = blockIndex
    def hasNext: Boolean = numbersPointer < blockslice.length
    def currentLong: Long = blockslice(numbersPointer)
}

object BlockOrdering extends Ordering[BlockHandler] {
  def compare(a:BlockHandler, b:BlockHandler) = a.currentLong compare b.currentLong
}