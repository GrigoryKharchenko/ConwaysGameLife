package com.example.conwaysgamelife.service

import com.example.conwaysgamelife.gamesettings.GRID_SIZE
import com.example.conwaysgamelife.extension.isInBounds
import com.example.conwaysgamelife.extension.toIndex
import com.example.conwaysgamelife.extension.toXCoordinate
import com.example.conwaysgamelife.extension.toYCoordinate
import com.example.conwaysgamelife.model.CellModel
import kotlin.random.Random

class GamePlayService {

    private var cells = List(size = GRID_SIZE * GRID_SIZE) { CellModel() }

    fun initGrid(): List<CellModel> = cells

    fun randomEvolve(): List<CellModel> {
        cells = cells.map { cellModel ->
            cellModel.copy(isAlive = Random.nextBoolean())
        }
        return cells
    }

    fun evolveUniverse(): List<CellModel> {
        cells = cells.mapIndexed { index, cellModel ->
            cellModel.copy(isAlive = evolveCell(index, cellModel.isAlive))
        }
        return cells
    }

    private fun evolveCell(index: Int, isAlive: Boolean): Boolean {
        return when (countNeighbours(index)) {
            ZERO_NEIGHBOURS, ONE_NEIGHBOURS -> false
            TWO_NEIGHBOURS -> isAlive
            THREE_NEIGHBOURS -> true
            else -> false
        }
    }

    private fun countNeighbours(index: Int): Int {
        val neighbours = mutableListOf<CellModel>()
        neighborCoordinatesOf(index.toXCoordinate(), index.toYCoordinate())
            .filter {
                it.isInBounds()
            }.map {
                it.toIndex()
            }.forEach {
                neighbours.add(cells[it])
            }
        return neighbours.filter { it.isAlive }.size
    }

    private fun neighborCoordinatesOf(x: Int, y: Int) =
        listOf(
            Pair(x - 1, y - 1),
            Pair(x, y - 1),
            Pair(x + 1, y - 1),
            Pair(x - 1, y),
            Pair(x + 1, y),
            Pair(x - 1, y + 1),
            Pair(x, y + 1),
            Pair(x + 1, y + 1)
        )

    private companion object {
        const val ZERO_NEIGHBOURS = 0
        const val ONE_NEIGHBOURS = 1
        const val TWO_NEIGHBOURS = 2
        const val THREE_NEIGHBOURS = 3
    }
}