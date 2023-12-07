package day7

import java.io.File

fun main() {
    val input = File("data/day7/input.txt").readLines()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
    val resultLunch = lunch(input)
    println(resultLunch)
}

data class Hand(val cards: String, val bid: Int)

fun Hand.order(): Long {
    val sorted = cards.toList().sorted()
    val grouped = cards.groupBy { it }

    if (sorted.toSet().size == 1) return 7
    if (grouped.any { it.value.size == 4 }) return 6
    if (sorted.toSet().size == 2) return 5
    if (grouped.any { it.value.size == 3 }) return 4
    if (sorted.toSet().size == 3) return 3
    if (sorted.toSet().size == 4) return 2
    return 1
}

fun Hand.cardOrder(): String {
    return this.cards.map { it.replaceValue() }.joinToString("")
}

fun Hand.cardOrderJoker(): String {
    return this.cards.map { it.replaceValueJoker() }.joinToString("")
}

fun Char.replaceValue() = when(this) {
    'A' -> 'M'
    'K' -> 'L'
    'Q' -> 'K'
    'J' -> 'J'
    'T' -> 'I'
    '9' -> 'H'
    '8' -> 'G'
    '7' -> 'F'
    '6' -> 'E'
    '5' -> 'D'
    '4' -> 'C'
    '3' -> 'B'
    '2' -> 'A'
    else -> '_'
}

fun Char.replaceValueJoker() = when(this) {
    'A' -> 'M'
    'K' -> 'L'
    'Q' -> 'K'
    'T' -> 'J'
    '9' -> 'I'
    '8' -> 'H'
    '7' -> 'G'
    '6' -> 'F'
    '5' -> 'E'
    '4' -> 'D'
    '3' -> 'C'
    '2' -> 'B'
    'J' -> 'A'
    else -> '_'
}

fun Char.cardValue() = when(this) {
    'A' -> 16
    'K' -> 13
    'Q' -> 12
    'J' -> 11
    'T' -> 10
    '9' -> 9
    '8' -> 8
    '7' -> 7
    '6' -> 6
    '5' -> 5
    '4' -> 4
    '3' -> 3
    '2' -> 2
    else -> -1
}

fun Hand.expandJoker(): Pair<Hand, Int> {

    val possibleCards = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')
    val c0 = if (this.cards[0] == 'J') possibleCards else listOf(this.cards[0])
    val c1 = if (this.cards[1] == 'J') possibleCards else listOf(this.cards[1])
    val c2 = if (this.cards[2] == 'J') possibleCards else listOf(this.cards[2])
    val c3 = if (this.cards[3] == 'J') possibleCards else listOf(this.cards[3])
    val c4 = if (this.cards[4] == 'J') possibleCards else listOf(this.cards[4])
    val possibleHands: MutableList<Hand> = mutableListOf()

    repeat(c0.size) { i0 ->
        repeat(c1.size) { i1 ->
            repeat(c2.size) { i2 ->
                repeat(c3.size) {i3 ->
                    repeat(c4.size) {i4 ->
                        val hand = listOf(c0[i0], c1[i1], c2[i2], c3[i3], c4[i4]).joinToString("")
                        possibleHands.add(Hand(hand, this.bid))
                    }
                }
            }
        }
    }

    val bestGroup = possibleHands.groupBy { it.order() }.toList().sortedBy { it.first }.last()
    val bestHand = bestGroup.second.sortedBy { it.cardOrderJoker() }.last()
    return Pair(bestHand, bestGroup.first.toInt())
}

fun breakfast(input: List<String>): Long {
    val hands = input.map {
        val (c, b) = it.split(" ")
        Hand(c, b.toInt())
    }.sortedBy { it.cardOrder() }
    val grouped = hands.groupBy { it.order() }.toList().sortedBy { it.first }
    val ranking = grouped.map { it.second }.flatten()
    return ranking.mapIndexed { index, hand -> hand.bid.toLong() * (index + 1) }.sumOf { it }
}

fun lunch(input: List<String>): Long {
    val hands = input.map {
        val (c, b) = it.split(" ")
        Hand(c, b.toInt())
    }.sortedBy { it.cardOrderJoker() }
    // here we have the ordering of the hands if we just would go for the card order including the joker rule.

    return hands.sortedBy {
        it.expandJoker().second
    }.mapIndexed { index, hand -> hand.bid.toLong() * (index + 1) }.sumOf { it }
}