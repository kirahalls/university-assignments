package assign08;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @version Nov 7, 2022
 * @author Daniel Kopta and Kira Halls and Nandini Goel
 * This Graph class acts as a starting point for your maze pathfinder.
 * It calculates a path through a maze using depth first search
 * and the shortest path through a maze using breadth first search.
 */
public class Graph {

    // The graph itself is just a 2D array of nodes
    private final Node[][] nodes;

    // The node to start the path finding from
    private Node start;

    // The size of the maze
    private final int width;
    private final int height;

    // Used for BFS - calculateShortestPath
    private LinkedList<Node> queue;

    /**
     * Constructs a maze graph from the given text file.
     *
     * @param filename - the file containing the maze
     * @throws Exception
     */
    public Graph(String filename) throws Exception {
        BufferedReader input;
        input = new BufferedReader(new FileReader(filename));

        if (!input.ready()) {
            input.close();
            throw new FileNotFoundException();
        }

        // read the maze size from the file
        String[] dimensions = input.readLine().split(" ");
        height = Integer.parseInt(dimensions[0]);
        width = Integer.parseInt(dimensions[1]);

        // instantiate and populate the nodes
        nodes = new Node[height][width];
        for (int i = 0; i < height; i++) {
            String row = input.readLine().trim();

            for (int j = 0; j < row.length(); j++) {
                switch (row.charAt(j)) {
                    case 'X':
                        nodes[i][j] = new Node(i, j);
                        nodes[i][j].isWall = true;
                        break;
                    case ' ':
                        nodes[i][j] = new Node(i, j);
                        break;
                    case 'S':
                        nodes[i][j] = new Node(i, j);
                        nodes[i][j].isStart = true;
                        start = nodes[i][j];
                        break;
                    case 'G':
                        nodes[i][j] = new Node(i, j);
                        nodes[i][j].isGoal = true;
                        break;
                    default:
                        throw new IllegalArgumentException("maze contains unknown character: '" + row.charAt(j) + "'");
                }
            }
        }
        input.close();
    }

    /**
     * Outputs this graph to the specified file.
     * Use this method after you have found a path to one of the goals.
     * Before using this method, for the nodes on the path, you will need
     * to set their isOnPath value to true.
     *
     * @param filename - the file to write to
     */
    public void printGraph(String filename) {
        try {
            PrintWriter output = new PrintWriter(new FileWriter(filename));
            output.println(height + " " + width);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    output.print(nodes[i][j]);
                }
                output.println();
            }
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Traverse the graph with BFS (shortest path to closest goal)
     * A side-effect of this method should be that the nodes on the path
     * have had their isOnPath member set to true.
     *
     * @return - the length of the path
     */
    public int CalculateShortestPath() {
        Node goal = breadthFirstSearch(this.start);
        if (goal != null) {
            return calculatePathLength(goal);
        }
        return 0;
    }

    /**
     * A private helper method to calculate the shortest path using a queue.
     *
     * @param source the node to start from
     * @return the 'goal' node to reach or null if no path
     */
    private Node breadthFirstSearch(Node source) {
        Node current = source;
        Node goal = null;
        queue = new LinkedList<Node>();
        source.visited = true;
        queue.offer(source);
        while (!queue.isEmpty()) {
            current = queue.poll();
            if (current.isGoal) {
                goal = current;
                return goal;
            }
            for (Node next : getNeighbors(current)) {
                if (!next.visited) {
                    next.visited = true;
                    next.setCameFrom(current);
                    queue.offer(next);
                }
            }
        }
        return goal;
    }


    /**
     * Traverse the graph with DFS (any path to any goal)
     * A side-effect of this method should be that the nodes on the path
     * have had their isOnPath member set to true.
     *
     * @return - the length of the path
     */
    public int CalculateAPath() {
        Node goal = depthFirstSearch(start);
        if (goal != null) {
            return calculatePathLength(goal);
        }
        return 0;
    }

    /**
     * A private helper method to calculate a path from a source to a goal in a maze.
     *
     * @param source the node to start from
     * @return the 'goal' node to reach or null if no path
     */
    private Node depthFirstSearch(Node source) {
        Node goal = null;
        source.visited = true;
        if (source.isGoal) {
            goal = source;
            return goal;
        }
        for (Node next : getNeighbors(source)) {
            if (goal == null) {
                if (!next.visited) {
                    next.cameFrom = source;
                    goal = depthFirstSearch(next);
                }
            }
        }
        return goal;
    }

    /**
     * A private helper method used in BFS and DFS, calculating the length of a path if it exists.
     *
     * @param goal the 'goal' node to reach or null if no path
     * @return int length of a path
     */
    private int calculatePathLength(Node goal) {
        int length = 0;
        Node currentNode = goal;
        while (currentNode.cameFrom != null) {
            length++;
            currentNode.isOnPath = true;
            currentNode = currentNode.cameFrom;
        }
        currentNode.isOnPath = true;
        length++;
        return length;
    }

    /**
     * A private helper method used in DFS to get neighbors of a single node, excluding walls.
     *
     * @param node the current node
     * @return ArrayList of node type of all neighbors adjacent to param node
     */
    private ArrayList<Node> getNeighbors(Node node) {
        ArrayList<Node> neighbors = new ArrayList<Node>();
        for (int i = node.x - 1; i <= node.x + 1; i += 2) {
            Node currentNode = nodes[i][node.y];
            if (!(nodes[i][node.y].isWall)) {
                neighbors.add(currentNode);
            }
        }
        for (int j = node.y - 1; j <= node.y + 1; j += 2) {
            Node currentNode = nodes[node.x][j];
            if (!(nodes[node.x][j].isWall)) {
                neighbors.add(currentNode);
            }
        }
        return neighbors;
    }

    /**
     * @author Daniel Kopta, Kira Halls, and Nandini Goel
     * A node class to assist in the implementation of the graph.
     * You will need to add additional functionality to this class.
     */
    private static class Node {
        // The node's position in the maze
        private final int x;
        private final int y;

        // The type of the node
        private boolean isStart;
        private boolean isGoal;
        private boolean isOnPath;
        private boolean isWall;
        private boolean visited;
        private Node cameFrom;

        /**
         * CONSTRUCTOR FOR NODE CLASS
         *
         * @param _x x-coordinate of node location in graph
         * @param _y y-coordinate of node location in graph
         */
        public Node(int _x, int _y) {
            isStart = false;
            isGoal = false;
            isOnPath = false;
            visited = false;
            cameFrom = null;
            x = _x;
            y = _y;
        }

        /**
         * A setter method for the node's cameFrom instance variable. Used to help keep track
         * of a path through a maze.
         *
         * @param cameFrom the node that the specified node came from
         */
        public void setCameFrom(Node cameFrom) {
            this.cameFrom = cameFrom;
        }

        /**
         * An overriden toString method to aid in outputting the solved graph
         *
         * @return a string representation of the solved graph
         */
        @Override
        public String toString() {
            if (isWall)
                return "X";
            if (isStart)
                return "S";
            if (isGoal)
                return "G";
            if (isOnPath)
                return ".";
            return " ";
        }
    }
}