package day2

import java.io.File

fun main() {
    val input = File("data/day2/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
    val resultLunch = lunch(input)
    println(resultLunch)
}

data class RGB(val red: Int, val green: Int, val blue: Int) {

    companion object {
        fun from(input: String): RGB {
            var red = 0
            var green = 0
            var blue = 0
            input.split(", ").forEach {
                val (count, color) = it.split(" ")
                when (color) {
                    "blue" -> blue += count.toInt()
                    "green" -> green += count.toInt()
                    "red" -> red += count.toInt()
                    else -> {}
                }
            }
            return RGB(red, green, blue)
        }
    }
}

data class Game(val id: Int, val rgbs: List<RGB>)

fun Game.possible(config: RGB) = this.rgbs.maxOf { it.red } <= config.red &&
            this.rgbs.maxOf { it.green } <= config.green &&
            this.rgbs.maxOf { it.blue } <= config.blue

fun Game.power() = this.rgbs.maxOf { it.red } * this.rgbs.maxOf { it.green } * this.rgbs.maxOf { it.blue }

fun breakfast(input: List<String>): Int {

    val config = RGB(12, 13, 14)

    val games = input.map { line ->
        val id = line.substringAfter("Game ").substringBefore(":").toInt()
        val rgbs = line.substringAfter(": ").split("; ").map { RGB.from(it) }
        Game(id, rgbs)
    }

    return games.filter { it.possible(config) }.sumOf { it.id }
}

fun lunch(input: List<String>): Int {

    val games = input.map { line ->
        val id = line.substringAfter("Game ").substringBefore(":").toInt()
        val rgbs = line.substringAfter(": ").split("; ").map { RGB.from(it) }
        Game(id, rgbs)
    }

    return games.sumOf { it.power() }
}