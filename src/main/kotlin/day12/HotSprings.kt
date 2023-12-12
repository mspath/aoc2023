package day12

import java.io.File

typealias Record = Pair<String, String>

fun main() {
    val input = File("data/day12/sample.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
}

fun Record.unfold(): Record {
    val observations = (0..4).map { this.first }.joinToString("?")
    val controls = (0..4).map { this.second }.joinToString(",")
    return Record(observations, controls)
}

fun String.generateRule(): Regex {
    val occurrences = this.split(",").map { it.toInt() }
    val groups = occurrences.map { "#{$it}[^#]" }
    val start = "^[^#]*"
    val end = "*$"
    return Regex(start + groups.joinToString("+") + end)
}

// thx. chatgpt
fun generateStrings(input: String): List<String> {
    val result = mutableListOf<String>()
    fun generateHelper(current: String, index: Int) {
        if (index == input.length) {
            result.add(current)
            return
        }
        if (input[index] == '?') {
            generateHelper("$current.", index + 1)
            generateHelper("$current#", index + 1)
        } else {
            generateHelper(current + input[index], index + 1)
        }
    }
    generateHelper("", 0)
    return result
}

fun Record.getDamaged() = this.second.split(",").map { it.toInt() }.sum()
fun Record.getUnknown() = this.first.filter { it == '?' }.count()

fun Record.getVariations(): List<Record> {
    val variations = generateStrings(this.first)
    return variations.map {
        Record(it, this.second)
    }
}

fun breakfast(input: List<String>) : Int {
    val records = input.map {
        val (observation, control) = it.split(" ")
        Pair(observation, control)
    }
    val result = records.sumOf {
        val variations = it.getVariations()
        val pattern = it.second.generateRule()
        variations.filter { pattern.matches(it.first) }.size
    }
    return result
}