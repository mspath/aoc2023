import day1.benchmarkCalorieCounting
import day1.testTrebuchet

fun main() {
    runTests()
    runBenchmarks()
}

fun runTests() {
    testTrebuchet()
}

fun runBenchmarks() {
    benchmarkCalorieCounting()
}