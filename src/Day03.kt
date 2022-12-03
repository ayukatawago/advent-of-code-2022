fun main() {
    fun Char.getScore(): Int =
        when {
            this.isLowerCase() -> this - 'a' + 1
            this.isUpperCase() -> this - 'A' + 27
            else -> 0
        }

    fun part1(input: List<String>): Int {
        var scoreSum = 0
        input.map { it.chunked(it.length / 2) }.forEach {
            if (it.size != 2) return@forEach
            val firstSet = it[0].toSet()
            val secondSet = it[1].toSet()
            val intersectSet = firstSet intersect secondSet
            if (intersectSet.size != 1) return@forEach
            scoreSum += intersectSet.first().getScore()
        }
        return scoreSum
    }

    fun part2(input: List<String>): Int {
        var scoreSum = 0
        input.chunked(3).forEach {
            if (it.size != 3) return@forEach
            val firstSet = it[0].toSet()
            val secondSet = it[1].toSet()
            val thirdSet = it[2].toSet()
            val intersectSet = firstSet intersect secondSet intersect thirdSet
            if (intersectSet.size != 1) return@forEach
            scoreSum += intersectSet.first().getScore()
        }
        return scoreSum
    }


    val testInput = readInput("Day03_test")

    println(part1(testInput))
    println(part2(testInput))
}
