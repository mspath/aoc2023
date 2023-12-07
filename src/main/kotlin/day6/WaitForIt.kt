package day6

import java.io.File

fun main() {
    val input = File("data/day6/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
    val resultLunch = lunch(input)
    println(resultLunch)
}

fun winners(race: Pair<Int, Int>): List<Int> {
    return (0..race.first).map { t ->
        val length = race.first - t
        val speed = t
        speed * length
    }.filter { it > race.second }
}

fun getDistance(button: Long, time: Long) = button * (time - button)

fun breakfast(input: List<String>): Int {
    val times = input.first().substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    val distances = input.last().substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    val zip = times.zip(distances)
    val winners = zip.map { winners(it) }.map { it.size }
    return winners.reduce { acc, i -> acc * i }
}

fun lunch(input: List<String>): Long {
    val time = input.first().substringAfter(":").filter { it.isDigit() }.toLong()
    val distance = input.last().substringAfter(":").filter { it.isDigit() }.toLong()
    var start = 0L
    var end = 0L
    run starting@{
        (0..time).forEach {
            if (getDistance(it, time) > distance) {
                start = it
                return@starting
            }
        }
    }
    run ending@{
        (time downTo 0).forEach {
            if (getDistance(it, time) > distance) {
                end = it
                return@ending
            }
        }
    }
    return end - start + 1
}