package day21

import java.io.File
import java.util.*
import kotlin.math.abs

fun main() {
    val input = File("data/day21/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
}

data class Point(val x: Int, val y: Int)

fun Point.possible(other: Point) = abs(other.x % 2) + abs(other.y % 2) != 1

fun Point.neighbors() = listOf(
    Point(this.x - 1, this.y), Point(this.x + 1, this.y), Point(this.x, this.y - 1), Point(this.x, this.y + 1)
)

fun breakfast(input: List<String>) : Int {

    val sRow = input.indexOfFirst {
        it.contains("S")
    }
    val sCol = input[sRow].indexOf("S")

    val points = input.flatMapIndexed { row: Int, line: String ->
        line.mapIndexedNotNull { col: Int, c ->
            if (c == '#') Point(col - sCol, row - sRow)
            else null
        }
    }

    val start = Point(0, 0)
    val seen: MutableSet<Point> = mutableSetOf()
    val queue: Queue<Point> = LinkedList()
    queue.add(start)

    val steps = 64 + 1

    repeat(steps) {
        val next: MutableSet<Point> = mutableSetOf()
        while (queue.isNotEmpty()) {
            val point = queue.remove()
            seen.add(point)
            val neighbors = point.neighbors().filterNot { points.contains(it) }.filterNot { seen.contains(it) }
            next.addAll(neighbors)
        }
        queue.addAll(next)
    }

    val result = seen.filter { start.possible(it) }
    println(result)
    return result.size
}