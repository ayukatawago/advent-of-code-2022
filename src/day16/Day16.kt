package day16

import readInput
import kotlin.math.max

fun main() {
    val testInput = readInput("day16/test")
    val regex = """Valve ([A-Z]+) has flow rate=(\d+); tunnels* leads* to valves* ([A-Z\s,]+)""".toRegex()
    val valveDataMap = HashMap<String, Valve>()
    testInput.forEach {
        val result = regex.matchEntire(it) ?: return@forEach
        val name = result.groupValues[1]
        val flowRate = result.groupValues[2].toInt()
        val valves = result.groupValues[3].split(", ").toSet()
        valveDataMap.put(name, Valve(flowRate, valves))
    }
    println(part1(valveDataMap))
    println(part2(valveDataMap))
}

private fun part1(valveMap: Map<String, Valve>): Int {
    val totalTime = 30
    val dp = Array(totalTime + 1) { hashMapOf<Status, Int>() }

    updateDp(dp, 0, Status("AA", emptySet()), 0)
    (0 until totalTime).forEach { time ->
        dp[time].forEach { (status, pressure) ->
            val valve = valveMap[status.valveName]!!
            if (valve.flowRate > 0 && status.valveName !in status.openedValves) {
                val newStatus = Status(status.valveName, status.openedValves + status.valveName)
                updateDp(dp, time + 1, newStatus, pressure + (totalTime - time - 1) * valve.flowRate)
            }
            valve.leadsTo.forEach {
                val newStatus = Status(it, status.openedValves)
                updateDp(dp, time + 1, newStatus, pressure)
            }
        }
    }

    return dp[totalTime].values.max()
}

private fun part2(valveMap: Map<String, Valve>): Int {
    val totalTime = 26
    val dp = Array(totalTime + 1) { hashMapOf<Status, Int>() }

    updateDp(dp, 0, Status("AA", emptySet()), 0)
    (0 until totalTime).forEach { time ->
        dp[time].forEach { (status, pressure) ->
            val valve = valveMap[status.valveName]!!
            if (valve.flowRate > 0 && status.valveName !in status.openedValves) {
                val newStatus = Status(status.valveName, status.openedValves + status.valveName)
                updateDp(dp, time + 1, newStatus, pressure + (totalTime - time - 1) * valve.flowRate)
            }
            valve.leadsTo.forEach {
                val newStatus = Status(it, status.openedValves)
                updateDp(dp, time + 1, newStatus, pressure)
            }
        }
    }

    val bm = dp[totalTime].toList().groupingBy { it.first.openedValves }.fold(0) { a, e -> maxOf(a, e.second) }
    val effectiveValveSet = valveMap.filter { it.value.flowRate > 0 }.map { it.key }.toSet()
    var maxPressure = 0
    effectiveValveSet.toRepeatedCombination().forEach { elf ->
        val elfPressure = bm[elf] ?: return@forEach
        val remaining = effectiveValveSet.toSet() - elf
        remaining.toRepeatedCombination().forEach {
            val pressure = bm[it] ?: return@forEach
            maxPressure = max(maxPressure, elfPressure + pressure)
        }
    }
    return maxPressure
}

private fun Set<String>.toRepeatedCombination(): List<Set<String>> {
    return if (size == 1) {
        listOf(emptySet(), setOf(first()))
    } else {
        val remainingList = drop(1).toSet().toRepeatedCombination()
        remainingList + remainingList.map { it + first() }
    }
}

private fun updateDp(dp: Array<HashMap<Status, Int>>, time: Int, status: Status, pressure: Int) {
    val currentPressure = dp[time][status]
    if (currentPressure == null || pressure > currentPressure) {
        dp[time][status] = pressure
    }
}

private data class Valve(val flowRate: Int, val leadsTo: Set<String>)

private data class Status(val valveName: String, val openedValves: Set<String>)