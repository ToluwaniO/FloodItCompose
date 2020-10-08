package com.toluog.flooditcompose

import com.toluog.flooditcompose.model.Cell
import com.toluog.flooditcompose.model.CellColor
import com.toluog.flooditcompose.model.capture
import java.util.*
import kotlin.random.Random

class Game (val colorCount: Int, private val boardHeight: Int, private val boardWidth: Int, private val maxSteps: Int) {
    val board = Array<Array<Cell?>>(boardHeight){ Array(boardWidth) {null} }
    private var captured = 1
    var steps = 0
        private set
    private lateinit var lastColor: CellColor
    private val nbs = arrayOf(arrayOf(1,0), arrayOf(-1,0), arrayOf(0,1), arrayOf(0,-1))

    init {
        initBoard()
    }

    private fun initBoard() {
        for (i in 0 until boardHeight) {
            for (j in 0 until boardWidth) {
                val cell = Cell(i, j, color = CellColor.from(Random.nextInt(colorCount)), captured = i == 0 && j==0)
                board[i][j] = cell
                if (i == 0 && j == 0) {
                    lastColor = cell.color
                }
            }
        }
        colorizeNeighbors(lastColor)
    }

    fun reset() {
        steps = 0
        captured = 1
        initBoard()
    }

    fun colorClicked(color: CellColor) {
        if (gameFinished() || lastColor == color || color.value >= colorCount) return
        lastColor = color
        steps++
        colorizeNeighbors(color)
    }

    fun gameFinished(): Boolean {
        return gameWon() || steps == maxSteps
    }

    fun gameWon(): Boolean {
        return captured == boardHeight*boardWidth
    }

    fun gameLost(): Boolean {
        return !gameWon() && steps >= maxSteps
    }

    private fun colorizeNeighbors(color: CellColor) {
        val stack = LinkedList<Cell?>()
        val visited = Array(boardHeight){ Array(boardWidth) {false} }
        visited[0][0] = true
        stack.add(board[0][0])

        while (stack.isNotEmpty()) {
            val cell = stack.removeLast() ?: continue
            if (cell.captured || cell.color == color) {
                if (!cell.captured) captured++
                board[cell.i][cell.j] = cell.capture(color)
            } else {
                continue
            }

            for(pos in nbs) {
                val row = cell.i + pos[0]
                val col = cell.j + pos[1]
                if (row < 0 || row >= boardHeight || col < 0 || col >= boardWidth) {
                    continue
                }
                if (!visited[row][col]) {
                    stack.addLast(board[row][col])
                    visited[row][col] = true
                }
            }
        }
    }



}