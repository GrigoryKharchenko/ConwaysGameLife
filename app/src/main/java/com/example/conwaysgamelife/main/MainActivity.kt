package com.example.conwaysgamelife.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.conwaysgamelife.gamesettings.GRID_SIZE
import com.example.conwaysgamelife.R
import com.example.conwaysgamelife.model.CellModel
import com.example.conwaysgamelife.ui.theme.ConwaysGameLifeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    setContent {
                        ConwaysGameLifeTheme {
                            MainScreen(
                                state = state,
                                sizeCell = getSizeCell(),
                                onStopClick = {
                                    viewModel.perform(MainViewEvent.OnStopClick)
                                },
                                onRandomClick = {
                                    viewModel.perform(MainViewEvent.OnRandomClick)
                                },
                                onPlayClick = {
                                    viewModel.perform(MainViewEvent.OnPlayClick)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    state: MainViewState,
    sizeCell: Int,
    onStopClick: () -> Unit,
    onRandomClick: () -> Unit,
    onPlayClick: () -> Unit,
) {
    Surface(
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Grid(cells = state.cells, sizeCell = sizeCell)
            Spacer(modifier = Modifier.height(16.dp))
            ButtonsSection(
                onStopClick = onStopClick,
                onRandomClick = onRandomClick,
                onPlayClick = onPlayClick
            )
        }
    }
}


@Composable
fun ButtonsSection(
    onStopClick: () -> Unit,
    onRandomClick: () -> Unit,
    onPlayClick: () -> Unit,
) {
    val expanded = remember {
        mutableStateOf(true)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = {
                expanded.value = !expanded.value
                if (expanded.value) onStopClick() else onPlayClick()
            },
            Modifier.weight(1f)
        ) {
            Text(
                if (expanded.value) {
                    stringResource(id = R.string.start_app)
                } else {
                    stringResource(id = R.string.stop_app)
                }
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            onClick = onRandomClick,
            Modifier.weight(1f)
        ) {
            Text(text = stringResource(id = R.string.random_cell))
        }
    }
}

@Composable
fun Grid(
    cells: List<CellModel>,
    sizeCell: Int
) {
    var index = 0
    Column {
        (0 until GRID_SIZE).forEach { x ->
            Row {
                (0 until GRID_SIZE).forEach { y ->
                    val cell = cells[index]
                    Cell(isAlive = cell.isAlive, sizeCell = sizeCell)
                    index++
                }
            }
        }
    }
}

@Composable
fun Cell(
    isAlive: Boolean,
    liveColor: Color = Color.White,
    deathColor: Color = Color.Black,
    sizeCell: Int
) {
    val background = if (isAlive) deathColor else liveColor
    Box(
        modifier = Modifier
            .padding(all = 1.dp)
            .size(sizeCell.dp)
            .background(background)
    )
}

@Composable
private fun getSizeCell(): Int {
    val horizontalPadding = with(LocalDensity.current) { 32.dp.toPx() }
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return ((screenWidth - horizontalPadding) / GRID_SIZE).toInt()
}
