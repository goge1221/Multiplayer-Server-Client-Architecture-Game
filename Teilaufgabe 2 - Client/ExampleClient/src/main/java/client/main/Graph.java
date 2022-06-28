package client.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Graph {
    private int mapSize; // No. of vertices

    private LinkedList<Integer> adj[];

    private ArrayList<Integer>returnMoves = new ArrayList<>();
    private boolean[] visited;

    private int position;

    // Initialize an empty linkedlist with the size of the map
    Graph(int position, boolean horizontalMapForm){
        this.position = position;
        this.mapSize = 32;
        visited = new boolean[mapSize];
        adj = new LinkedList[mapSize];
        for (int i = 0; i < mapSize; ++i)
            adj[i] = new LinkedList();
        if(horizontalMapForm) fillHorizontalGraph();
//        else fillVerticalGraph();
    }

    private void fillHorizontalGraph(){
        for(int i = 0; i < mapSize; ++i){
            if(i == 0 || i == 7 || i == 24 || i == 31) addEdgesIntoGraph(i);
            else if (i == 8 || i == 16 || i == 15 || i == 23) addSidesIntoGraph(i);
            else if ((i >= 1 && i <= 6) ||( i >= 25 && i <= 30)) addTopsIntoGraph(i);
            else addMiddleNodesIntoGraph(i);
        }
    }

    private void addMiddleNodesIntoGraph(int input){
        addNode(input, input+8);
        addNode(input, input-8);
        addNode(input, input-1);
        addNode(input, input+1);
    }
    private void addTopsIntoGraph(int input){
        addNode(input, input+1);
        addNode(input, input-1);
        if(input >= 25 && input <= 30){
            addNode(input, input-8);
            return;
        }
        addNode(input, input+8);
    }

    private void addSidesIntoGraph(int input){
        addNode(input, input -8);
        addNode(input, input +8);
        if(input == 8 || input == 16){
            addNode(input, input + 1);
            return;
        }
        addNode(input, input - 1);
    }
    private void addEdgesIntoGraph(int input){
        switch (input){
            case 0 : {
                addNode(input, input+1);
                addNode(input, input+8);
                return;
            }
            case 7 : {
                addNode(input, input+8);
                addNode(input, input-1);
                return;
            }
            case 24 : {
                addNode(input, input-8);
                addNode(input, input+1);
                return;
            }
            case 31: {
                addNode(input, input-8);
                addNode(input, input-1);
                return;
            }
        }
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
        Iterator<Integer> i = adj[this.position].listIterator();
        while (i.hasNext()) {
            int n = i.next();
            if (!visited[n]) {
                this.position = n;
                calculatePath();
            }
        }
    }

    public ArrayList<Integer> getReturnMoves() {
        calculatePath();
        return returnMoves;
    }


}