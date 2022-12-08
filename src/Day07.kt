fun main() {
    val testInput = readInput("Day07_test")

    println(part1(testInput))
    println(part2(testInput))
}

private fun createDirMap(input: List<String>): Map<String, Int> {
    val dirMap = hashMapOf("/" to 0)

    var currentDirName = "/"
    input.forEach {
        if (it.startsWith("$ cd /")) return@forEach
        if (it.startsWith("$ ls")) return@forEach
        if (it.startsWith("$ cd ..")) {
            currentDirName = currentDirName.replace("""/[a-z.]*$""".toRegex(), "")
            return@forEach
        }
        if (it.startsWith("$ cd ")) {
            currentDirName += '/' + it.replace("$ cd ", "")
            return@forEach
        }
        if (it.startsWith("dir ")) {
            val dirName = it.replace("dir ", "")
            dirMap["${currentDirName}/$dirName"] = 0
        } else {
            val size = it.replaceFirst(""" .*""".toRegex(), "").toInt()
            var dirName = currentDirName
            while (dirName != "/") {
                dirMap[dirName] = dirMap.getOrDefault(dirName, 0) + size
                dirName = dirName.replace("""/[a-z.]*$""".toRegex(), "")
            }
            dirMap["/"] = dirMap.getOrDefault("/", 0) + size
        }
    }
    return dirMap
}

private fun part1(input: List<String>): Int {
    val dirMap = createDirMap(input)
    println(dirMap)
    return dirMap.values.filter { it < 100000 }.sum()
}

private fun part2(input: List<String>): Int {
    val dirMap = createDirMap(input)
    val totalSize = dirMap["/"] ?: return 0
    val remaining = totalSize - 40000000
    if (remaining <= 0) {
        return 0
    }

    return dirMap.values.sorted().first { it > remaining }
}
