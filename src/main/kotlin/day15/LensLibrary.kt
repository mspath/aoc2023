package day15

import java.io.File

fun main() {
    val input = File("data/day15/sample.txt").readText().split(",")
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
}

data class Lens(val label: String, var value: Int)

fun String.hash(): Int {
    var hash = 0
    this.forEach {
        hash += it.code
        hash *= 17
        hash %= 256
    }
    return hash
}

fun breakfast(input: List<String>) : Int {
    return input.sumOf { it.hash() }
}