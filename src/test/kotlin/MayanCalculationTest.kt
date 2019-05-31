import MayanCalculation.MayanDigit
import MayanCalculation.MayanNumber
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.*

internal class MayanCalculationTest {

    private val mayanCalculation = MayanCalculation(
        Scanner(
            "4 4\n" +
                    ".oo.o...oo..ooo.oooo....o...oo..ooo.oooo....o...oo..ooo.oooo....o...oo..ooo.oooo\n" +
                    "o..o................____________________________________________________________\n" +
                    ".oo.....................................________________________________________\n" +
                    "............................................................____________________\n" +
                    "4\n" +
                    "o...\n" +
                    "....\n" +
                    "....\n" +
                    "....\n" +
                    "4\n" +
                    "o...\n" +
                    "....\n" +
                    "....\n" +
                    "....\n" +
                    "+"
        )
    ).read()

    private val digitZero = MayanDigit(listOf(".oo.", "o..o", ".oo.", "...."))
    private val numberZero = MayanNumber(listOf(mayanCalculation.game.alphabet.get(digitZero)))
    private val digitOne = MayanDigit(listOf("o...", "....", "....", "...."))
    private val digitTwo = MayanDigit(listOf("oo..", "....", "....", "...."))
    private val numberTwenty = MayanNumber(
        listOf(
            mayanCalculation.game.alphabet.get(digitTwo),
            mayanCalculation.game.alphabet.get(digitZero)
        )
    )
    private val numberTwentyOne = MayanNumber(
        listOf(
            mayanCalculation.game.alphabet.get(digitTwo),
            mayanCalculation.game.alphabet.get(digitOne)
        )
    )

    @Test
    fun testAlphabet() {
        assertEquals(20, mayanCalculation.game.alphabet.alphabet.size)
        assertEquals(4, mayanCalculation.game.alphabet.height)
    }

    @Test
    fun testConvertDigitToInt() {
        assertEquals(BigInteger.ONE, mayanCalculation.game.alphabet.get(digitOne).value)
        assertEquals(BigInteger.TWO, mayanCalculation.game.alphabet.get(digitTwo).value)
    }

    @Test
    fun testConvertIntToDigit() {
        assertEquals(digitOne, mayanCalculation.game.alphabet.get(BigInteger.ONE).digit)
        assertEquals(digitTwo, mayanCalculation.game.alphabet.get(BigInteger.TWO).digit)
    }

    @Test
    fun testConvertNumberToInt() {
        assertEquals(BigInteger.valueOf(20 * 2 + 1), numberTwentyOne.integer)
    }

    @Test
    fun testConvertIntToNumber() {
        assertEquals(numberTwentyOne, mayanCalculation.game.alphabet.getNumberForInt(BigInteger.valueOf(20 * 2 + 1)))
    }

    @Test
    fun testZero() {
        assertEquals(digitZero, mayanCalculation.game.alphabet.get(BigInteger.ZERO).digit)
        assertEquals(numberZero, mayanCalculation.game.alphabet.getNumberForInt(BigInteger.valueOf(0)))
        assertEquals(numberTwenty, mayanCalculation.game.alphabet.getNumberForInt(BigInteger.valueOf(40)))

    }

}