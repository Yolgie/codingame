import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class VoxCodeiEpisodeOne(private val scanner: Scanner) {
    private val firewallGrid: FirewallGrid = readGame(scanner)

    fun initialState(): GameState = GameState(emptyList(), firewallGrid.initialSurveillanceNodes, -1, -1)

    fun GameState.read(): GameState {
        val roundsLeft = scanner.nextInt() // number of rounds left before the end of the game
        val bombsLeft = scanner.nextInt() // number of bombs left

        return this.copy(bombsLeft = bombsLeft, roundsLeft = roundsLeft)
    }

    fun GameState.run(): GameState {

        return this
    }

    fun GameState.print(): GameState {
        System.err.println(this)
        System.err.print(this.renderMap())

        bombs.singleOrNull() { bomb -> bomb.timeRemaining == bombFuseTime }
            ?.apply { println(coordinate) }
        return this
    }

    fun GameState.tick(): GameState { // count down all bomb timers & explode them if they reach 0
        var newBombs = bombs
        var explodingBombs = newBombs.filter { bomb -> bomb.timeRemaining == 1 }
        while (explodingBombs.isNotEmpty()) {
            // evaluate explosion and remove exploded surveillance nodes / add triggered bombs to list
            // TODO explodingBombs.map { bomb -> bomb.getExplosionRadius(firewallGrid.passiveNodes) }
            newBombs = newBombs.map { bomb -> if (bomb in explodingBombs) bomb.copy(timeRemaining = 0) else bomb }
            explodingBombs = newBombs.filter { bomb -> bomb.timeRemaining == 1 }
        }

        return this
    }

    fun GameState.renderMap(): String {
        val output = StringBuilder()
        val map = mutableMapOf<Coordinate, Char>()
        surveillanceNodes.forEach { coordinate -> map.put(coordinate, surveillanceNodeSymbol) }
        firewallGrid.passiveNodes.forEach { coordinate -> map.put(coordinate, passiveNodeSymbol) }
        bombs.forEach { bomb -> map.put(bomb.coordinate, bomb.timeRemaining.toString().first()) }

        (0..firewallGrid.yMax).forEach { y ->
            (0..firewallGrid.xMax).forEach { x ->
                output.append(map.getOrDefault(Coordinate(x, y), '.'))
            }
            output.appendln()
        }
        return output.toString()
    }


    data class GameState(
        val bombs: List<Bomb>,
        val surveillanceNodes: List<Coordinate>,
        val bombsLeft: Int,
        val roundsLeft: Int
    ) {
        override fun toString(): String =
            "Current GameState(surveillanceNodes=${surveillanceNodes.count()}, bombs=${bombs.count()}, bombsLeft=$bombsLeft, roundsLeft=$roundsLeft)"
    }

    data class Bomb(val coordinate: Coordinate, val timeRemaining: Int)
    data class FirewallGrid(
        val xMax: Int, // width
        val yMax: Int, // height
        val passiveNodes: List<Coordinate>,
        val initialSurveillanceNodes: List<Coordinate>
    )

    private fun Bomb.getExplosionRadius(): List<Coordinate> {
        fun Bomb.getExplosionRadius(direction: Coordinate): List<Coordinate> {
            (this.coordinate..direction)
            firewallGrid
            return emptyList()
        }

        val explosionRadius = mutableListOf<Coordinate>()
        explosionRadius += this.getExplosionRadius(Coordinate(+1, 0))
        this.coordinate
        firewallGrid.passiveNodes
        return emptyList()

    }


    data class Coordinate(val x: Int, val y: Int) : Comparable<Coordinate> {
        fun distance(to: Coordinate): Double =
            sqrt((to.x - this.x).toDouble().pow(2) + (to.y - this.y).toDouble().pow(2))

        override fun toString(): String {
            return "$x $y"
        }

        override fun compareTo(other: Coordinate): Int = when {
            y != other.y -> (y - other.y)
            else -> (x - other.x)
        }

        operator fun rangeTo(other: Coordinate) = CoordinateRange(this, other)
    }

    class CoordinateRange(
        override val start: Coordinate,
        override val endInclusive: Coordinate
    ) : ClosedRange<Coordinate>, Iterable<Coordinate> {
        override fun iterator(): Iterator<Coordinate> {
            return CoordinateIterator(start, endInclusive)
        }
    }

    class CoordinateIterator(
        val start: Coordinate,
        val endInclusive: Coordinate
    ) : Iterator<Coordinate> {
        var current = start

        override fun hasNext(): Boolean = current <= endInclusive

        override fun next(): Coordinate {
            val newX = if (endInclusive.x > current.x) current.x + 1 else current.x
            val newY = if (endInclusive.y > current.y) current.y + 1 else current.y
            current = Coordinate(newX, newY)
            return current
        }

    }

    companion object {
        const val bombFuseTime = 3
        const val bombRange = 3
        const val waitCommand = "WAIT"
        const val surveillanceNodeSymbol = '@'
        const val passiveNodeSymbol = '#'

        private fun readGame(input: Scanner): VoxCodeiEpisodeOne.FirewallGrid {
            val width = input.nextInt() // width of the firewall grid
            val height = input.nextInt() // height of the firewall grid
            input.nextLine() // finish reading the line
            val map = mutableListOf<String>()
            val passiveNodes = mutableListOf<Coordinate>()
            val surveillanceNodes = mutableListOf<Coordinate>()
            for (y in 0 until height) {
                val mapLine = input.nextLine() // one line of the firewall grid
                System.err.println(mapLine)
                map += mapLine
                mapLine.forEachIndexed { x, mapPoint ->
                    when (mapPoint) {
                        passiveNodeSymbol -> passiveNodes.add(Coordinate(x, y))
                        surveillanceNodeSymbol -> surveillanceNodes.add(Coordinate(x, y))
                    }
                }
            }
            return FirewallGrid(width, height, passiveNodes, surveillanceNodes)
        }
    }
}

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args: Array<String>) {
    val voxCodei = VoxCodeiEpisodeOne(Scanner(System.`in`))
    var state = voxCodei.initialState()
    while (true) {
        with(voxCodei) {
            state = state
                .read()
                .run()
                .print()
                .tick()
        }
    }
}
