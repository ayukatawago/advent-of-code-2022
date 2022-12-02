import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        var maxCalories = 0

        var calories = 0
        input.forEach { calory ->
            if (calory.isEmpty()) {
                maxCalories = max(maxCalories, calories)
                calories = 0
            } else {
                calories += calory.toInt()
            }
        }
        return maxCalories
    }

    fun part2(input: List<String>): Int {
        val caloriesList = mutableListOf<Int>()

        var calories = 0
        input.forEach { calory ->
            if (calory.isEmpty()) {
                caloriesList.add(calories)
                calories = 0
            } else {
                calories += calory.toInt()
            }
        }
        caloriesList.add(calories)

        return caloriesList.sorted().takeLast(3).sum()
    }

    val testInput = readInput("Day01_test")

    println(part1(testInput))
    println(part2(testInput))
}
