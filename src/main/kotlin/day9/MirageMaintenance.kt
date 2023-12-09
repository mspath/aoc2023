package day9

import java.io.File

fun main() {
    val input = File("data/day9/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
    val resultLunch = lunch(input)
    println(resultLunch)
}

fun List<Int>.reduce(): List<Int> = this.windowed(2).map { it.last() - it.first() }

fun List<Int>.predict(): Int {
    if (this.isEmpty()) return 0
    if (this.all { it == 0 }) return 0
    return this.last() + this.reduce().predict()
}

fun List<Int>.predictBackwards(): Int {
    if (this.isEmpty()) return 0
    if (this.all { it == 0 }) return 0
    return this.first() - this.reduce().predictBackwards()
}

fun breakfast(input: List<String>) : Int {
    val histories = input.map { it.split(" ").map { it.toInt() } }
    val results = histories.map {
        it.predict()
    }
    return results.sum()
}

fun lunch(input: List<String>) : Int {
    val histories = input.map { it.split(" ").map { it.toInt() } }
    val results = histories.map {
        it.predictBackwards()
    }
    return results.sum()
}