package client.main.moveHandler;


import client.main.exceptions.NodesNotConnectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * The Calculator between two points calculates an alternative route if there
 * is no direct link between two points using the Floyd Marshall Algorithm.
 * This implementation heavily uses concepts and parts from the algorithm
 * on GeeksForGeeks: shortest path in an unweighted graph.
 */
class CalculatorBetweenTwoPoints {
//TAKEN FROM START <a href="https://www.geeksforgeeks.org/shortest-path-unweighted-graph/">...</a>>
//The algorithm used to use a ArrayList to store the connections between the different points
//For my implementation i decided to reuse the created connections between the points which were
//stored in form of a linked list in the GraphPathCalculator
//The rest of the algorithm was used as shown in the link above
    private LinkedList<Integer>[] adj;

    private Logger logger = LoggerFactory.getLogger(CalculatorBetweenTwoPoints.class);

    public CalculatorBetweenTwoPoints(){}
    public void updateAdjListAndMovesList(Map.Entry<LinkedList<Integer>[], ArrayList<Integer>> path) {
        adj = path.getKey();
    }

    public ArrayList<Integer> getShortestDistance(int startPoint, int destinationPoint, int size){

        // predecessor[i] array stores predecessor of
        // i and distance array stores distance of i
        // from s
        int[] pred = new int[size];
        int[] dist = new int[size];

        if (!BFS(startPoint, destinationPoint, size, pred, dist))
            throw new NodesNotConnectedException(
                    startPoint + " and " + destinationPoint + " not connected. Cannot reach destination.");

        // LinkedList to store path
        LinkedList<Integer> path = new LinkedList<>();
        int crawl = destinationPoint;
        path.add(crawl);
        while (pred[crawl] != -1) {
            path.add(pred[crawl]);
            crawl = pred[crawl];
        }

        // Print distance
        logger.info("Shortest path length between " + startPoint + " and " + destinationPoint + " is " + dist[destinationPoint]);

        ArrayList<Integer>toReturnPath = new ArrayList<>();

        for (int i = path.size() - 1; i >= 0; i--) {
            toReturnPath.add(path.get(i));
        }

        return toReturnPath;
    }

    // a modified version of BFS that stores predecessor
    // of each vertex in array pred
    // and its distance from source in array dist
    private boolean BFS(int src, int dest, int size, int[] pred, int[] dist) {
        // a queue to maintain queue of vertices whose
        // adjacency list is to be scanned as per normal
        // BFS algorithm using LinkedList of Integer type
        LinkedList<Integer> queue = new LinkedList<>();

        // boolean array visited[] which stores the
        // information whether ith vertex is reached
        // at least once in the Breadth first search
        boolean[] visited = new boolean[size];

        // initially all vertices are unvisited
        // so v[i] for all i is false
        // and as no path is yet constructed
        // dist[i] for all i set to infinity
        for (int i = 0; i < size; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        // now source is first to be visited and
        // distance from source to itself should be 0
        visited[src] = true;
        dist[src] = 0;
        queue.add(src);

        // bfs Algorithm
        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < adj[u].size(); i++) {
                if (!visited[adj[u].get(i)]) {
                    visited[adj[u].get(i)] = true;
                    dist[adj[u].get(i)] = dist[u] + 1;
                    pred[adj[u].get(i)] = u;
                    queue.add(adj[u].get(i));

                    // stopping condition (when we find
                    // our destination)
                    if (adj[u].get(i) == dest)
                        return true;
                }
            }
        }
        return false;
    }
    //TAKEN FROM END
}
