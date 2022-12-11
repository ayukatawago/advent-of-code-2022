package day10

import readInput
import java.util.LinkedList

fun main() {
    fun part1(instructions: List<Instruction>): Int {
        var signalStrength = 0
        var register = 1
        var cycle = 0
        val queue = LinkedList<Instruction>()
        queue.addAll(instructions)
        var nexeCheckCycle = 20

        var storedInstruction: Instruction.AddX? = null

        while (queue.isNotEmpty() || storedInstruction != null) {
            cycle++
            if (storedInstruction == null) {
                val instruction = queue.removeFirst()
                if (instruction is Instruction.AddX) {
                    storedInstruction = instruction
                }
            } else {
                register += storedInstruction.value
                storedInstruction = null
            }
            if (cycle == nexeCheckCycle - 1) {
                val strength = nexeCheckCycle * register
                println("### [$cycle] $nexeCheckCycle $register -> $strength")
                signalStrength += nexeCheckCycle * register
                nexeCheckCycle += 40
            }
            println("[$cycle] $register")
        }

        return signalStrength
    }

    fun part2(instructions: List<Instruction>): List<String> {
        val answer = mutableListOf<String>()

        var register = 1
        var cycle = 0
        val queue = LinkedList<Instruction>()
        queue.addAll(instructions)

        val row = CharArray(40) { '.' }
        var storedInstruction: Instruction.AddX? = null
        var spriteRange = IntRange(0, 2)

        while (queue.isNotEmpty() || storedInstruction != null) {
            cycle++
            row[(cycle - 1) % 40] = if ((cycle - 1) % 40 in spriteRange) {
                '#'
            } else {
                '.'
            }
            if (storedInstruction == null) {
                val instruction = queue.removeFirst()
                if (instruction is Instruction.AddX) {
                    storedInstruction = instruction
                }
            } else {
                register += storedInstruction!!.value
                spriteRange = IntRange(register - 1, register + 1)
                storedInstruction = null
            }
            if (cycle % 40 == 0) {
                answer.add(row.joinToString(""))
            }
        }

        return answer
    }

    val testInput = readInput("day10/test")
    val instructions = testInput.mapNotNull {
        when {
            it == "noop" -> Instruction.Noop
            it.startsWith("addx") -> Instruction.AddX(it.replace("addx ", "").toInt())
            else -> null
        }
    }

    println(part1(instructions))
    part2(instructions).forEach {
        println(it)
    }
}

private sealed class Instruction {
    class AddX(val value: Int) : Instruction()
    object Noop : Instruction()
}
