import java.util.*

class ThereIsNoSpoon(val inputScanner: Scanner) {
    private lateinit var game: Game

    data class Game(val width: Int, val height: Int, val gameState: GameState)

    data class GameState(val coordinateSystem: CoordinateSystem)

    data class CoordinateSystem(val coordinates: Map<Coordinate, Node>) {
        val NO_NEIGHBOR = Coordinate(-1, -1)

        fun getClosestHorizontalNeighbor(root: Coordinate): Coordinate {
            assert(root in coordinates)
            return coordinates
                .filterValues { node -> node.active }
                .filterKeys { coordinate -> coordinate.y == root.y }
                .filterKeys { coordinate -> coordinate.x > root.x }
                .minBy { (coordinate, _) -> coordinate.x }
                ?.key ?: NO_NEIGHBOR
        }

        fun getClosestVerticalNeighbor(root: Coordinate): Coordinate {
            assert(root in coordinates)
            return coordinates
                .filterValues { node -> node.active }
                .filterKeys { coordinate -> coordinate.x == root.x }
                .filterKeys { coordinate -> coordinate.y > root.y }
                .minBy { (coordinate, _) -> coordinate.y }
                ?.key ?: NO_NEIGHBOR
        }
    }

    data class Coordinate(val x: Int, val y: Int) {
        override fun toString(): String {
            return "$x $y"
        }
    }

    data class Node(val active: Boolean) {
        constructor(initializer: Char) : this(initializer == '0')
    }

    fun read(): ThereIsNoSpoon {
        readGame()
        return this
    }

    fun solve() {
        game.gameState.coordinateSystem.coordinates
            .filterValues { it.active }
            .keys
            .map { coordinate ->
                listOf(
                    coordinate,
                    game.gameState.coordinateSystem.getClosestHorizontalNeighbor(coordinate),
                    game.gameState.coordinateSystem.getClosestVerticalNeighbor(coordinate)
                )
            }
            .map { it.joinToString(" ") }
            .forEach(::println)
    }

    private fun readGame() {
        val width = inputScanner.nextInt() // the number of cells on the X axis
        val height = inputScanner.nextInt() // the number of cells on the Y axis
        if (inputScanner.hasNextLine()) {
            inputScanner.nextLine()
        }
        this.game = Game(width, height, readInitialGameState(width, height))
    }

    fun readInitialGameState(width: Int, height: Int): GameState {
        val coordinateSystem = mutableMapOf<Coordinate, Node>()
        for (y in 0 until height) {
            val line = inputScanner.nextLine() // width characters, each either 0 or .
            System.err.print(line)
            for ((x, node) in line.withIndex()) {
                coordinateSystem[Coordinate(x, y)] = Node(node)
            }
        }
        System.err.println()
        System.err.println(coordinateSystem.keys.joinToString(", "))
        return GameState(CoordinateSystem(coordinateSystem.toMap()))
    }

}

fun main() {
    ThereIsNoSpoon(Scanner(System.`in`))
        .read()
        .solve()
}
