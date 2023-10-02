package client.main.moveHandler.pathFinding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * The GraphPathCalculator is a class which calculates all nodes that the ki needs to
 * visit, it takes in map indexes of only available map nodes(!Water) and determines them
 * based on the starting position of the player.
 * The algorithm implementation was created with the help of an article
 * from GeeksForGeeks: Breadth First Search for a Graph
 * <a href="https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/">...</a>
 */
class GraphPathCalculator {
    /**
     * TAKEN FROM START * <a href="https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/">...</a>
     * The algorithm itself which calculates was taken from the link up above, the filling
     * of the graph with the needed nodes was done by me
     */
    private final Logger logger = LoggerFactory.getLogger(GraphPathCalculator.class);
    private final LinkedList<Integer>[] adj;

    private final ArrayList<Integer> returnMoves = new ArrayList<>();
    private final boolean[] visited;

    private int position;

    // Initialize an empty linkedlist with the size of the map
    GraphPathCalculator(HashSet<Integer> mapIndexes, int startPosition){
        logger.debug("The graph path calculator received following indexes: " + mapIndexes);
        position = startPosition;
        // No. of vertices
        int givenMapSize = 50;
        visited = new boolean[givenMapSize];
        adj = new LinkedList[givenMapSize];
        for (int i = 0; i < givenMapSize; ++i)
            adj[i] = new LinkedList<>();
        fillGraphWithNodes(mapIndexes);
    }

    private void addNode(int fromNode, int toNode)
    {
        adj[fromNode].add(toNode); // Add w to v's list.
    }

    private void calculatePath()
    {
        // Mark the current node as visited and print it
        visited[this.position] = true;
        returnMoves.add(this.position);
        // Recur for all the vertices adjacent to this
        // vertex
        for (int n : adj[this.position]) {
            if (!visited[n]) {
                this.position = n;
                calculatePath();
            }
        }
    }
    // TAKEN FROM END

    private void fillGraphWithNodes(HashSet<Integer> mapIndexes){
        for (int value : mapIndexes)
            if(value == 0 || value == 9 || value == 40 || value == 49) addEdgesIntoGraph(value, mapIndexes);
            else if (value % 10 == 0 || value == 19 || value == 29 || value == 39) addSidesIntoGraph(value, mapIndexes);
            else if (value < 9 || value > 40) addTopsIntoGraph(value, mapIndexes);
            else addMiddleNodesIntoGraph(value, mapIndexes);
    }


    private void addSidesIntoGraph(int input, HashSet<Integer> mapIndexes){
        if (mapIndexes.contains(input - 10))
            addNode(input, input - 10);
        if (mapIndexes.contains(input + 10))
            addNode(input, input + 10);

        if(input % 10 == 0){
            if (mapIndexes.contains(input + 1))
                addNode(input, input + 1);
            return;
        }
        if (mapIndexes.contains(input - 1))
            addNode(input, input - 1);
    }

    private void addTopsIntoGraph(int input, HashSet<Integer> mapIndexes){
        if (mapIndexes.contains(input + 1))
            addNode(input, input+1);
        if (mapIndexes.contains(input - 1))
            addNode(input, input-1);
        if(input < 9){
            if (mapIndexes.contains(input + 10))
                addNode(input, input+10);
            return;
        }
        if (mapIndexes.contains(input - 10))
            addNode(input, input-10);
    }


    private void addEdgesIntoGraph(int input, HashSet<Integer> mapIndexes){
        switch (input) {
            case 0 -> {
                //It is possible that the field to the right is made of water
                if (mapIndexes.contains(input+1))
                    addNode(input, input + 1);
                if (mapIndexes.contains(input + 10))
                    addNode(input, input + 10);
            }
            case 9 -> {
                if (mapIndexes.contains(input + 10))
                    addNode(input, input + 10);
                if (mapIndexes.contains(input-1))
                    addNode(input, input - 1);
            }
            case 40 -> {
                if (mapIndexes.contains(input - 10))
                    addNode(input, input - 10);
                if (mapIndexes.contains(input+1))
                    addNode(input, input + 1);
            }
            case 49 -> {
                if (mapIndexes.contains(input - 10))
                    addNode(input, input - 10);
                if (mapIndexes.contains(input-1))
                    addNode(input, input - 1);
            }
        }
    }


    private void addMiddleNodesIntoGraph(int input, HashSet<Integer> mapIndexes){
        if (mapIndexes.contains(input + 10))
            addNode(input, input+10);
        if (mapIndexes.contains(input - 10))
            addNode(input, input-10);
        if (mapIndexes.contains(input - 1))
            addNode(input, input-1);
        if (mapIndexes.contains(input + 1))
            addNode(input, input+1);
    }

    public Map.Entry<LinkedList<Integer>[], ArrayList<Integer>> getReturnMoves() {
        calculatePath();
        logger.debug("Based on the input the calculator came up with following path: " + returnMoves);
        return Map.entry(adj, returnMoves);
    }

}
