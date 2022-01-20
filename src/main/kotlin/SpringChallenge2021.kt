import java.lang.System.err
import java.util.*

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val numberOfCells = input.nextInt() // 37
    val map = mutableMapOf<Int, GameCell>()
    for (i in 0 until numberOfCells) {
        val index = input.nextInt()
        map[index] = GameCell(
            index = index,  // 0 is the center cell, the next cells spiral outwards
            richness = input.nextInt(), // 0 if the cell is unusable, 1-3 for usable cells
            neighbors = mapOf( // the index of the neighbouring cell for each direction
                Direction.E to input.nextInt(),
                Direction.NE to input.nextInt(),
                Direction.NW to input.nextInt(),
                Direction.W to input.nextInt(),
                Direction.SW to input.nextInt(),
                Direction.SE to input.nextInt()
            )
        )
    }
    val game = Game(map)

    // game loop
    while (true) {
        val day = input.nextInt() // the game lasts 24 days: 0-23
        val nutrients = input.nextInt() // the base score you gain from the next COMPLETE action
        val sun = input.nextInt() // your sun points
        val score = input.nextInt() // your current score
        val oppSun = input.nextInt() // opponent's sun points
        val oppScore = input.nextInt() // opponent's score
        val oppIsWaiting = input.nextInt() != 0 // whether your opponent is asleep until the next day
        val numberOfTrees = input.nextInt() // the current amount of trees
        val treeInputs = mutableListOf<TreeInput>()
        for (i in 0 until numberOfTrees) {
            treeInputs += TreeInput(
                gameCell = game.get(input.nextInt()), // location of this tree
                size = input.nextInt(), // size of this tree: 0-3
                mine = input.nextInt() != 0, // 1 if this is your tree
                dormant = input.nextInt() != 0 // 1 if this tree is dormant
            )
        }
        val numberOfPossibleActions = input.nextInt() // all legal actions
        if (input.hasNextLine()) {
            input.nextLine()
        }
        for (i in 0 until numberOfPossibleActions) {
            val possibleAction = input.nextLine() // try printing something from here to start with
            err.println(possibleAction)
        }

        val trees = treeInputs
            .map { treeInput ->
                treeInput.gameCell.index to Tree(
                    treeInput,
                    Cost.grow(treeInput.size+1, treeInputs)
                )
            }.toMap()

        // todo optimization -> as soon as 2 grown trees exist, harvest them both because a third will not happen...

        if (trees.values.filter(Tree::mine).count { tree -> tree.size == 3 } == 2 && sun >= Cost.harvest) {
            trees
                .values
                .filter(Tree::mine)
                .filter { tree -> tree.size == 3 }
                .maxBy { tree -> tree.gameCell.richness }
                ?.also { println("COMPLETE ${it.gameCell.index}") }
                ?: println("WAIT")
        }
        if (day == 5 && sun >= Cost.harvest) {
            // harvest as long as possible
            trees
                .values
                .filter(Tree::mine)
                .filter { tree -> tree.size == 3 }
                .maxBy { tree -> tree.gameCell.richness }
                ?.also { println("COMPLETE ${it.gameCell.index}") }
                ?: println("WAIT")
        } else {
            // grow richest now grown tree
            trees
                .values
                .filter(Tree::mine)
                .maxBy { tree -> tree.growCost }
                ?.also { println("GROW ${it.gameCell.index}") }
                ?: println("WAIT")
        }
        // GROW cellIdx | SEED sourceIdx targetIdx | COMPLETE cellIdx | WAIT <message>
    }
}

data class TreeInput(val gameCell: GameCell, override val size: Int, val mine: Boolean, val dormant: Boolean) : HasTreeSize
data class Tree(val gameCell: GameCell, override val size: Int, val mine: Boolean, val dormant: Boolean, val growCost: Int) : HasTreeSize {
    constructor(treeInput: TreeInput, growCost: Int) : this(
        treeInput.gameCell,
        treeInput.size,
        treeInput.mine,
        treeInput.dormant,
        growCost
    )
}
interface HasTreeSize {
    val size: Int
}
data class GameCell(val index: Int, val richness: Int, val neighbors: Map<Direction, Int>)
data class Game(val gameCells: Map<Int, GameCell>) {
    fun get(index: Int) = gameCells[index] ?: throw IllegalStateException("All game cells should exist in the map")
}

enum class Direction { E, NE, NW, W, SW, SE }

object Cost {
    const val harvest = 4
    fun grow(newTreeSize: Int, trees: List<HasTreeSize>): Int {
        return when (newTreeSize) {
            1 -> 1 + trees.count { it.size == newTreeSize }
            2 -> 3 + trees.count { it.size == newTreeSize }
            3 -> 7 + trees.count { it.size == newTreeSize }
            else -> -1
        }
    }
}
