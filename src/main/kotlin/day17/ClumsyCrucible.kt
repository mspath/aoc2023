package day17

import java.io.File
import java.util.*
import kotlin.Comparator

fun main() {
    val input = File("data/day17/sample.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
}

data class State(val row: Int, val col: Int, val heat: Int, val path: String)

data class Seen(val row: Int, val col: Int, val path: String)

fun State.seen() = Seen(this.row, this.col, this.path)

fun breakfast(input: List<String>) : Int {
    val pool = input.map {
        it.map { cost -> cost.toString().toInt() }
    }
    val compareByHeat: Comparator<State> = compareBy { it.heat }
    val progress = PriorityQueue(compareByHeat)
    val offsetNextPositions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
    val offsetNextPositionsDirections = listOf('^', 'v', '<', '>')
    val goal = Pair(pool.size - 1, pool.first().size - 1)

    // seed the queue
    progress.add(State(0, 1, pool[0][1], ">"))
    progress.add(State(1, 0, pool[1][0], "v"))

    val seen: MutableSet<Seen> = mutableSetOf()

    while (!progress.isEmpty()) {
        val current = progress.remove()
        if (current.seen() in seen) continue
        seen.add(current.seen())
        val nextPositions = offsetNextPositions.mapIndexedNotNull { index, offset ->
            val r = current.row + offset.first
            val c = current.col + offset.second
            val p = offsetNextPositionsDirections[index]
            val heat = try { current.heat + pool[r][c] } catch (e: Exception) { 0 }
            val path = if (p != current.path.first())  p.toString() else current.path + p
            if (r < 0 || r >= pool.size || c < 0 || c >= pool.first().size || path.length > 3) null else
            State(r, c, heat, path)
        }.filterNot { seen.contains(it.seen()) }
        if (nextPositions.filter { it.row == goal.first && it.col == goal.second }.isNotEmpty()) {
            println("done")
            return nextPositions.first { it.row == goal.first && it.col == goal.second }.heat + 3
        }
        progress.addAll(nextPositions)
    }

    return -1
}