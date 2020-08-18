package board

import java.lang.IllegalArgumentException

open class SquareBoardImpl(override val width: Int) : SquareBoard {

    var cells: Array<Array<Cell>> = createBoard(width)

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return if (i > width || j > width || i == 0 || j == 0) {
            null
        } else {
            getCell(i, j)
        }
    }

    override fun getCell(i: Int, j: Int): Cell = cells[i - 1][j - 1]

    override fun getAllCells(): Collection<Cell> = cells.flatten()

    override fun getRow(i: Int, jRange: Int
                        Progression): List<Cell> {
        val listRows = mutableListOf<Cell>()
        jRange.map { j: Int ->
            if (j <= width) {
                listRows.add(getCell(i, j))
            }
        }
        return listRows
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val listColumns = mutableListOf<Cell>()
        iRange.map { i: Int ->
            if (i <= width) {
                listColumns.add(getCell(i, j))
            }
        }
        return listColumns
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            Direction.UP -> getCellOrNull(i - 1, j)
            Direction.DOWN -> getCellOrNull(i + 1, j)
            Direction.LEFT -> getCellOrNull(i, j - 1)
            Direction.RIGHT -> getCellOrNull(i, j + 1)
        }
    }

}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)

class GameBoardImpl<T>(override val width: Int) : SquareBoardImpl(width), GameBoard<T> {

    private val gameBoardCells = mutableMapOf<Cell, T?>()

    init {
        cells = createBoard(width)
        cells.flatten().map { gameBoardCells[it] = null }
    }

    override fun get(cell: Cell): T? = gameBoardCells[cell]

    override fun set(cell: Cell, value: T?) {
        gameBoardCells[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return gameBoardCells.filterValues { predicate.invoke(it) }.keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return gameBoardCells.filterValues { predicate.invoke(it) }.keys.first()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return gameBoardCells.any { predicate.invoke(it.value) }
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return gameBoardCells.all { predicate.invoke(it.value) }
    }

}

private fun createBoard(width: Int): Array<Array<Cell>> {
    var board = arrayOf<Array<Cell>>()

    for (i in 1..width) {
        var array = arrayOf<Cell>()
        for (j in 1..width) {
            array += Cell(i, j)
        }
        board += array
    }
    return board
}


fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

