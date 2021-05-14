import java.util.*

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val numberOfCells = input.nextInt() // 37
    val map = mutableListOf<Ground>()
    for (i in 0 until numberOfCells) {
        val index = input.nextInt() // 0 is the center cell, the next cells spiral outwards
        val richness = input.nextInt() // 0 if the cell is unusable, 1-3 for usable cells
        val neigh0 = input.nextInt() // the index of the neighbouring cell for each direction
        val neigh1 = input.nextInt()
        val neigh2 = input.nextInt()
        val neigh3 = input.nextInt()
        val neigh4 = input.nextInt()
        val neigh5 = input.nextInt()
        map.add(Ground(index, richness))
    }

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
        val trees = mutableListOf<Tree>()
        for (i in 0 until numberOfTrees) {
            val index = input.nextInt()
            trees.add(
                Tree(
                    index = index, // location of this tree
                    size = input.nextInt(), // size of this tree: 0-3
                    mine = input.nextInt() != 0, // 1 if this is your tree
                    dormant = input.nextInt() != 0, // 1 if this tree is dormant
                    richness = map.find { ground -> ground.index == index }!!.richness
                )
            )
        }
        val numberOfPossibleActions = input.nextInt() // all legal actions
        if (input.hasNextLine()) {
            input.nextLine()
        }
        for (i in 0 until numberOfPossibleActions) {
            val possibleAction = input.nextLine() // try printing something from here to start with
            System.err.println(possibleAction)
        }

        // Write an action using println()
        // To debug: System.err.println("Debug messages...");

        val richestTree = trees
            .filter(Tree::mine)
            .maxBy(Tree::richness)!!
        if (sun >= 3) {
            println("COMPLETE ${richestTree.index}")
        } else {
            println("WAIT")
        }
        // GROW cellIdx | SEED sourceIdx targetIdx | COMPLETE cellIdx | WAIT <message>
    }
}

data class Tree(val index: Int, val size: Int, val mine: Boolean, val dormant: Boolean, val richness: Int)
data class Ground(val index: Int, val richness: Int)
