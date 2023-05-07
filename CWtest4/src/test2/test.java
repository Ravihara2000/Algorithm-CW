package test2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Graph2 {
    private int V; // No. of vertices

    public int getV() {
        return V;
    }

    public void setV(int v) {
        V = v;
    }

    public List<Integer>[] getAdj() {
        return adj;
    }

    public void setAdj(List<Integer>[] adj) {
        this.adj = adj;
    }

    private List<Integer>[] adj; // Array of adjacency lists

    public Graph2(int V) {
        this.V = V;
        adj = new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    public void addEdge(int v, int w) {
        adj[v].add(w); // Add w to v's list
    }

    private void removeSinkVertices(boolean[] visited) {
        System.out.println();
        System.out.println("*--------------------------------------------*");
        boolean sinkVertexFound = true;

        while (sinkVertexFound) {
            sinkVertexFound = false;

            for (int i = 0; i < V; i++) {
                if (!visited[i] && adj[i].isEmpty()) {
                    visited[i] = true;

                    System.out.println("Found and eliminated sink vertex: " + i);

                    for (List<Integer> list : adj) {
                        list.remove(Integer.valueOf(i));
                    }
                    sinkVertexFound = true;
                    break;
                }
            }
        }
        System.out.println("*--------------------------------------------*");
    }

    private boolean isCyclicUtil(int v, boolean[] visited) {
        visited[v] = true;

        // Recur for all the vertices adjacent to this vertex
        for (int i : adj[v]) {
            if (!visited[i] || isCyclicUtil(i, visited)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCyclic() {
        boolean[] visited = new boolean[V];
        removeSinkVertices(visited);

        // Check if any cycles exist in the modified graph
        for (int i = 0; i < V; i++) {
            if (!visited[i] && isCyclicUtil(i, visited)) {
                return true;
            }
        }

        return false;
    }

    public static Graph2 parseGraphFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        // Read the first line to determine the number of vertices
        line = reader.readLine();
        int V = Integer.parseInt(line);
        Graph2 graph = new Graph2(V);

        // Read the remaining lines to add edges to the graph
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split(" ");
            int v = Integer.parseInt(tokens[0]) - 1;
            int w = Integer.parseInt(tokens[1]) - 1;
            graph.addEdge(v, w);
        }

        reader.close();
        return graph;
    }
}

public class test {
    public static void main(String[] args) {
        try {
            Graph2 g = Graph2.parseGraphFromFile("input.txt");

            // Print all vertices
            System.out.println("Vertices:");
            for (int i = 1; i <= g.getV(); i++) {
                System.out.print(i + " ");
            }
            System.out.println();

            // Print all edges
            System.out.println("Edges:");
            for (int i = 1; i < g.getV(); i++) {
                for (int j : g.getAdj()[i]) {
                    System.out.println(i + " -> " + j);
                }
            }

            if (g.isCyclic()) {
                System.out.println();
                System.out.println("*---------------------------*");
                System.out.println("Graph is a cyclic");
                System.out.println("*---------------------------*");
                System.out.println();
            } else {
                System.out.println();
                System.out.println("*---------------------------*");
                System.out.println("Graph is acyclic");
                System.out.println("*---------------------------*");
                System.out.println();
            }
        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
        }
    }
}
