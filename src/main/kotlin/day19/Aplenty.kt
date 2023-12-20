package day19

import java.io.File

fun main() {
    val input = File("data/day19/input.txt").readText().split("\n\n")
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
}

data class Condition(val category: Char, val operator: Char, val amount: Int, val goal: String)

fun Rule.getGoal(states: List<State>): String {
    this.conditions.dropLast(1).forEach {
        val category = it.category
        val state = states.first { state -> state.category  == category }
        when (it.operator) {
            '>' -> {
                if (state.amount > it.amount) return it.goal
            }
            else -> {
                if (state.amount < it.amount) return it.goal
            }
        }
    }
    // if none of the above kicked in it is safe to return the last which is without condition
    return this.conditions.last().goal
}

data class Rule(val name: String, val conditions: List<Condition>)

data class State(val category: Char, val amount: Int)

fun breakfast(input: List<String>) : Int {

    val (r, p) = input
    val rules = r.split("\n").map { rule ->
        val name = rule.substringBefore("{")
        val parts = rule.substringAfter("{")
            .substringBefore("}").split(",").map {
                if (!it.contains(":")) Condition('_', '_', 0, it)
                else {
                    val c = it[0]
                    val o = it[1]
                    val a = it.substring(2).substringBefore(":")
                    val g = it.substringAfter(":")
                    Condition(c, o, a.toInt(), g)
                }
            }
        Rule(name, parts)
    }

    val parts = p.split("\n").map { part ->
        val conditions = part.substringAfter("{")
            .substringBefore("}").split(",").map {
                val c = it[0]
                val a = it.substringAfter("=").toInt()
                State(c, a)
            }
        conditions
    }

    val start = rules.first { it.name == "in" }
    var result = 0

    parts.forEach { states ->
        var current = start
        while (true) {
            val next = current.getGoal(states)
            if (next in listOf("A", "R")) {
                if (next == "A") result += states.sumOf { it.amount }
                break
            }
            current = rules.first { it.name == next }
        }
    }

    return result
}