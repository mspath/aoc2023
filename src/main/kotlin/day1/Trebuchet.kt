package day1

import java.io.File

fun main() {
    val input = File("data/day1/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
    val resultLunch = lunch(input)
    println(resultLunch)
}

fun breakfast(input: List<String>) : Int {
    val result = input.sumOf {
        val first = it.first { c -> c.isDigit() }
        val last = it.last { c -> c.isDigit() }
        first.toString().toInt() * 10 + last.toString().toInt()
    }
    return result
}

fun String.digits(): List<Int> {
    return (0 until this.length).mapNotNull {index ->
        if (this[index].isDigit()) this[index].toString().toInt()
        else if (this.startsWith("one", index)) 1
        else if (this.startsWith("two", index)) 2
        else if (this.startsWith("three", index)) 3
        else if (this.startsWith("four", index)) 4
        else if (this.startsWith("five", index)) 5
        else if (this.startsWith("six", index)) 6
        else if (this.startsWith("seven", index)) 7
        else if (this.startsWith("eight", index)) 8
        else if (this.startsWith("nine", index)) 9
        else null
    }
}

fun lunch(input: List<String>) : Int {
    val digits = input.map {
        it.digits()
    }
    val result = digits.sumOf {
        it.first() * 10 + it.last()
    }
    return result
}