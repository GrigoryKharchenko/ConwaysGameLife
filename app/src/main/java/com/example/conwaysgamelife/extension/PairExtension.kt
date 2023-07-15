package com.example.conwaysgamelife.extension

import com.example.conwaysgamelife.gamesettings.GRID_SIZE

fun Pair<Int, Int>.isInBounds() =
    !((first < 0)
        .or(first >= GRID_SIZE)
        .or(second < 0).or(second >= GRID_SIZE))

fun Pair<Int, Int>.toIndex() = second * GRID_SIZE + first
