package games.gameOfFifteen

import board.*
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game = GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {

    private val cells = mutableMapOf<Cell, Int?>()

    override fun initialize() {
        val boardCells = createSquareBoard(4).getAllCells()
        boardCells.mapIndexed { index, cell ->
            if (index < initializer.initialPermutation.size) {
                cells[cell] = initializer.initialPermutation[index]
            } else {
                cells[cell] = null
            }
        }
    }

    override fun canMove(): Boolean {
        return true
    }

    override fun hasWon(): Boolean {
        val movedValues = cells.values.toList().filterNotNull()
        val originalValues = (1..15).toList()
        if (movedValues.size == originalValues.size) {
            movedValues.forEachIndexed { index, i ->
                if (i != originalValues[index]) {
                    return false
                }
            }
        }
        return true
    }

    override fun processMove(direction: Direction) {
        val emptyCell = cells.filterValues { it == null }.keys.first()
        when (direction) {
            Direction.RIGHT -> {
                val anotherCell = Cell(emptyCell.i, emptyCell.j - 1)
                if (anotherCell.j > 0) {
                    cells[emptyCell] = cells[anotherCell]
                    cells[anotherCell] = null
                }
            }
            Direction.LEFT -> {
                val anotherCell = Cell(emptyCell.i, emptyCell.j + 1)
                if (anotherCell.j < 5) {
                    cells[emptyCell] = cells[anotherCell]
                    cells[anotherCell] = null
                }
            }
            Direction.UP -> {
                val anotherCell = Cell(emptyCell.i + 1, emptyCell.j)
                if (anotherCell.i < 5) {
                    cells[emptyCell] = cells[anotherCell]
                    cells[anotherCell] = null
                }
            }
            Direction.DOWN -> {
                val anotherCell = Cell(emptyCell.i - 1, emptyCell.j)
                if (anotherCell.i > 0) {
                    cells[emptyCell] = cells[anotherCell]
                    cells[anotherCell] = null
                }
            }
        }
    }

    override fun get(i: Int, j: Int): Int? {
        return cells[Cell(i, j)]
    }

}