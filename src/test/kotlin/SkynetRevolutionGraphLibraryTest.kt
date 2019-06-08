import org.junit.jupiter.api.Assertions.*
import SkynetRevolution.GraphLibrary.*
import org.junit.jupiter.api.Test

internal class SkynetRevolutionGraphLibraryTest {

    @Test
    fun addAndRemoveNodes() {
        val graph = Graph()
        assertNumberOfNodes(0, graph)
        assertNumberOfEdges(0, graph)
        assertNumberOfExits(0, graph)

        val one = graph.addNode(0, false)
        assertNumberOfNodes(1, graph)
        assertNumberOfEdges(0, graph)
        assertNumberOfExits(0, graph)

        val two = graph.addNode(1, true)
        assertNumberOfNodes(2, graph)
        assertNumberOfEdges(0, graph)
        assertNumberOfExits(1, graph)

        graph.addNode(2, true)
        assertNumberOfNodes(3, graph)
        assertNumberOfEdges(0, graph)
        assertNumberOfExits(2, graph)

        graph.removeNode(one)
        assertNumberOfNodes(2, graph)
        assertNumberOfEdges(0, graph)
        assertNumberOfExits(2, graph)

        graph.removeNode(two)
        assertNumberOfNodes(1, graph)
        assertNumberOfEdges(0, graph)
        assertNumberOfExits(1, graph)
    }

    @Test
    fun updateNode() {
        val graph = Graph()

        graph.addNode(0, false)
        assertNumberOfNodes(1, graph)
        assertNumberOfExits(0, graph)

        graph.addOrUpdateNode(0, true)
        assertNumberOfNodes(1, graph)
        assertNumberOfExits(1, graph)

        graph.updateNode(0, false)
        assertNumberOfNodes(1, graph)
        assertNumberOfExits(0, graph)
    }

    @Test
    fun addAndRemoveEdges() {
        val graph = Graph()
        graph.addNode(0,false)
        graph.addNode(1, false)
        graph.addNode(2, true)
        graph.addNode(3, false)
        assertNumberOfNodes(4, graph)
        assertNumberOfEdges(0, graph)
        assertNumberOfExits(1, graph)

        graph.addEdge(0, 1)
        graph.addEdge(1,2)
        graph.addEdge(2,3)
        assertNumberOfNodes(4, graph)
        assertNumberOfEdges(3, graph)
        assertNumberOfExits(1, graph)

        assertDirectlyConnected(0, 1, graph)
        assertDirectlyConnected(1, 2, graph)
        assertDirectlyConnected(2, 3, graph)
        assertDirectlyConnected(1,0, graph)
        assertDirectlyConnected(2,1, graph)
        assertDirectlyConnected(3,2, graph)

        graph.removeEdge(0,1)
        assertNumberOfNodes(4, graph)
        assertNumberOfEdges(2, graph)
        assertNumberOfExits(1, graph)

        graph.removeEdge(0,1)
        assertNumberOfNodes(4, graph)
        assertNumberOfEdges(2, graph)
        assertNumberOfExits(1, graph)
    }

    @Test
    fun findExitPaths() {
        val graph = Graph()
        val root = graph.addNode(0, false)
        val top = graph.addNode(1, false)
        val exit = graph.addNode(2, true)
        val bottom = graph.addNode(3, false)
        graph.addEdge(0, 1)
        graph.addEdge(1,2)
        graph.addEdge(2,3)
        graph.addEdge(3,0)

        val pathsFromRoot = graph.getShortestPathsToExit(root)
        assertEquals(2, pathsFromRoot.size)

        val pathsFromTop = graph.getShortestPathsToExit(top)
        assertEquals(1, pathsFromTop.size)

        val pathsFromBottom = graph.getShortestPathsToExit(bottom)
        assertEquals(1, pathsFromBottom.size)

        val pathsFromExit = graph.getShortestPathsToExit(exit)
        assertEquals(0, pathsFromExit.size)
    }

    @Test
    fun expandPath() {
        val graph = Graph()
        val root = graph.addNode(0, false)
        val top = graph.addNode(1, false)
        val exit = graph.addNode(2, true)
        val bottom = graph.addNode(3, false)
        graph.addEdge(0, 1)
        graph.addEdge(1,2)
        graph.addEdge(2,3)
        graph.addEdge(3,0)

        val initialPath = Path(listOf(), listOf(root))
        val expandedPath = graph.expandPath(initialPath)

        assertEquals(2, expandedPath.size)
        assertTrue(expandedPath.any { it.nodes.contains(top) })
        assertTrue(expandedPath.any { it.nodes.contains(bottom) })
        assertFalse(expandedPath.any { it.nodes.contains(exit) })
    }

    private fun assertDirectlyConnected(originId: Int, destinationId: Int, graph: Graph) {
        val origin = graph.getNode(originId)!!
        val destination = graph.getNode(destinationId)!!
        assertTrue(graph.getConnectedNodes(origin).contains(destination), "Expects destination to be in connected nodes")
        assertTrue(graph.getEdge(origin, destination) != null)
    }

    private fun assertNumberOfNodes(count: Int, graph: Graph) {
        assertEquals(count, graph.getNodeCount(), "Expects #of nodes to be $count")
    }

    private fun assertNumberOfEdges(count: Int, graph: Graph) {
        assertEquals(count, graph.getEdgeCount(), "Expects #of edges to be $count")
    }

    private fun assertNumberOfExits(count: Int, graph: Graph) {
        assertEquals(count, graph.getExitCount(), "Expects #of exits to be $count")
    }
}