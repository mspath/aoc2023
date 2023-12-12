package day11

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = File("data/day11/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
    val resultLunch = lunch(input)
    println(resultLunch)
}

data class Point(val x: Long, val y: Long)

fun Point.distance(other: Point): Long {
    val heightDiff = max(this.y, other.y) - min(this.y, other.y)
    val widthDiff = max(this.x, other.x) - min(this.x, other.x)
    return heightDiff + widthDiff
}

fun Set<Point>.graph() {
    for (row in 0 .. this.maxOf { it.y }) {
        for (col in 0 .. this.maxOf { it.x }) {
            if (this.contains(Point(col, row))) print('#')
            else print('.')
        }
        print('\n')
    }
}

fun calculateDistance(galaxy: MutableSet<Point>, expandingTo: Long): Long {
    val width = galaxy.maxOf { it.x }
    val height = galaxy.maxOf { it.y }
    val rowsToExpand: MutableList<Long> = mutableListOf()
    for (row in 0L until height) {
        if (galaxy.none { it.y == row}) {
            rowsToExpand.add(row)
        }
    }
    val colsToExpand: MutableList<Long> = mutableListOf()
    for (column in 0L until width) {
        if (galaxy.none { it.x == column}) {
            colsToExpand.add(column)
        }
    }
    rowsToExpand.reversed().forEach {row ->
        val pointsToShift = galaxy.filter { it.y > row }
        pointsToShift.forEach {
            galaxy.remove(it)
            galaxy.add(it.copy(y = it.y + expandingTo - 1))
        }
    }
    colsToExpand.reversed().forEach {col ->
        val pointsToShift = galaxy.filter { it.x > col }
        pointsToShift.forEach {
            galaxy.remove(it)
            galaxy.add(it.copy(x = it.x + expandingTo - 1))
        }
    }
    val distances = galaxy.toList().sumOf { p ->
        galaxy.sumOf { it.distance(p) }
    }
    return distances / 2
}

fun breakfast(input: List<String>) : Long {
    val galaxy = input.flatMapIndexed { y, row: String ->
        row.mapIndexedNotNull() { x, c ->
            when(c) {
                '#' -> Point(x.toLong(), y.toLong())
                else -> null
            }
        }
    }.toMutableSet()
    return calculateDistance(galaxy, 2L)
}

fun lunch(input: List<String>) : Long {
    val galaxy = input.flatMapIndexed { y, row: String ->
        row.mapIndexedNotNull() { x, c ->
            when(c) {
                '#' -> Point(x.toLong(), y.toLong())
                else -> null
            }
        }
    }.toMutableSet()
    return calculateDistance(galaxy, 1_000_000L)
}