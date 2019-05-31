import java.math.BigInteger
import java.util.*

private val BASE = BigInteger.valueOf(20)

class MayanCalculation(val scanner: Scanner) {
    lateinit var game: Game

    enum class Operation(val character: Char, val operation: (BigInteger, BigInteger) -> BigInteger) {
        PLUS('+', { first, second -> first.add(second) }),
        MINUS('-', { first, second -> first.subtract(second) }),
        MULTIPLY('*', { first, second -> first.multiply(second) }),
        DIVITE('/', { first, second -> first.divide(second) });

        companion object {
            fun getOperationBy(character: Char): Operation {
                return values().find { operation -> operation.character == character }
                    ?: throw IllegalArgumentException("Operation not valid")
            }
        }
    }

    data class Game(
        val alphabet: Alphabet,
        val firstNumber: MayanNumber,
        val secondNumber: MayanNumber,
        val operation: Operation
    )

    data class Alphabet(val alphabet: List<MayanNumeral>) {
        val height = alphabet.first().height

        fun get(digit: MayanDigit): MayanNumeral {
            return alphabet.find { numeral -> numeral.digit == digit }
                ?: throw IllegalArgumentException("Digit not found in alphabet")
        }

        fun get(value: BigInteger): MayanNumeral {
            return alphabet.find { numeral -> numeral.value == value }
                ?: throw IllegalArgumentException("Value not found in alphabet")
        }

        fun getNumberForInt(integer: BigInteger): MayanNumber {
            if (integer == BigInteger.ZERO) {
                return MayanNumber(listOf(get(integer)))
            }
            var remaining = integer
            val mayanNumber = mutableListOf<MayanNumeral>()
            while (remaining != BigInteger.ZERO) {
                val (newRemaining, current) = remaining.divideAndRemainder(BASE)
                mayanNumber.add(0, get(current))
                remaining = newRemaining
            }
            return MayanNumber(mayanNumber.toList())
        }
    }

    data class MayanNumeral(val digit: MayanDigit, val value: BigInteger) {
        val height = digit.representation.size
    }

    data class MayanDigit(val representation: List<String>)

    data class MayanNumber(private val mayanNumber: List<MayanNumeral>) {
        val integer = mayanNumber.fold<MayanNumeral, BigInteger>(
            BigInteger.ZERO,
            { sum, number -> sum.multiply(BASE).add(number.value) })

        override fun toString(): String {
            return mayanNumber.map { it.digit.representation.joinToString("\n") }
                .joinToString("\n")
        }

    }

    data class Solution(val mayanNumber: MayanNumber) {
        fun print() {
            print(mayanNumber)
        }
    }

    fun read(): MayanCalculation {
        this.game = readGame()
        return this
    }

    private fun readGame(): Game {
        val alphabet = readAlphabet()
        val firstNumber = readNumber(alphabet)
        val secondNumber = readNumber(alphabet)
        val operation = Operation.getOperationBy(scanner.nextLine().trim().first())

        return Game(alphabet, firstNumber, secondNumber, operation)
    }

    private fun readAlphabet(): Alphabet {
        val alphabet = mutableListOf<MayanNumeral>()
        val width = scanner.nextInt()
        val height = scanner.nextInt()
        scanner.nextLine()
        val alphabetLines = mutableListOf<String>()
        repeat(height) { alphabetLines.add(scanner.nextLine()) }
        repeat(BASE.toInt()) { i ->
            alphabet.add(
                MayanNumeral(
                    MayanDigit(alphabetLines.map { line -> line.substring(i * width until (i + 1) * width) }),
                    BigInteger.valueOf(i.toLong())
                )
            )
        }
        return Alphabet(alphabet.toList())
    }

    private fun readNumber(alphabet: Alphabet): MayanNumber {
        val count = scanner.nextLine().toInt().div(alphabet.height)
        val digits = mutableListOf<MayanNumeral>()
        repeat(count) { digits.add(readDigit(alphabet)) }
        return MayanNumber(digits.toList())
    }

    private fun readDigit(alphabet: Alphabet): MayanNumeral {
        val digitLines = mutableListOf<String>()
        repeat(alphabet.height) { digitLines.add(scanner.nextLine()) }
        return alphabet.get(MayanDigit(digitLines))
    }

    fun solve(): Solution {
        val resultAsInt = game.operation.operation.invoke(game.firstNumber.integer, game.secondNumber.integer)
        return Solution(game.alphabet.getNumberForInt(resultAsInt))
    }
}

fun main() {
    MayanCalculation(Scanner(System.`in`))
        .read()
        .solve()
        .print()
}
