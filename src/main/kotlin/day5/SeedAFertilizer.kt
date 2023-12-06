package day5

import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    val input = File("data/day5/sample.txt").readText()
    val resultBreakfast = breakfast(input)
    println(resultBreakfast)
    val resultLunch = lunch(input)
    println(resultLunch)
}

data class Mapping(val destinationStart: Long, val sourceStart: Long, val range: Long) {
    fun asTransformer() = Transformer(sourceStart.rangeUntil(sourceStart + range), destinationStart - sourceStart)
}

data class Transformer(val range: LongRange, val shift: Long) {
    fun processTransformer(input: Transformer): List<Transformer> {
        return emptyList()
    }

    // first will hold any transformed values, second the remainder
    fun processRange(input: LongRange): Pair<LongRange, LongRange> {
        val intersection = input.intersect(this.range)
        val intersectionShifted = if (intersection.isEmpty()) LongRange.EMPTY
        else LongRange(intersection.first + this.shift, intersection.last + this.shift)
        val complements = input.filter(this.range)
        return Pair(intersectionShifted, complements)
    }
}

fun LongRange.intersect(other: LongRange): LongRange {
    if (this.isEmpty() || other.isEmpty()) return LongRange.EMPTY
    val start = max(this.first, other.first)
    val end = min(this.last, other.last)
    if (start > end) return LongRange.EMPTY
    return LongRange(start, end)
}

fun LongRange.filter(other: LongRange): LongRange {
    val intersectionStart = maxOf(this.first, other.first)
    val intersectionEnd = minOf(this.last, other.last)

    return if (intersectionStart > intersectionEnd) {
        this
    } else if (this.first == intersectionStart && this.last == intersectionEnd) {
        LongRange.EMPTY
    } else if (this.first == intersectionStart) {
        (intersectionEnd + 1)..this.last
    } else {
        this.first until intersectionStart // Return the range before intersectionStart
    }
}

data class Layer(val mappings: List<Mapping>)

fun Layer.map(from: Long): Long {
    val mapper = this.mappings.firstOrNull {
        from >= it.sourceStart && from < it.sourceStart + it.range
    }
    mapper?.let {
        val offset = from - it.sourceStart
        return it.destinationStart + offset
    }
    return from
}

fun breakfast(input: String): Long {
    val seeds = input.split("\n\n").first().substringAfter(": ")
        .split(" ")
        .map { it.toLong() }

    val layers = input.split("\n\n").drop(1).map {
        val mappings = it.split("\n").drop(1).map {
            val (d, s, r) = it.split(" ").map { it.toLong() }
            Mapping(d, s, r)
        }.toList()
        Layer(mappings)
    }

    val outputs = seeds.map {
        var value = it
        layers.forEach {
            value = it.map(value)
        }
        value
    }

    return outputs.min()
}

fun lunch(input: String): Long {

    val seeds = input.split("\n\n").first().substringAfter(": ")
        .split(" ")
        .map { it.toLong() }
        .windowed(2, 2)
        .map { it.first().rangeUntil(it.first() + it.last())}
        .sortedBy { it.first }

    val layers = input.split("\n\n").drop(1).map {
        val mappings = it.split("\n").drop(1).map {
            val (d, s, r) = it.split(" ").map { it.toLong() }
            Mapping(d, s, r)
        }.toList()
        Layer(mappings)
    }.map { it.mappings.map { it.asTransformer() }.sortedBy { it.range.first } }

    // seeds is a list of ranges of the seeding values
    seeds.forEach { println(it) }

    // layers is a list of layers.
    // a layer is a list of transformers which contain a range and a shift to apply to this range
    layers.forEach { println(it) }

    // for each of the layers:
    //   at each layer we start with a 'consolidated' list of ranges of the current values

    //   each range needs to be applied to all transformers at the layer

    //   the result of these transformations needs to be consolidated to a fresh list of ranges

    // after the last transformation, the smallest value of all ranges is the result

    return -1
}