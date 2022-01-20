import VoxCodeiEpisodeOne.Coordinate
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class VoxCodeiEpisodeOneTest {

    @Nested
    inner class `Coordinate tests` {
        @Test
        fun `test coordinate range`() {
            val range = (Coordinate(0, 0)..Coordinate(1, 0))
            assertTrue("contains starting coordinate") { range.contains(Coordinate(0, 0)) }
            assertTrue("contains no other coordinates") { range.toList().size == 1 }
        }
    }
}
