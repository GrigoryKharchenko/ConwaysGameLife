package com.example.conwaysgamelife.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.conwaysgamelife.gamesettings.TIME_ONE_MOVE
import com.example.conwaysgamelife.service.GamePlayService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _state = MutableStateFlow(MainViewState())
    val state = _state.asStateFlow()

    private val gameRules by lazy {
        GamePlayService()
    }

    private var coroutineJob: Job? = null

    init {
        initGrid()
    }

    fun perform(event: MainViewEvent) {
        when (event) {
            MainViewEvent.OnPlayClick -> onPlayClick()
            MainViewEvent.OnStopClick -> onStopClick()
            MainViewEvent.OnRandomClick -> onRandomClick()
        }
    }

    private fun initGrid() {
        _state.value = MainViewState(cells = gameRules.initGrid())
    }

    private fun onRandomClick() {
        _state.update { state ->
            state.copy(cells = gameRules.randomEvolve())
        }
    }

    private fun onStopClick() {
        coroutineJob?.cancel()
    }

    private fun onPlayClick() {
        coroutineJob = viewModelScope.launch {
            while (true) {
                delay(TIME_ONE_MOVE)
                _state.update { state ->
                    state.copy(cells = gameRules.evolveUniverse())
                }
            }
        }
    }
}