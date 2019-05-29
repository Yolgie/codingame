import java.util.*

class Skeleton(val scanner: Scanner) {
    private lateinit var game: Game

    data class Game(val something: String, var state: GameState)

    data class GameState(val something: String)

    data class Solution(val something: List<String>) {
        override fun toString(): String {
            TODO("add output formatting here")
            return ""
        }
        fun print() {
            something.forEach(::println)
        }
    }

    fun read() : Skeleton {
        this.game = readGame()
        this.game.state = readGameState()
        return this
    }

    private fun readGame(): Game {
        TODO("not implemented")
    }

    private fun readGameState(): GameState {
        TODO("not implemented")
    }

    fun solve() : Solution {
        TODO("not implemented")
    }
}

fun main() {
    Skeleton(Scanner(System.`in`))
        .read()
        .solve()
        .print()
}