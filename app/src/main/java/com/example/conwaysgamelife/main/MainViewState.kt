package com.example.conwaysgamelife.main

import com.example.conwaysgamelife.model.CellModel

data class MainViewState(
    val cells: List<CellModel> = emptyList()
)