package day25

import java.io.File

fun main() {
    val input = File("data/day25/sample.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
}

data class Connection(val first: String, val second: String)

fun breakfast(input: List<String>) : Long {

    val connections = input.flatMap {line ->
        val left = line.substringBefore(": ")
        val right = line.substringAfter(": ").split(" ")
        right.map { Connection(left, it) } + right.map { Connection(it, left) }
    }

    connections.sortedBy { it.first }.forEach { println(it) }

    val offByOne = connections.groupBy { it.first }.map { it.key to it.value.map { it.second } }
    offByOne.forEach {
        println(it)
    }

    val offByTwo = offByOne.map { it.first to it.second.flatMap { n ->
            offByOne.filter { it.first == n}
        }.flatMap { it.second }.toList().toSet()
    }

    offByTwo.forEach {
        println(it)
    }

    // oh well
    return -1L
}