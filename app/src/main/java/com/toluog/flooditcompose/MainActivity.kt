package com.toluog.flooditcompose

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.onSizeChanged
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.toluog.flooditcompose.components.Grid
import com.toluog.flooditcompose.components.MyApp
import com.toluog.flooditcompose.components.Options
import com.toluog.flooditcompose.model.GameState
import com.toluog.flooditcompose.viewmodel.GameViewModel
import com.toluog.flooditcompose.viewmodel.GameViewModelFactory

class MainActivity : AppCompatActivity() {
    private val gameViewModel by viewModels<GameViewModel> { GameViewModelFactory(application) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                Scaffold(
                    topBar = { appBar() }
                ) {
                    GameView()
                }
            }
        }
    }

    @Composable
    fun appBar() {
        TopAppBar(
            title = { Text("Flood It!") },
            contentColor = Color.White,
            backgroundColor = MaterialTheme.colors.primaryVariant,
            actions = {
                IconButton(onClick = { gameViewModel.newGame() }) { Icon(Icons.Filled.Refresh) }
            }
        )
    }

    @Composable
    fun GameView() {
        var size by remember { mutableStateOf(IntSize.Zero) }
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().wrapContentSize(Alignment.Center)
            .padding(16.dp).onSizeChanged {
                size = it
            },
            verticalArrangement = Arrangement.Center,
        ) {
            GameContent(size = size)
        }
    }

    @Composable
    fun GameContent(size: IntSize) {
        val state = gameViewModel.gameState
        when (val nState = state.value) {
            GameState.Loading -> CircularProgressIndicator()
            GameState.Won -> {
                Text(
                        text = stringResource(id = R.string.you_won),
                        fontSize = TextUnit.Sp(32),
                        textAlign = TextAlign.Center
                )

                Button(onClick = { gameViewModel.newGame() }) {
                    Text(stringResource(id = R.string.start_new_game))
                }
            }
            GameState.Lost -> {
                Text(
                    text = stringResource(id = R.string.you_lost),
                    fontSize = TextUnit.Sp(32),
                    textAlign = TextAlign.Center
                )

                Button(onClick = { gameViewModel.newGame() }) {
                    Text(stringResource(id = R.string.start_new_game))
                }
            }
            is GameState.Playing -> {
                Grid(
                    state = nState,
                    size = size
                )

                Options(colorCount = nState.colorCount, colorClicked = {
                    gameViewModel.colorClicked(it)
                })
            }
        }.exhaustive
    }
}
