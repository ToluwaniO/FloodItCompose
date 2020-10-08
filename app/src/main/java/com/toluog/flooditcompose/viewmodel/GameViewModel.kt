package com.toluog.flooditcompose.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.toluog.flooditcompose.Game
import com.toluog.flooditcompose.model.CellColor
import com.toluog.flooditcompose.model.GameState

const val GAME_SIZE = 12
const val MAX_STEPS = 20
const val COLOR_COUNT = 4

class GameViewModel(app: Application): AndroidViewModel(app) {

    companion object {
        private const val BEST_SCORE_KEY = "best"
        const val BEST_SCORE_DEFAULT = 0
    }
    private val sharedPref = app.getSharedPreferences(BEST_SCORE_KEY, Context.MODE_PRIVATE)
    private val game = Game(boardHeight = GAME_SIZE, boardWidth = GAME_SIZE, maxSteps = MAX_STEPS, colorCount = COLOR_COUNT)
    var gameState = mutableStateOf<GameState>(GameState.Loading)
        private set

    init {
        val best = sharedPref.getInt(BEST_SCORE_KEY, BEST_SCORE_DEFAULT)
        gameState.value = GameState.Playing(game.board, game.steps, MAX_STEPS, game.colorCount, best)
    }

    fun colorClicked(color: CellColor) {
        game.colorClicked(color)
        updateState()
    }

    fun newGame() {
        game.reset()
        updateState()
    }

    private fun updateState() {
        val best = sharedPref.getInt(BEST_SCORE_KEY, 0)
        when {
            game.gameWon() -> {
                if (best == BEST_SCORE_DEFAULT || game.steps < best) {
                    with(sharedPref.edit()) {
                        putInt(BEST_SCORE_KEY, game.steps)
                        apply()
                    }
                }
                gameState.value = GameState.Won
            }
            game.gameLost() -> {
                gameState.value = GameState.Lost
            }
            else -> {
                gameState.value = GameState.Playing(game.board, game.steps, MAX_STEPS, game.colorCount, best)
            }
        }
    }
}