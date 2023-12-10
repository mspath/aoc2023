package day10

import java.io.File

fun main() {
    val input = File("data/day10/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
}

data class Position(val x: Int, val y: Int, val type: Char)

fun Position.neighbors(): List<Pair<Int, Int>> = when(this.type) {
    '|' -> listOf(Pair(x, y - 1), Pair(x, y + 1))
    '-' -> listOf(Pair(x - 1, y), Pair(x + 1, y))
    'L' -> listOf(Pair(x, y - 1), Pair(x + 1, y))
    'J' -> listOf(Pair(x - 1, y), Pair(x, y - 1))
    '7' -> listOf(Pair(x - 1, y), Pair(x, y + 1))
    'F' -> listOf(Pair(x, y + 1), Pair(x + 1, y))
    // I'm cheating a bit..
    // 'S' -> listOf(Pair(x, y + 1), Pair(x + 1, y)) // sample
    'S' -> listOf(Pair(x, y - 1), Pair(x, y + 1)) // input
    '.' -> emptyList()
    else -> emptyList()
}

fun breakfast(input: List<String>) : Int {
    val map = input.flatMapIndexed { y: Int, line: String ->
        line.mapIndexed { x, c -> Position(x, y, c) }
    }
    val lookup = map.associateBy { Pair(it.x, it.y) }
    val start = map.first { it.type == 'S' }
    val maze: MutableList<Pair<Int, Int>> = mutableListOf()
    maze.add(Pair(start.x, start.y))
    var done = false
    var distance = 0
    var current = start
    while (!done) {
        distance += 1
        val next = current.neighbors().filter { !maze.contains(it) }
        if (next.isEmpty()) done = true
        if (!done) {
            current = lookup[next.first()]!!
            maze.add(next.first())
        }
    }
    return distance / 2
}

fun List<Pair<Int, Int>>.graph() {
    repeat(this.maxOf { it.second }) { y ->
        repeat(this.maxOf { it.first }) { x ->
            if (this.contains(Pair(x, y))) print("*") else print('.')
        }
        print("\n")
    }
}
