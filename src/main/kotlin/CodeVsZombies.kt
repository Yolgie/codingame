import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Save humans, destroy zombies!
 **/
class CodeVsZombies(val scanner: Scanner) {
    fun read(): GameState {
        val player = Player(
            Coordinate(
                x = scanner.nextInt(),
                y = scanner.nextInt()
            )
        )
        val humanCount = scanner.nextInt()
        val humans = (0 until humanCount).map {
            NPC(
                id = scanner.nextInt(),
                coordinate = Coordinate(
                    x = scanner.nextInt(),
                    y = scanner.nextInt()
                )
            )

        }
        val zombieCount = scanner.nextInt()
        val zombies = (0 until zombieCount).map {
            Zombie(
                id = scanner.nextInt(),
                coordinate = Coordinate(
                    x = scanner.nextInt(),
                    y = scanner.nextInt()
                ),
                nextCoordinate = Coordinate(
                    x = scanner.nextInt(),
                    y = scanner.nextInt()
                )
            )
        }
        return GameState(player, humans, zombies)
    }

    fun GameState.run(): Coordinate {
        val allHumans = humans + player
        // calculate which human is targeted by each zombie
        val zombieTargets = zombies.map { zombie ->
            zombie to (allHumans.minBy { human -> zombie.distance(human) } ?: throw IllegalStateException())
        }.toMap()
        val zombiesTargeting = allHumans.map { human ->
            human to zombieTargets
                .filterValues { target -> human == target }
                .keys
        }.toMap()

        return zombiesTargeting.filterValues { zombies -> zombies.isNotEmpty() }
            .keys.first().coordinate
    }

    fun Coordinate.print() {
        println(this)
    }

    class Game {
        val xMax = 16000 // width
        val yMax = 9000  // height
    }

    interface HasCoordinate {
        val coordinate: Coordinate
        fun distance(to: HasCoordinate) = coordinate.distance(to.coordinate)
        fun distance(to: Coordinate) = coordinate.distance(to)
    }

    interface Human : HasCoordinate {
        val id: Int
    }

    data class GameState(val player: Player, val humans: List<NPC>, val zombies: List<Zombie>)
    data class NPC(override val id: Int, override val coordinate: Coordinate) : Human
    data class Player(override val coordinate: Coordinate) : Human {
        override val id = -1
    }

    data class Zombie(val id: Int, override val coordinate: Coordinate, val nextCoordinate: Coordinate) : HasCoordinate

    data class Coordinate(val x: Int, val y: Int) {
        override fun toString(): String {
            return "$x $y"
        }

        fun distance(to: Coordinate): Int = sqrt((to.x - this.x).pow(2) + (to.y - this.y).pow(2)).toInt()
    }

}

private fun Int.pow(x: Int): Double = this.toDouble().pow(x)

fun main(args: Array<String>) {
    val codeVsZombies = CodeVsZombies(Scanner(System.`in`))
    while (true) {
        with(codeVsZombies) {
            this.read()
                .run()
                .print()
        }
    }
}
