package day4

import java.io.File
import kotlin.math.pow

fun main() {
    val input = File("data/day4/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
    val resultLunch = lunch(input)
    println(resultLunch)
}

data class Card (val winning: List<Int>, val numbers: List<Int>) {

    companion object {
        fun from (input: String): Card {
            val values = input.substringAfter(":")
            val (left, right) = values.split(" | ")
            val winning = left.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            val numbers = right.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            return Card(winning, numbers)
        }

        fun value (card: Card): Int {
            val matches = card.winning.toSet() intersect card.numbers.toSet()
            if (matches.isEmpty()) return 0
            return 2.toDouble().pow(matches.size - 1).toInt()
        }

        fun matches (card: Card): Int {
            val matches = card.winning.toSet() intersect card.numbers.toSet()
            return matches.size
        }
    }
}

fun breakfast(input: List<String>): Int {
    val cards = input.map {
        Card.from(it)
    }
    return cards.sumOf { Card.value(it) }
}

fun lunch(input: List<String>): Long {
    val cards = input.map {
        Card.from(it)
    }
    val deck = MutableList(cards.size) { 1L }
    cards.forEachIndexed { index, card ->
        val matches = Card.matches(card)
        repeat(matches) {
            deck[index + it + 1] = deck[index + it + 1] + deck[index]
        }
    }

    return deck.sumOf { it }
}