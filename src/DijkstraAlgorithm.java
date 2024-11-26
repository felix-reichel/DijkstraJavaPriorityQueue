import java.util.*;


// This is a very elegant way to run the Dijkstra Greedy Algorithm in Java with the usage of a Java PriorityQueue.

// Taken from:  https://lernjava.de/dijkstra-algorithmus-in-java/


// with some minor refinements... e.g. handling bi-directionality.

public class DijkstraAlgorithm {

    public static void dijkstra(Map<Node, Map<Node, Integer>> graph, Node start) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.getDistance()));
        start.setDistance(0);
        priorityQueue.add(start);

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();

            // Skip nodes that have already been processed with a shorter distance
            if (current.getDistance() == Integer.MAX_VALUE) continue;

            for (Map.Entry<Node, Integer> neighborEntry : graph.getOrDefault(current, new HashMap<>()).entrySet()) {
                Node neighbor = neighborEntry.getKey();
                int edgeWeight = neighborEntry.getValue();
                int newDistance = current.getDistance() + edgeWeight;

                // Relaxation step
                if (newDistance < neighbor.getDistance()) {
                    neighbor.setDistance(newDistance);
                    neighbor.setPrevious(current);
                    priorityQueue.add(neighbor);
                }
            }
        }
    }

    public static void main(String[] args) {
        Node linz = new Node("Linz");
        Node wels = new Node("Wels");
        Node gmunden = new Node("Gmunden");
        Node steyr = new Node("Steyr");
        Node vocklabruck = new Node("Vöcklabruck");
        Node enns = new Node("Enns");
        Node schwanenstadt = new Node("Schwanenstadt");
        Node badIschl = new Node("Bad Ischl");
        Node traun = new Node("Traun");
        Node eferding = new Node("Eferding");

        Map<Node, Map<Node, Integer>> graph = new HashMap<>();
        addBiDirectionalEdge(graph, linz, wels, 30);
        addBiDirectionalEdge(graph, linz, steyr, 38);
        addBiDirectionalEdge(graph, linz, enns, 20);
        addBiDirectionalEdge(graph, linz, traun, 15);
        addBiDirectionalEdge(graph, wels, gmunden, 35);
        addBiDirectionalEdge(graph, wels, vocklabruck, 25);
        addBiDirectionalEdge(graph, wels, schwanenstadt, 20);
        addBiDirectionalEdge(graph, gmunden, vocklabruck, 15);
        addBiDirectionalEdge(graph, gmunden, badIschl, 25);
        addBiDirectionalEdge(graph, steyr, traun, 40);
        addBiDirectionalEdge(graph, steyr, enns, 18);
        addBiDirectionalEdge(graph, enns, eferding, 22);
        addBiDirectionalEdge(graph, eferding, wels, 35);

        // Apply Dijkstra's algorithm from Linz
        dijkstra(graph, linz);

        System.out.println("Shortest distances from Linz:");
        for (Node node : graph.keySet()) {
            String distance = node.getDistance() == Integer.MAX_VALUE ? "Unreachable" : String.valueOf(node.getDistance());
            System.out.println(node.getName() + ": " + distance);
        }

        System.out.println("\nShortest path from Linz to Gmunden:");
        printPath(gmunden);
        System.out.println("\nTotal Distance: " + (gmunden.getDistance() == Integer.MAX_VALUE ? "Unreachable" : gmunden.getDistance()));
    }

    // Add bi-directional edges to the graph
    public static void addBiDirectionalEdge(Map<Node, Map<Node, Integer>> graph, Node from, Node to, int distance) {
        graph.putIfAbsent(from, new HashMap<>());
        graph.putIfAbsent(to, new HashMap<>());
        graph.get(from).put(to, distance);
        graph.get(to).put(from, distance);
    }

    // Print the path to the target node
    public static void printPath(Node target) {
        if (target.getDistance() == Integer.MAX_VALUE) {
            System.out.print("No path exists.");
            return;
        }
        if (target.getPrevious() != null) {
            printPath(target.getPrevious());
        }
        System.out.print(target.getName() + " ");
    }
}

class Node {
    private final String name;
    private int distance = Integer.MAX_VALUE; // Initially set to "infinity"
    private Node previous = null;

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    @Override
    public String toString() {
        return name;
    }
}