package day14

import java.io.File

fun main() {
    val input = File("data/day14/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
}

fun Set<Point>.result(height: Int): Int =
    this.sumOf {
        height - it.y
    }

data class Point(val x: Int, val y: Int)

fun Set<Point>.tiltNorth(rocks: MutableSet<Point>) {
    for (y in 1..rocks.maxOf { it.y }) {
        for (x in 0..rocks.maxOf { it.x }) {
            if (rocks.contains(Point(x, y))) {
                var backtrack = 1
                while (!rocks.contains(Point(x, y - backtrack)) && !this.contains(Point(x, y - backtrack)) && y - backtrack >= 0) {
                    rocks.remove(Point(x, y - backtrack + 1))
                    rocks.add(Point(x, y - backtrack))
                    backtrack += 1
                }
            }
        }
    }
}

fun Set<Point>.graph() {
    for (y in 0..maxOf { it.y }) {
        for (x in 0..maxOf { it.x }) {
            if (this.contains(Point(x, y))) print('O')
            else print('.')
        }
        print('\n')
    }
}

fun breakfast(input: List<String>) : Int {
    val cubes = input.flatMapIndexed { y: Int, line: String ->
        line.mapIndexedNotNull { x, c ->
            when (c) {
                '#' -> Point(x, y)
                else -> null
            }
        }
    }.toSet()
    val rounded = input.flatMapIndexed { y: Int, line: String ->
        line.mapIndexedNotNull { x, c ->
            when (c) {
                'O' -> Point(x, y)
                else -> null
            }
        }
    }.toMutableSet()

    return rounded.result(input.size)
}