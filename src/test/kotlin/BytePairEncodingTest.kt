import BytePairEncoding.Rule
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class BytePairEncodingTest {
    @Test
    fun `test original example`() {
        val input = "aaabdaaabac"
        val expected = "XdXac"
        val expectedRules = listOf(
            Rule("aa", 'Z'),
            Rule("Za", 'Y'),
            Rule("Yb", 'X')
        )

        val result = BytePairEncoding(input).solve()

        assertEquals(expected, result.encoded)
        assertEquals(expectedRules, result.rules)
    }

    @Test
    fun `test second example`() {
        val input = "aedcaafffbddcaaacdcd"
        val expected = "aeXfffbdXacZd"
        val expectedRules = listOf(
            Rule("dc", 'Z'),
            Rule("Za", 'Y'),
            Rule("Ya", 'X')
        )

        val result = BytePairEncoding(input).solve()

        assertEquals(expected, result.encoded)
        assertEquals(expectedRules, result.rules)
    }

    @Nested
    inner class `test findPairs` {
        @Test
        fun `test example case`() {
            val input = "aaabdaaabac"
            val expected = listOf("aa" to 2, "ab" to 2)

            val pairs = BytePairEncoding().findPairs(input)

            assertEquals(expected, pairs)
        }

        @Test
        fun `test with triple`() {
            val input = "aabaaa"
            val expected = listOf("aa" to 2)

            assertEquals(expected, BytePairEncoding().findPairs(input))
        }

        @Test
        fun `test with quadruples`() {
            val input = "aaaa"
            val expected = listOf("aa" to 2)

            assertEquals(expected, BytePairEncoding().findPairs(input))
        }

        @Test
        fun `test with quintuple`() {
            val input = "aaaaa"
            val expected = listOf("aa" to 2)

            assertEquals(expected, BytePairEncoding().findPairs(input))
        }

        @Test
        fun `test with sextuple`() {
            val input = "aaaaaa"
            val expected = listOf("aa" to 3)

            assertEquals(expected, BytePairEncoding().findPairs(input))
        }

        @Test
        fun `test with septuple`() {
            val input = "aaaaaaa"
            val expected = listOf("aa" to 3)

            assertEquals(expected, BytePairEncoding().findPairs(input))
        }
    }
}
