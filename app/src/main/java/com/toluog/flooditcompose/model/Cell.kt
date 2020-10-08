package com.toluog.flooditcompose.model

data class Cell(val i: Int, val j: Int, val color: CellColor, val captured: Boolean)

fun Cell.capture(newColor: CellColor) = Cell(i, j, newColor, true)