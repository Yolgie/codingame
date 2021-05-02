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
        val distances = Distances(zombies, allHumans)
        // calculate which human is targeted by each zombie
        val zombieTargets = zombies.map { zombie ->
            zombie to (distances.getAllFor(zombie)
                .minBy { (_, distance) -> distance }
                ?: throw IllegalStateException())
        }.toMap()
        System.err.println("zombieTargets: $zombieTargets")
        assert(zombieTargets.keys.containsAll(zombies))
        val zombiesTargeting = allHumans.map { human ->
            human to zombieTargets
                .filterValues { (target, _) -> human == target }
                .keys
        }.toMap()
            .filterValues { zombies -> zombies.isNotEmpty() }
        System.err.println("zombiesTargeting: $zombiesTargeting")
        // calculate lost humans
        val closestApproachingZombie = zombiesTargeting.map { (human, zombies) ->
            human to (zombies.minBy { zombie -> distances.getFor(zombie, human) } ?: throw IllegalStateException())
        }.toMap()
        System.err.println("closestApproaching: $closestApproachingZombie")
        val lost = closestApproachingZombie
            .filter { (human, _) -> human != player }
            .filter { (human, zombie) ->
                System.err.println(
                    "islost: ${distances.getFor(zombie, human)}(${
                        distances.getFor(zombie, human) / 400
                    }) vs ${distances.getForPlayer(human)}-2000(${(distances.getForPlayer(human) - 2000) / 1000}) -> ${
                        distances.getFor(zombie, human) / 400 < (distances.getForPlayer(human) - 2000) / 1000
                    }"
                )
                distances.getFor(zombie, human) / zombieStepSize < (distances.getForPlayer(human) - playerKillRange) / playerStepSize
            }

        System.err.println("all zombies $zombies")
        System.err.println("lost $lost")

        val target = zombiesTargeting
            .filterKeys { human -> human != player }
            .filterValues { zombies -> zombies.isNotEmpty() }
            .filterKeys { human -> human !in lost.keys }
            .keys.firstOrNull()
            ?: humans.minBy { human -> distances.getForPlayer(human) }
            ?: throw IllegalStateException()
        System.err.println("going towards: $target (${distances.getForPlayer(target)}")
        if (distances.getForPlayer(target) < zombieStepSize) {
            // check if more points can be made by approaching zombies
            val closeZombies = zombies.filter { zombie ->
                distances.getForPlayer(zombie) < playerStepSize + playerKillRange
            }
            if (closeZombies.isNotEmpty()) {
                return closeZombies.sortedByDescending(distances::getForPlayer).first().nextCoordinate
            }
        }
        return target.coordinate
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

    class Distances(zombies: List<Zombie>, humans: List<Human>) {
        private val player = humans.single { it.id == -1 }
        private val distances = zombies.flatMap { zombie ->
            humans.map { human ->
                Pair(zombie, human) to zombie.distance(human)
            }
        }.toMap()
        private val distancesFromZombies = zombies.associateWith { target ->
            distances.filterKeys { (zombie, _) -> zombie == target }
                .mapKeys { (zombieHumanPair, _) -> zombieHumanPair.let { (_, human) -> human } }
                .toMap()
        }
        private val distancesFromHumans = humans.associateWith { target ->
            distances.filterKeys { (_, human) -> human == target }
                .mapKeys { (zombieHumanPair, _) -> zombieHumanPair.let { (zombie, _) -> zombie } }
                .toMap()
        }
        private val humanDistancesFromPlayer = humans.associateWith { human -> player.distance(human) }

        fun getAll() = distances
        fun getFor(zombie: Zombie, human: Human) = distances[Pair(zombie, human)] ?: throw IllegalStateException()
        fun getAllFor(target: Zombie) = distancesFromZombies[target] ?: throw IllegalStateException()
        fun getAllFor(target: Human) = distancesFromHumans[target] ?: throw IllegalStateException()
        fun getForPlayer(human: Human): Int = humanDistancesFromPlayer[human] ?: throw IllegalStateException()
        fun getForPlayer(zombie: Zombie): Int = getFor(zombie, player)
    }

    companion object {
        const val playerStepSize = 1000
        const val zombieStepSize = 400
        const val playerKillRange = 2000
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
