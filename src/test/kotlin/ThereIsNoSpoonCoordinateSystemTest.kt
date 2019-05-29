import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import ThereIsNoSpoon.*

internal class ThereIsNoSpoonCoordinateSystemTest {

    private val smallGame = Game(
        2,
        2,
        GameState(
            CoordinateSystem(
                mapOf(
                    Coordinate(0, 0) to Node('0'),
                    Coordinate(0, 1) to Node('0'),
                    Coordinate(1, 0) to Node('0'),
                    Coordinate(1, 1) to Node('.')
                )
            )
        )
    )

    @TestFactory
    fun getClosestHorizontalNeighbor() = listOf(
        Coordinate(0, 0) to Coordinate(1, 0),
        Coordinate(1, 0) to Coordinate(-1, -1),
        Coordinate(0, 1) to Coordinate(-1, -1)
    )
        .map { (input, expected) ->
            DynamicTest.dynamicTest("the closest horizontal neighbor to $input should be $expected") {
                Assertions.assertEquals(
                    expected,
                    smallGame.gameState.coordinateSystem.getClosestHorizontalNeighbor(input)
                )
            }
        }

    @TestFactory
    fun getClosestVerticalNeighbor() = listOf(
        Coordinate(0, 0) to Coordinate(0, 1),
        Coordinate(1, 0) to Coordinate(-1, -1),
        Coordinate(0, 1) to Coordinate(-1, -1)
    )
        .map { (input, expected) ->
            DynamicTest.dynamicTest("the closest vertical neighbor to $input should be $expected") {
                Assertions.assertEquals(
                    expected,
                    smallGame.gameState.coordinateSystem.getClosestVerticalNeighbor(input)
                )
            }
        }
}