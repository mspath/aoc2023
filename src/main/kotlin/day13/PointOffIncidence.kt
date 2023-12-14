package day13

import java.io.File

fun main() {
    val input = File("data/day13/sample.txt").readText()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast) // 30535
    //val resultLunch = lunch(input)
    //println(resultLunch) // 30844
}

fun Pair<List<Int>, List<Int>>.symmetrical(): Boolean {
    val size = minOf(this.first.size, this.second.size)
    if (size < 1) return false
    return this.first.reversed().subList(0, size) == this.second.subList(0, size)
}

// left of / above index or 0
fun solveSymmetry(codes: List<Int>): Int {
    println("solving for: $codes")
    for (i in codes.indices) {
        val before = codes.subList(0, i + 1)
        val after = codes.subList(i + 1, codes.size)
        if (Pair(before, after).symmetrical()) return i + 1
    }
    return 0
}

fun String.flipOne(): List<String> {
    fun Char.flip() = when (this) {
        '.' -> '#'
        else -> '.'
    }
    return this.indices.map {
        val c = this[it]
        val sb = StringBuilder(this)
        sb.setCharAt(it, c.flip())
        sb.toString()
    }
}

fun breakfast(input: String) : Int {
    val groups = input.split("\n\n")

    val blocks = groups.map {
        val rows = it.split("\n")
        val cols = rows.first().indices.map { col ->
            rows.indices.map { row ->
                rows[row][col]
            }.joinToString("")
        }
        Pair(rows, cols)
    }
    val result = blocks.sumOf { solveSymmetry(it.first.map { it.hashCode() }) } * 100 + blocks.sumOf { solveSymmetry(it.second.map { it.hashCode() }) }

    lunch(input)

    return result
}

fun lunch(input: String) {

    println("in lunch")

    val groupsFlipped = input.split("\n\n").map {
        it.flipOne()
    }

    val groups = input.split("\n\n")

    val blocks = groups.map {
        val rows = it.split("\n")
        val cols = rows.first().indices.map { col ->
            rows.indices.map { row ->
                rows[row][col]
            }.joinToString("")
        }
        Pair(rows, cols)
    }

    val horizontals = blocks.map { solveSymmetry(it.first.map { it.hashCode() }) }.map { it > 0 }

    println(horizontals)
}