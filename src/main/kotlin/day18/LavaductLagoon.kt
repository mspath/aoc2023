package day18

import java.io.File

fun main() {
    val input = File("data/day18/sample.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
}

data class Point(val x: Int, val y: Int)

fun Point.inside(horizontals: List<List<Point>>, verticals: List<List<Point>>): Boolean {
    if (horizontals.any { horizontal -> horizontal.contains(this) }) return true
    if (verticals.any { vertical -> vertical.contains(this) }) return true
    val max = verticals.maxOf {
        it.first().x
    }
    val beam = (this.x..max).map { x -> Point(x, this.y) }
    val crosses = verticals.filter { vertical ->
        beam.intersect(vertical.toSet()).isNotEmpty()
    }.size
    return crosses % 2 == 1
}

fun breakfast(input: List<String>) : Int {

    val instructions = input.map {
        val (direction, amount, _) = it.split(" ")
        Pair(direction, amount.toInt())
    }

    val horizontals: MutableList<List<Point>> = mutableListOf()
    val verticals: MutableList<List<Point>> = mutableListOf()

    var position = Point(0, 0)

    instructions.forEach { instruction ->
        when(instruction.first) {
            "R" -> {
                val end = Point(position.x + instruction.second, position.y)
                val points = (position.x..end.x).map { x -> Point(x, position.y) }
                horizontals.add(points)
                position = end
            }
            "L" -> {
                val end = Point(position.x - instruction.second, position.y)
                val points = (end.x..position.x).map { x -> Point(x, position.y) }
                horizontals.add(points)
                position = end
            }
            "D" -> {
                val end = Point(position.x, position.y + instruction.second)
                val points = (position.y..end.y).map { y -> Point(position.x, y) }
                verticals.add(points)
                position = end
            }
            "U" -> {
                val end = Point(position.x, position.y - instruction.second)
                val points = (end.y..position.y).map { y -> Point(position.x, y) }
                verticals.add(points)
                position = end
            }
            else -> {}
        }
    }

    val minX = verticals.minOf { it.first().x }
    val maxX = verticals.maxOf { it.first().x }
    val minY = horizontals.minOf { it.first().y }
    val maxY = horizontals.maxOf { it.first().y }

    val seen: MutableSet<Point> = mutableSetOf()

    var result = 0

    for (x in minX..maxX) {
        for (y in minY..maxY) {
            val point = Point(x, y)
            if (!seen.contains(point) && point.inside(horizontals, verticals)) result += 1
            seen.add(point)
        }
    }

    // 60379 is too high
    return result
}