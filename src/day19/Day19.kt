package day19

import readInput

fun main() {
    val testInput = readInput("day19/test")

    val blueprints = testInput.mapNotNull {
        val regex =
            """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian.""".toRegex()
        val result = regex.matchEntire(it) ?: return@mapNotNull null
        val inputValues = result.groupValues.drop(1)
        BluePrintData(
            inputValues[0].toInt(),
            inputValues[1].toInt(),
            inputValues[2].toInt(),
            inputValues[3].toInt(),
            inputValues[4].toInt(),
            inputValues[5].toInt(),
            inputValues[6].toInt()
        )
    }
    println(blueprints)
    println(part1(blueprints))
    println(part2(blueprints))
}

private fun part1(blueprints: List<BluePrintData>): Int =
    blueprints.sumOf { blueprint ->
        blueprint.id * blueprint.getMaxGeodeCount(24)
    }

private fun part2(blueprints: List<BluePrintData>): Int =
    blueprints.take(3).fold(1) { acc, blueprint ->
        acc * blueprint.getMaxGeodeCount(32)
    }

private data class BluePrintData(
    val id: Int,
    val oreCostForOre: Int,
    val oreCostForClay: Int,
    val oreCostForObsidian: Int,
    val clayCostForObsidian: Int,
    val oreCostForGeode: Int,
    val obsidianCostForGeode: Int
) {
    fun getMaxGeodeCount(time: Int): Int {
        var currentResultMap: HashMap<RobotCount, MutableSet<AmountCount>> = hashMapOf()
        currentResultMap[RobotCount()] = mutableSetOf(AmountCount())
        var maxGeode = 0

        repeat(time) {
            val newResultMap: HashMap<RobotCount, MutableSet<AmountCount>> = hashMapOf()
            val remainingTime = time - it
            currentResultMap.forEach { (robots, amountList) ->
                val checkedAmount = mutableSetOf<AmountCount>()
                val nextStateSet = mutableSetOf<Pair<RobotCount, AmountCount>>()
                amountList.forEach calculation@{ amount ->
                    if (checkedAmount.isNotEmpty() && checkedAmount.all { amount.isSmall(it) }) {
                        return@calculation
                    }
                    if (maxGeode > amount.geode + (remainingTime - 1) * robots.geodeRobot) {
                        return@calculation
                    }

                    checkedAmount.add(amount)
                    val nextAmount = AmountCount(
                        amount.ore + robots.oreRobot,
                        amount.clay + robots.clayRobot,
                        amount.obsidian + robots.obsidianRobot,
                        amount.geode + robots.geodeRobot
                    )
                    nextStateSet.add(robots to nextAmount)

                    if (amount.canBuildOre()) {
                        val nextRobot = robots.copy(oreRobot = robots.oreRobot + 1)
                        val newAmount = nextAmount.copy(ore = nextAmount.ore - oreCostForOre)
                        nextStateSet.add(nextRobot to newAmount)
                    }
                    if (amount.canBuildClay()) {
                        val nextRobot = robots.copy(clayRobot = robots.clayRobot + 1)
                        val newAmount = nextAmount.copy(ore = nextAmount.ore - oreCostForClay)
                        nextStateSet.add(nextRobot to newAmount)
                    }
                    if (amount.canBuildObsidian()) {
                        val nextRobot = robots.copy(obsidianRobot = robots.obsidianRobot + 1)
                        val newAmount = nextAmount.copy(
                            ore = nextAmount.ore - oreCostForObsidian,
                            clay = nextAmount.clay - clayCostForObsidian
                        )
                        nextStateSet.add(nextRobot to newAmount)
                    }
                    if (amount.canBuildGeode()) {
                        val nextRobot = robots.copy(geodeRobot = robots.geodeRobot + 1)
                        val newAmount = nextAmount.copy(
                            ore = nextAmount.ore - oreCostForGeode,
                            obsidian = nextAmount.obsidian - obsidianCostForGeode
                        )
                        nextStateSet.add(nextRobot to newAmount)
                    }
                }
                nextStateSet.forEach { (robots, amount) ->
                    if (newResultMap.containsKey(robots)) {
                        newResultMap[robots]!!.add(amount)
                    } else {
                        newResultMap[robots] = mutableSetOf(amount)
                    }
                }
            }
            maxGeode = newResultMap.maxOf { it.value.maxOf { it.geode } }
            println("[${it + 1}] $maxGeode - ${newResultMap.size}")
            currentResultMap = newResultMap
        }
        return maxGeode
    }

    private fun AmountCount.canBuildOre(): Boolean =
        ore >= oreCostForOre

    private fun AmountCount.canBuildClay(): Boolean =
        ore >= oreCostForClay

    private fun AmountCount.canBuildObsidian(): Boolean =
        clay >= clayCostForObsidian && ore >= oreCostForObsidian

    private fun AmountCount.canBuildGeode(): Boolean =
        obsidian >= obsidianCostForGeode && ore >= oreCostForGeode

    private data class State(val robots: RobotCount, val amount: AmountCount)
    private data class RobotCount(
        val oreRobot: Int = 1,
        val clayRobot: Int = 0,
        val obsidianRobot: Int = 0,
        val geodeRobot: Int = 0
    )

    private data class AmountCount(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0, val geode: Int = 0) {
        fun isSmall(from: AmountCount): Boolean =
            ore <= from.ore && clay <= from.clay && obsidian <= from.obsidian && geode <= from.obsidian
    }
}
