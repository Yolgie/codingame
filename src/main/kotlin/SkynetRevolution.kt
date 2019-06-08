import java.util.*

class SkynetRevolution(val scanner: Scanner) {
    private lateinit var game: Game

    class GraphLibrary {

        data class Node(val id: Int, val isExit: Boolean, private val graph: Graph) {
            fun edges() = graph.connectedEdges[this].orEmpty()
            fun connectedNodes() = graph.connectedNodes[this].orEmpty()

            fun addEdge(other: Node): Edge {
                return graph.addEdge(this, other)
            }

            fun removeEdge(other: Node) {
                graph.removeEdge(this, other)
            }
        }

        data class Edge(val id: Int, val connection: Set<Node>, private val graph: Graph) {
            fun next(other: Node): Node {
                return connection.first { it != other }
            }
        }

        data class Path(val edges: List<Edge>, val nodes: List<Node>) {
            fun last() = nodes.last()
        }

        class Graph {
            val connectedNodes = mutableMapOf<Node, MutableList<Node>>()
            val connectedEdges = mutableMapOf<Node, MutableList<Edge>>()
            val edges = mutableListOf<Edge>()

            fun addNode(id: Int, isExit: Boolean): Node {
                val newNode = Node(id, isExit, this)
                connectedNodes.putIfAbsent(newNode, mutableListOf())
                connectedEdges.putIfAbsent(newNode, mutableListOf())
                assertNode(newNode)
                return getNode(id)!!
            }

            fun addOrUpdateNode(id: Int, isExit: Boolean): Node {
                return if (getNode(id) != null) {
                    updateNode(id, isExit)
                } else {
                    addNode(id, isExit)
                }
            }

            fun updateNode(id: Int, isExit: Boolean): Node {
                val newNode = Node(id, isExit, this)
                val oldNode = getNode(newNode.id)!!
                connectedNodes.values
                    .forEach { nodes -> nodes.replaceAll { node -> if (node == oldNode) newNode else node } }
                edges.replaceAll { edge ->
                    if (edge.connection.contains(oldNode)) Edge(
                        edge.id,
                        edge.connection.minus(oldNode).plus(newNode),
                        this
                    ) else edge
                }
                val oldConnectedNodes = connectedNodes[oldNode]!!
                connectedNodes.remove(oldNode)
                connectedNodes[newNode] = oldConnectedNodes
                val oldEdges = connectedEdges[oldNode]!!
                connectedEdges.remove(oldNode)
                connectedEdges.put(newNode, oldEdges)
                return newNode
            }

            fun removeNode(node: Node) {
                connectedNodes.remove(node)
                connectedNodes.values.forEach { nodes -> nodes.remove(node) }
                connectedEdges.remove(node)
                connectedEdges.values.forEach { edges -> edges.removeIf { edge -> edge.connection.contains(node) } }
            }

            fun addEdge(connection: Set<Int>) = addEdge(connection.first(), connection.last())
            fun addEdge(originId: Int, otherId: Int) = addEdgeWithOptionals(getNode(originId), getNode(otherId))

            private fun addEdgeWithOptionals(origin: Node?, other: Node?): Edge {
                assertNodes(origin, other)
                return addEdge(origin!!, other!!)
            }

            fun addEdge(origin: Node, other: Node): Edge {
                assertNodes(origin, other)

                val edge = Edge(edges.size, setOf(origin, other), this)
                connectedNodes[origin]?.add(other)
                connectedNodes[other]?.add(origin)
                connectedEdges[origin]?.add(edge)
                connectedEdges[other]?.add(edge)
                edges.add(edge)
                return edge
            }

            fun removeEdge(edge: Edge) = removeEdge(edge.connection.map { it.id }.toSet())
            fun removeEdge(connection: Set<Int>) = removeEdge(connection.first(), connection.last())
            fun removeEdge(originId: Int, otherId: Int) = removeEdgeWithOptionals(getNode(originId), getNode(otherId))

            private fun removeEdgeWithOptionals(origin: Node?, other: Node?) {
                assertNodes(origin, other)
                return removeEdge(origin!!, other!!)
            }

            fun removeEdge(origin: Node, other: Node) {
                assertNodes(origin, other)

                val edge = getEdge(origin, other)
                connectedNodes[origin]?.remove(other)
                connectedNodes[other]?.remove(origin)
                connectedEdges[origin]?.remove(edge)
                connectedEdges[other]?.remove(edge)
                edges.remove(edge)

                // maybe add graph split detection here and remove vertices/connectedEdges that are not reachable from the agent anymore
            }

            fun getShortestPathsToExit(root: Node): List<List<Edge>> {
                var paths = listOf(Path(emptyList(), listOf(root)))

                while (paths.none { it.last().isExit }) {
                    paths = paths.map(this::expandPath).flatten()
                }

                return paths.filter { it.last().isExit }.filter { it.edges.isNotEmpty() }.map { it.edges }
            }

            fun expandPath(path: Path): List<Path> {
                return path.last()
                    .edges()
                    .filter { newEdge -> !path.edges.contains(newEdge) }
                    .map { newEdge -> Path(path.edges.plus(newEdge), path.nodes.plus(newEdge.next(path.last()))) }
            }

            fun getConnectedNodes(origin: Node): List<Node> {
                assertNode(origin)
                return connectedNodes[origin]!!
            }

            fun getConnectedEdges(origin: Node): List<Edge> {
                assertNode(origin)
                return connectedEdges[origin]!!
            }

            fun getEdge(origin: Node, other: Node): Edge? {
                assertNodes(origin, other)
                return edges.find { it.connection == setOf(origin, other) }
            }

            fun getEdge(id: Int) = edges.find { it.id == id }!!
            fun getNodes() = connectedNodes.keys.toList()
            fun getNode(id: Int) = getNodes().find { it.id == id }
            fun getNodeCount() = connectedNodes.size
            fun getEdgeCount() = edges.size

            fun getExitCount() = connectedNodes.keys.count { node -> node.isExit }

            private fun assertNodes(origin: Node?, other: Node?) {
                assert(origin != other)
                assertNode(origin)
                assertNode(other)
            }

            private fun assertNode(node: Node?) {
                assert(node != null)
                assert(connectedNodes.containsKey(node))
                assert(connectedEdges.containsKey(node))
            }

            private fun throwNotFoundException(id: Int) {
                throw Exception("id $id not present in graph")
            }
        }
    }

    data class Game(val graph: GraphLibrary.Graph)

    data class Agent(val nodeId: Int)

    data class Solution(val edge: GraphLibrary.Edge) {
        override fun toString(): String {
            return edge.connection.map { it.id }.joinToString(" ")
        }

        fun apply(graph: GraphLibrary.Graph): Solution {
            graph.removeEdge(edge)
            return this
        }

        fun print() {
            println(this)
        }
    }

    fun read(): SkynetRevolution {
        this.game = readGame()
        return this
    }

    private fun readGame(): Game {
        val graph = GraphLibrary.Graph()

        val nodeCount = scanner.nextInt() // the total number of nodes in the level, including the gateways
        val linkCount = scanner.nextInt() // the number of links
        val exitCount = scanner.nextInt() // the number of exit gateways

        val nodeIds = mutableListOf<Int>()
        val linkIds = mutableListOf<Set<Int>>()
        val exitIds = mutableListOf<Int>()
        repeat(nodeCount) { id -> nodeIds.add(id) }
        repeat(linkCount) { linkIds.add(setOf(scanner.nextInt(), scanner.nextInt())) }
        repeat(exitCount) { exitIds.add(scanner.nextInt()) }

        nodeIds.forEach { id -> graph.addNode(id, exitIds.contains(id)) }
        linkIds.forEach { connection -> graph.addEdge(connection) }

        return Game(graph)
    }

    private fun readTurn(): Agent {
        return Agent(scanner.nextInt())
    }

    fun gameLoop() {
        while (true) {
            val agent = readTurn()

            val agentNode = game.graph.getNode(agent.nodeId)!!
            val pathsToExit = game.graph.getShortestPathsToExit(agentNode)


            // method: cut first -> works
//            Solution(pathsToExit.first().first()).apply(game.graph).print()

            // method: cut last -> works
//            Solution(pathsToExit.first().last()).apply(game.graph).print()

            // method: cut most common
            Solution(pathsToExit
                .flatten()
                .groupingBy { it }
                .eachCount()
                .maxBy { it.value }!!
                .key
            )
                .apply(game.graph)
                .print()
        }
    }
}

fun main() {
    SkynetRevolution(Scanner(System.`in`))
        .read()
        .gameLoop()
}