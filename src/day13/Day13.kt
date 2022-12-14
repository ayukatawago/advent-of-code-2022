package day13

import readInput

fun main() {
    val testInput = readInput("day13/test")
    val packets = testInput.filter { it.isNotBlank() }.map { Packet.from(it) }

    part1(packets)
    part2(packets)
}

private fun part1(packets: List<Packet>) {
    val answer = packets.chunked(2).mapIndexed { index, pair ->
        if (pair.first() < pair.last()) index + 1 else 0
    }
    println(answer.sum())
}

private fun part2(packets: List<Packet>) {
    val packet2 = Packet.from("[[2]]")
    val packet6 = Packet.from("[[6]]")
    val sortedPackets = (packets + packet2 + packet6).sorted()
    val index2 = sortedPackets.indexOf(packet2) + 1
    val index6 = sortedPackets.indexOf(packet6) + 1
    println(index2 * index6)
}

sealed class Packet : Comparable<Packet> {
    class ListPacket(val values: List<Packet>) : Packet() {
        override fun compareTo(other: Packet): Int =
            when (other) {
                is ListPacket ->
                    values.zip(other.values).map { (first, second) -> first.compareTo(second) }.firstOrNull { it != 0 }
                        ?: values.size.compareTo(other.values.size)

                is IntPacket -> compareTo(other.toList())
            }
    }

    data class IntPacket(val value: Int) : Packet() {
        fun toList(): Packet = ListPacket(listOf(this))

        override fun compareTo(other: Packet): Int =
            when (other) {
                is ListPacket -> toList().compareTo(other)
                is IntPacket -> value.compareTo(other.value)
            }
    }

    companion object {
        fun from(input: String): Packet {
            val iterator = input.split("""((?<=[\[\],])|(?=[\[\],]))""".toRegex())
                .filter { it.isNotBlank() }
                .filter { it != "," }
                .iterator()
            return from(iterator)
        }

        private fun from(iterator: Iterator<String>): Packet {
            val packet = mutableListOf<Packet>()
            while (iterator.hasNext()) {
                when (val value = iterator.next()) {
                    "]" -> return ListPacket(packet)
                    "[" -> packet.add(from(iterator))
                    else -> packet.add(IntPacket(value.toInt()))
                }
            }
            return ListPacket(packet)
        }
    }
}