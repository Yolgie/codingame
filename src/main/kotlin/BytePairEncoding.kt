import java.util.*
import java.util.regex.Pattern

class BytePairEncoding(val input: String = "") {
    fun solve(): Solution {
        return refineSolution(Solution(input))
    }

    fun refineSolution(solution: Solution): Solution {
        val nextPair = findPairs(solution.encoded).maxBy { (_, count) -> count }?.first
        return if (nextPair != null) {
            val nextReplacementChar = solution.nextReplacementChar()
            val newInput = solution.encoded.replace(nextPair, nextReplacementChar.toString())
            val newRule = Rule(nextPair, nextReplacementChar)

            refineSolution(Solution(newInput, solution.rules + newRule))
        } else {
            solution
        }
    }

    fun findPairs(input: String): List<Pair<String, Int>> {
        return input
            .zipWithNext { a, b -> "$a$b" }
            .toSet()
            .map { pair -> pair to input.countMatches(pair) }
            .filter { (_, count) -> count > 1 }
    }

    private fun String.countMatches(pattern: String): Int {
        val matcher = Pattern.compile(pattern).matcher(this)

        var count = 0
        while (matcher.find()) {
            count++
        }
        return count
    }

    data class Solution(val encoded: String, val rules: List<Rule> = emptyList()) {

        fun nextReplacementChar(): Char = rules.map(Rule::replacement).min()?.dec() ?: 'Z'

        fun print() = println(this)

        override fun toString(): String {
            return "$encoded\n${rules.joinToString("\n")}"
        }
    }

    data class Rule(val pair: String, val replacement: Char) {
        override fun toString(): String {
            return "$replacement = $pair"
        }
    }
}

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val count = input.nextInt()
    input.nextInt() // ignored/not needed
    val inputString = (1..count+1).map { input.nextLine() }.joinToString("")
    BytePairEncoding(inputString)
        .solve()
        .print()
}
