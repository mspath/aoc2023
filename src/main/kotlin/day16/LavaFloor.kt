package day16

import java.io.File

fun main() {
    val input = File("data/day16/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
    val resultLunch = lunch(input)
    println(resultLunch)
}

enum class Direction { NORTH, SOUTH, EAST, WEST }

data class Position(val x: Int, val y: Int)

data class State(val position: Position, val direction: Direction)

fun State.next(type: Char, border: Position): List<State> {
    val beams = when(type) {
        '|' -> {
            when(direction) {
                Direction.NORTH -> listOf(this.copy(Position(this.position.x, this.position.y - 1)))
                Direction.SOUTH -> listOf(this.copy(Position(this.position.x, this.position.y + 1)))
                Direction.EAST -> listOf(State(Position(this.position.x, this.position.y - 1), Direction.NORTH),
                    State(Position(this.position.x, this.position.y + 1), Direction.SOUTH))
                Direction.WEST -> listOf(State(Position(this.position.x, this.position.y + 1), Direction.SOUTH),
                    State(Position(this.position.x, this.position.y - 1), Direction.NORTH))
            }
        }
        '-' -> {
            when(direction) {
                Direction.EAST -> listOf(this.copy(Position(this.position.x + 1, this.position.y)))
                Direction.WEST -> listOf(this.copy(Position(this.position.x - 1, this.position.y)))
                Direction.NORTH -> listOf(State(Position(this.position.x - 1, this.position.y), Direction.WEST),
                    State(Position(this.position.x + 1, this.position.y), Direction.EAST))
                Direction.SOUTH -> listOf(State(Position(this.position.x + 1, this.position.y), Direction.EAST),
                    State(Position(this.position.x - 1, this.position.y), Direction.WEST))
            }
        }
        '/' -> {
            when(direction) {
                Direction.NORTH -> listOf(State(Position(this.position.x + 1, this.position.y), Direction.EAST))
                Direction.SOUTH -> listOf(State(Position(this.position.x - 1, this.position.y), Direction.WEST))
                Direction.EAST -> listOf(State(Position(this.position.x, this.position.y - 1), Direction.NORTH))
                Direction.WEST -> listOf(State(Position(this.position.x, this.position.y + 1), Direction.SOUTH))
            }
        }
        '\\' -> {
            when(direction) {
                Direction.NORTH -> listOf(State(Position(this.position.x - 1, this.position.y), Direction.WEST))
                Direction.SOUTH -> listOf(State(Position(this.position.x + 1, this.position.y), Direction.EAST))
                Direction.EAST -> listOf(State(Position(this.position.x, this.position.y + 1), Direction.SOUTH))
                Direction.WEST -> listOf(State(Position(this.position.x, this.position.y - 1), Direction.NORTH))
            }
        }
        '.' -> {
            when(direction) {
                Direction.NORTH -> listOf(this.copy(Position(position.x, position.y - 1)))
                Direction.SOUTH -> listOf(this.copy(Position(position.x, position.y + 1)))
                Direction.EAST -> listOf(this.copy(Position(position.x + 1, position.y)))
                Direction.WEST -> listOf(this.copy(Position(position.x - 1, position.y)))
            }
        }
        else -> emptyList()
    }
    return beams.filter { it.position.x in 0 .. border.x && it.position.y in 0 .. border.y}
}

fun solve(input: List<String>, beam: State) : Int {

    val energized: MutableSet<Position> = mutableSetOf()
    val beams: MutableList<State> = mutableListOf(beam)
    val knownStates: MutableSet<State> = mutableSetOf()

    while (beams.isNotEmpty()) {
        val new: MutableSet<State> = mutableSetOf()
        val old: MutableSet<State> = mutableSetOf()
        beams.forEach { beam ->
            old.add(beam)
            if (!knownStates.contains(beam)) {
                energized.add(beam.position)
                val nexts = beam.next(
                    input[beam.position.y][beam.position.x],
                    Position(input.first().length - 1, input.size - 1)
                )
                new.addAll(nexts)
            }
        }
        beams.addAll(new)
        beams.removeAll(old)
        knownStates.addAll(old)
    }
    return energized.size
}

fun breakfast(input: List<String>) : Int {
    return solve(input, State(Position(0, 0), Direction.EAST))
}

fun lunch(input: List<String>) : Int {
    val width = input.size
    val height = input.first().length
    val north = (0 until width).map { State(Position(it, 0), Direction.SOUTH) }
    val south = (0 until width).map { State(Position(it, height - 1), Direction.NORTH) }
    val east = (0 until height).map { State(Position(0, it), Direction.WEST) }
    val west = (0 until height).map { State(Position(width - 1, it), Direction.EAST) }
    val starts = north + south + east + west
    return starts.maxOf { solve(input, it) }
}