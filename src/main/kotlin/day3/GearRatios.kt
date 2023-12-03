package day3

import java.io.File

fun main() {
    val input = File("data/day3/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
    val resultLunch = lunch(input)
    println(resultLunch)
}

data class Point(val x: Int, val y: Int)

data class PartNumber(val value: Int, val start: Point)

fun PartNumber.getNeighbors(): List<Point>{
    val n: MutableList<Point> = mutableListOf()
    listOf(
        Point(-1, -1), Point(-1, 0), Point(-1, 1),
        Point(0, -1), Point(0, 1),
        Point(1, -1), Point(1, 0), Point(1, 1),
    ).forEach {
        n.add(Point(this.start.x + it.x, this.start.y + it.y))
    }
    if (this.value > 10)  {
        n.add(Point(this.start.x - 2, start.y - 1))
        n.add(Point(this.start.x - 2, start.y))
        n.add(Point(this.start.x - 2, start.y + 1))
        n.remove(Point(this.start.x - 1, this.start.y))
    }
    if (this.value > 100)  {
        n.add(Point(this.start.x - 3, start.y - 1))
        n.add(Point(this.start.x - 3, start.y))
        n.add(Point(this.start.x - 3, start.y + 1))
        n.remove(Point(this.start.x - 2, this.start.y))
    }
    return n.toList()
}

fun parsePartNumbers(input: List<String>): List<PartNumber> {
    val width = input.first().length
    val height = input.size
    val parts: MutableList<PartNumber> = mutableListOf()
    (0 until height).forEach { y ->
        var current: Int? = null
        (0 until width).forEach { x ->
            val c = input[y][x]
            if (c.isDigit()) {
                current = if (current == null) c.digitToInt() else current!! * 10 + c.digitToInt()
            }
            else {
                if (current != null) {
                    parts.add(PartNumber(current!!, Point(x - 1, y)))
                    current = null
                }
            }
        }
        if (current != null) {
            parts.add(PartNumber(current!!, Point(width - 1, y)))
            current = null
        }
    }
    return parts.toList()
}

fun parseSymbols(input: List<String>): Set<Point> {
    val width = input.first().length
    val height = input.size
    val symbols = (0 until height).flatMap { y ->
        (0 until width).mapNotNull { x ->
            val c = input[y][x]
            if (!c.isDigit() && c != '.') Point(x, y)
            else null
        }
    }.toSet()
    return symbols
}

fun parseGears(input: List<String>): Set<Point> {
    val width = input.first().length
    val height = input.size
    val gears = (0 until height).flatMap { y ->
        (0 until width).mapNotNull { x ->
            val c = input[y][x]
            if (c == '*') Point(x, y)
            else null
        }
    }.toSet()
    return gears
}

fun breakfast(input: List<String>) : Int {
    val symbols = parseSymbols(input)
    val parts = parsePartNumbers(input)
    return parts.filter { it.getNeighbors().any { it in symbols } }.sumOf { it.value }
}

fun lunch(input: List<String>): Int {
    val gears = parseGears(input)
    val parts = parsePartNumbers(input)
    val connectedParts = gears.map { gear ->
        parts.filter { it.getNeighbors().contains(gear) }
    }
    return connectedParts.filter { it.size == 2 }.sumOf { it.first().value * it.last().value }
}