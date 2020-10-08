package com.toluog.flooditcompose.components

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.toluog.flooditcompose.R
import com.toluog.flooditcompose.exhaustive
import com.toluog.flooditcompose.model.Cell
import com.toluog.flooditcompose.model.CellColor
import com.toluog.flooditcompose.model.GameState
import com.toluog.flooditcompose.viewmodel.GameViewModel
import java.util.*
import kotlin.math.min

@Composable
fun Grid(state: GameState.Playing, size: IntSize) {
    val tileSizePx = (min(size.height, size.width)/state.board.size).toFloat().coerceAtLeast(0f)
    val tileSizeDp = Dp(tileSizePx / DensityAmbient.current.density)

    drawScoreSection(state = state)

    for (row in state.board.indices) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
               horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (col in state.board.indices) {
                    val cell = state.board[row][col] ?: continue
                    drawCell(cell = cell, size = tileSizeDp)
                }
            }
        }
    }
}

@Composable
fun Options(colorCount: Int, colorClicked: (CellColor) -> Unit) {
    val circleSize = 80.dp
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        for (i in 0 until colorCount) {
            Box(
                modifier = Modifier.preferredSize(circleSize).padding(16.dp).drawBehind {
                    drawCircle(
                        color = getColor(CellColor.from(i))
                    )
                }.clickable(onClick = {
                    colorClicked(CellColor.from(i))
                })
            )
        }
    }
}

@Composable
fun drawCell(cell: Cell, size: Dp) {
    Box(
        modifier = Modifier.preferredSize(size = size ).drawBehind {
            drawRect(
                color = getColor(cell.color)
            )
        }
    )
}

@Composable
fun drawScoreSection(state: GameState.Playing) {
    return Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
    ) {
        Column() {
            Text(
                    text = stringResource(id = R.string.steps).toUpperCase(Locale.getDefault()),
                    fontSize = TextUnit.Sp(16),
                    fontWeight = FontWeight.Bold
            )
            Text(
                    text = stringResource(id = R.string.steps_count, state.steps, state.maxSteps),
                    fontSize = TextUnit.Sp(16)
            )
        }

        Column() {
            Text(
                    text = stringResource(id = R.string.best).toUpperCase(Locale.getDefault()),
                    fontSize = TextUnit.Sp(16),
                    fontWeight = FontWeight.Bold
            )
            if (state.bestSteps == GameViewModel.BEST_SCORE_DEFAULT) {
                Text(
                        text = "-",
                        fontSize = TextUnit.Sp(16)
                )
            } else {
                Text(
                        text = stringResource(id = R.string.steps_count, state.bestSteps, state.maxSteps),
                        fontSize = TextUnit.Sp(16)
                )
            }
        }
    }
}

fun getColor(color: CellColor): Color {
    return when (color) {
        CellColor.RED -> Color.Red
        CellColor.GREEN -> Color.Green
        CellColor.BLUE -> Color.Blue
        CellColor.YELLOW -> Color.Yellow
        CellColor.PURPLE -> Color.Yellow
        CellColor.ORANGE -> Color.White
    }.exhaustive
}