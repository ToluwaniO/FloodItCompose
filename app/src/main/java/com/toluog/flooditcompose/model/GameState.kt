package com.toluog.flooditcompose.model

sealed class GameState {
    object Loading: GameState()
    object Won: GameState()
    object Lost: GameState()
    data class Playing(val board: Array<Array<Cell?>>, val steps: Int, val maxSteps: Int,
                       val colorCount: Int, val bestSteps: Int): GameState()
}