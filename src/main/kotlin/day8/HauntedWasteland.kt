package day8

import java.io.File

fun main() {
    val input = File("data/day8/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
    val resultLunch = lunch(input)
    println(resultLunch)
}

data class Node(val name: String, val left: String, val right: String)

fun breakfast(input: List<String>) : Long {

    val instructions = input.first().toCharArray()

    val sequence = generateSequence(0) { step ->
        (step + 1) % instructions.size
    }.map { instructions[it] }.iterator()

    val rules = input.drop(2).map { line ->
        val name = line.substring(0, 3)
        val left = line.substring(7, 10)
        val right = line.substring(12, 15)
        Node(name, left, right)
    }.associateBy { it.name }

    var steps = 0L
    var nextNodeName = "AAA"

    while(nextNodeName != "ZZZ") {
        val nextNode = when (sequence.next()) {
            'L' -> rules[nextNodeName]?.left
            else -> rules[nextNodeName]?.right
        }
        nextNode?.let {
            nextNodeName = it
        }
        steps++
    }
    return steps
}

fun gcd(a: Long, b: Long): Long {
    if (a == 0L) return b
    return gcd(b % a, a)
}

fun lcm(a: Long, b: Long) = a * (b / gcd(a, b))

fun lcm(input: List<Long>): Long {
    var result = input[0]
    for (i in 1 until input.size) result = lcm(result, input[i])
    return result
}

fun lunch(input: List<String>) : Long {

    val instructions = input.first().toCharArray()

    val sequence = generateSequence(0) { step ->
        (step + 1) % instructions.size
    }.map { instructions[it] }.iterator()

    val rules = input.drop(2).map { line ->
        val name = line.substring(0, 3)
        val left = line.substring(7, 10)
        val right = line.substring(12, 15)
        Node(name, left, right)
    }.associateBy { it.name }

    val names = input.drop(2).map {
        it.substring(0, 3)
    }
    val namesEndingInA = names.filter { it.endsWith("A") }
    val namesEndingInAEnds = namesEndingInA.map {
        mutableListOf<Long>()
    }

    namesEndingInA.forEachIndexed { index, name ->

        val iterator = generateSequence(0) { step ->
            (step + 1) % instructions.size
        }.map { instructions[it] }.iterator()

        var steps = 0L
        var nextNodeName = name

        while (namesEndingInAEnds[index].size < 1) {
            steps++
            val nextNode = when (sequence.next()) {
                'L' -> rules[nextNodeName]?.left
                else -> rules[nextNodeName]?.right
            }
            nextNode?.let {
                nextNodeName = it
                if (it.endsWith("Z")) namesEndingInAEnds[index].add(steps)
            }
        }

        println(namesEndingInAEnds[index])
        // paste them into bard to get the answer for lcm ...
    }

    val values = namesEndingInAEnds.map { it.first() }.sorted().reversed()
    // this already will print the loop values and lcm
    println(values)
    println("lcm: ${lcm(values)}")

    val currents = values.toMutableList()
    var x = 0
    // this will take a while bbut also find the result
    while (currents.toSet().size > 1) {
        x++
        val min = currents.withIndex().minBy { (_, f) -> f }.index
        currents[min] += values[min]
    }
    println(currents)
    return currents.first()
}