package com.example.conwaysgamelife.extension

import com.example.conwaysgamelife.gamesettings.GRID_SIZE

fun Int.toXCoordinate() = this % GRID_SIZE

fun Int.toYCoordinate() = this / GRID_SIZE